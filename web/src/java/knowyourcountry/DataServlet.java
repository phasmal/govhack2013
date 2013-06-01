/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knowyourcountry;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import knowyourcountry.abs.ABSStatX0020SDMXX0020WebX0020Service;
import knowyourcountry.abs.GetDataStructureDefinition;
import knowyourcountry.abs.GetDataStructureDefinitionResponse;
import knowyourcountry.abs.GetGenericData;
import knowyourcountry.abs.GetGenericDataResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author rob
 */
public class DataServlet extends HttpServlet
{
    private static NamespaceContext namespaces = new NsContext(
                map("m", "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message",
                    "g", "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/generic",
                    "s", "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/structure",
                    "xml", "http://www.w3.org/XML/1998/namespace"));
    
    private static Index index = null;
    
    private static synchronized Index getIndex()
    {
        if (index == null)
        {
            index = loadIndex();
        }
        
        return index;
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        
        // /api/region/801/children/children/type/19/time/2010
        String pi = req.getPathInfo();
        if (pi.startsWith("/")) pi = pi.substring(1);
        String[] parts = pi.split("/");
        System.err.println("PI " + pi + " -> " + parts.length);
        
        if (parts.length == 2 
            && parts[0].equals("region"))
        {
            resp.setContentType("application/json");
            out.print(getIndex().get(parts[1]));
        }
        else if (parts.length >= 8
                 && parts[0].equals("region")
                 && parts[1].equals("801")
                 && parts[2].equals("children")
                 && parts[3].equals("children")
                 && parts[4].equals("type")
                 && parts[5].equals("19")
                 && parts[6].equals("time")
                 && parts[7].equals("2010"))
        {
            resp.setContentType("application/json");
            outputData(out);
        }
        else
        {
            resp.setStatus(404);
            out.print("Not Found");
        }
        
        out.flush();
        out.close();
    }
    
