/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knowyourcountry;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
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
import knowyourcountry.abs.GetGenericData;
import knowyourcountry.abs.GetGenericDataResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author rob
 */
public class DataServlet extends HttpServlet
{
    private NamespaceContext namespaces = new NamespaceContext() {

        @Override
        public String getNamespaceURI(String prefix)
        {
            String uri = null;
            if (prefix.equals("m"))
            {
                System.err.println("namespace:m");
                uri = "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message";
            }
            else if (prefix.equals("n"))
            {
                System.err.println("namespace:n");
                uri = "http://www.SDMX.org/resources/SDMXML/schemas/v2_0/generic";
            }
            return uri;
        }

        @Override
        public String getPrefix(String string)
        {
            throw new UnsupportedOperationException("Not needed.");
        }

        @Override
        public Iterator getPrefixes(String string)
        {
            throw new UnsupportedOperationException("Not needed.");
        }
    };
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        resp.setContentType("application/json");
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        
        ABSStatX0020SDMXX0020WebX0020Service service = new ABSStatX0020SDMXX0020WebX0020Service();
        
        GetGenericData.QueryMessage qm = new GetGenericData.QueryMessage();
        
        Document doc = docFromResource("/knowyourcountry/abs/hardcoded.xml");
        
        qm.setAny(doc.getDocumentElement());
        
        GetGenericDataResponse.GetGenericDataResult result
            = service.getABSStatX0020SDMXX0020WebX0020ServiceSoap().getGenericData(qm);
        
        Node body = (Node) result.getAny();
        //Node match = xpathToNode("/m:MessageGroup", body, namespaces);
        NodeList match = xpathToNodeList("/m:MessageGroup/n:DataSet/n:Series", body, namespaces);
        JSONArray ja = new JSONArray();
        for (int i = 0; i < match.getLength(); i++)
        {
            String value = xpathToString("n:Obs/n:ObsValue/@value", match.item(i), namespaces);
            if (!value.equals("NaN"))
            {
                JSONObject jo = new JSONObject();
                jo.put("ASGC_2008", xpathToString("n:SeriesKey/n:Value[@concept='ASGC_2008']/@value", match.item(i), namespaces));
                jo.put("value", value);
                ja.add(jo);
            }
        }
        out.print(ja.toJSONString());
        out.flush();
        out.close();
    }
    
    private String toString(NodeList nodes) throws ServletException
    {
        StringBuilder text = new StringBuilder();
        
        int l = nodes.getLength();
        for (int i = 0; i < l; i++)
        {
            text.append(toString(nodes.item(i)));
        }
        
        return text.toString();
    }
    
    private String toString(Node node) throws ServletException
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
    
    private String xpathToString(String expression, Node target, NamespaceContext namespaces) 
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
    
    private Node xpathToNode(String expression, Node target, NamespaceContext namespaces) 
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
    
    private NodeList xpathToNodeList(String expression, Node target, NamespaceContext namespaces) 
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
    
    private Document docFromResource(String resourcePath) throws ServletException, IOException
    {
        try
        {
            return newDocumentBuilder().parse(getClass().getResourceAsStream(resourcePath));
        }
        catch (SAXException e)
        {
            throw new ServletException(e);
        }
    }

    private DocumentBuilder newDocumentBuilder() throws ServletException
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

    private XPathExpression newXpathExpression(String expression, NamespaceContext namespaces) 
            throws XPathExpressionException
    {
        XPathExpression xpe;
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        xp.setNamespaceContext(namespaces);
        xpe = xp.compile(expression);
        return xpe;
    }
    
    
}