    private void outputData(PrintWriter out) throws ServletException, IOException
    {
        ABSStatX0020SDMXX0020WebX0020Service service = new ABSStatX0020SDMXX0020WebX0020Service();
        
        GetGenericData.QueryMessage qm = new GetGenericData.QueryMessage();
        
        Document doc = docFromResource("/knowyourcountry/abs/hardcoded-data.xml");
        
        qm.setAny(doc.getDocumentElement());
        
        GetGenericDataResponse.GetGenericDataResult result
            = service.getABSStatX0020SDMXX0020WebX0020ServiceSoap().getGenericData(qm);
        
        Node body = (Node) result.getAny();
        
        NodeList match = xpathToNodeList("/m:MessageGroup/g:DataSet/g:Series", body, namespaces);
        JSONArray ja = new JSONArray();
        for (int i = 0; i < match.getLength(); i++)
        {
            String value = xpathToString("g:Obs/g:ObsValue/@value", match.item(i), namespaces);
            if (!value.equals("NaN"))
            {
                JSONObject jo = new JSONObject();
                String regionCode = xpathToString("g:SeriesKey/g:Value[@concept='ASGC_2008']/@value", match.item(i), namespaces);
                Index.Region r = getIndex().get(regionCode);
                jo.put("region", r == null ? null : r.toJson());
                jo.put("value", value);
                ja.add(jo);
            }
        }
        out.print(ja.toJSONString());
    }
    
    
    private static Index loadIndex()
    {
        try
        {
            ABSStatX0020SDMXX0020WebX0020Service service = new ABSStatX0020SDMXX0020WebX0020Service();
            
            GetDataStructureDefinition.QueryMessage qm = new GetDataStructureDefinition.QueryMessage();
            
            Document doc = docFromResource("/knowyourcountry/abs/hardcoded-index.xml");
            
            qm.setAny(doc.getDocumentElement());
            GetDataStructureDefinitionResponse.GetDataStructureDefinitionResult result
                = service.getABSStatX0020SDMXX0020WebX0020ServiceSoap().getDataStructureDefinition(qm);
            
            Node body = (Node) result.getAny();
        
            Index idx = new Index();
        
            //System.err.println("X=" + toString(xpathToNodeList("/m:Structure/m:CodeLists/s:CodeList", body, namespaces)));
            
            NodeList match = xpathToNodeList("/m:Structure/m:CodeLists/s:CodeList[@id='CL_NRP7_ASGC_2008']/s:Code", body, namespaces);

            for (int i = 0; i < match.getLength(); i++)
            {
                Node item = match.item(i);
                String code = xpathToString("@value", item, namespaces);
                String parentCode = xpathToString("@parentCode", item, namespaces);
                
                String name = null;
                NodeList descs = xpathToNodeList("s:Description", item, namespaces);
                for (int j = 0; j < descs.getLength(); j++)
                {
                    Node d = descs.item(j);
                    
                    NamedNodeMap attrs = d.getAttributes();
                    for (int k = 0; k < attrs.getLength(); k++)
                    {
                        Node a = attrs.item(k);
                        if (a.getPrefix().equals("xml") && a.getLocalName().equals("lang") && a.getTextContent().equals("en"))
                        {
                            name = d.getTextContent();
                        }
                    }
                }
//                System.err.println("item : " + new Index.Region(code, parentCode,name).toJson().toJSONString());
                idx.addRegion(code, parentCode, name);
            } 
//            System.err.println("Index:");
//            System.err.println(idx);
            return idx;
        }
        catch (ServletException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
    
    
    private static String toString(NodeList nodes) throws ServletException
    {
        StringBuilder text = new StringBuilder();
        
        int l = nodes.getLength();
        for (int i = 0; i < l; i++)
        {
            text.append(toString(nodes.item(i)));
        }
        
        return text.toString();
    }
    
    private static String toString(Node node) throws ServletException
    {
        try
        {
            StringWriter sw = new StringWriter();
            StreamResult sr = new StreamResult(sw);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            t.transform(new DOMSource(node), sr);
            return sw.toString();
        }
        catch (TransformerConfigurationException e)
        {
            throw new ServletException(e);
        }
        catch (TransformerException e)
        {
            throw new ServletException(e);
        }
        
    }
    
    private static String xpathToString(String expression, Node target, NamespaceContext namespaces) 
            throws ServletException
    {
        try
        {
            XPathExpression xpe = newXpathExpression(expression, namespaces);
            return (String) xpe.evaluate(target, XPathConstants.STRING);
        }
        catch (XPathExpressionException e)
        {
            throw new ServletException(e);
        }
    }
    
    private static Node xpathToNode(String expression, Node target, NamespaceContext namespaces) 
            throws ServletException
    {
        try
        {
            XPathExpression xpe = newXpathExpression(expression, namespaces);
            return (Node) xpe.evaluate(target, XPathConstants.NODE);
        }
        catch (XPathExpressionException e)
        {
            throw new ServletException(e);
        }
    }
    
    private static NodeList xpathToNodeList(String expression, Node target, NamespaceContext namespaces) 
            throws ServletException
    {
        try
        {
            XPathExpression xpe = newXpathExpression(expression, namespaces);
            return (NodeList) xpe.evaluate(target, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e)
        {
            throw new ServletException(e);
        }
    }
    
    private static Document docFromResource(String resourcePath) throws ServletException, IOException
    {
        try
        {
            return newDocumentBuilder().parse(DataServlet.class.getResourceAsStream(resourcePath));
        }
        catch (SAXException e)
        {
            throw new ServletException(e);
        }
    }

    private static DocumentBuilder newDocumentBuilder() throws ServletException
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            return dbf.newDocumentBuilder();
        }
        catch (ParserConfigurationException e)
        {
            throw new ServletException(e);
        }
    }

    private static XPathExpression newXpathExpression(String expression, NamespaceContext namespaces) 
            throws XPathExpressionException
    {
        XPathExpression xpe;
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        xp.setNamespaceContext(namespaces);
        xpe = xp.compile(expression);
        return xpe;
    }
    
    private static Map<String, String> map(String... strings) 
    {
        if (strings.length % 2 != 0)
        {
            throw new IllegalArgumentException(
                    "The array has to be of an even size - size is "
                            + strings.length);
        }

        Map<String, String> values = new HashMap<String, String>();

        for (int x = 0; x < strings.length; x += 2)
        {
            values.put((String) strings[x], strings[x + 1]);
        }

        return values;
    }
    
    public static class Index
    {
        private Map<String, Region> regions = new HashMap<String, Region>();
        
        private void addRegion(String code, String parentCode, String name)
        {
            regions.put(code, new Region(code, parentCode, name));
        }
        
        public Region get(String code)
        {
            return regions.get(code);
        }
        
        public static class Region
        {
            public String code;
            public String parentCode;
            public String name;
            
            public Region(String code, String parentCode, String name)
            {
                this.code = code;
                this.parentCode = parentCode;
                this.name = name;
            }
            
            public JSONObject toJson()
            {
                JSONObject j = new JSONObject();
                j.put("name", name);
                j.put("code", code);
                j.put("parent", "/api/region/" + parentCode);
                j.put("children", "/api/region/" + code + "/children");
                JSONObject shape = new JSONObject();
                shape.put(
                    "jsonp", 
                    "http://www.censusdata.abs.gov.au/arcgis/rest/services/FIND/MapServer/17/query"
                    + "?geometryType=esriGeometryEnvelope"
                    + "&spatialRel=esriSpatialRelIntersects"
                    + "&returnCountOnly=false"
                    + "&returnIdsOnly=false"
                    + "&returnGeometry=true"
                    + "&f=pjson"
                    + "&text=" + name);
                j.put("shape", shape);
                return j;
            }
        }
        
        public String toString()
        {
            List<String> codes = new ArrayList<String>(regions.keySet());
            Collections.sort(codes);
            JSONObject o = new JSONObject();
            for (String code: codes)
            {
                o.put(code, regions.get(code).toJson());
            }
            return o.toJSONString();
        }
        
    }
}
