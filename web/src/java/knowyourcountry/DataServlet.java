/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knowyourcountry;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException
    {
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        
        ABSStatX0020SDMXX0020WebX0020Service service = new ABSStatX0020SDMXX0020WebX0020Service();
        
        GetGenericData.QueryMessage qm = new GetGenericData.QueryMessage();
        
        Document doc = docFromResource("/knowyourcountry/abs/hardcoded.xml");
        
        qm.setAny(doc.getDocumentElement());
        
        GetGenericDataResponse.GetGenericDataResult result
            = service.getABSStatX0020SDMXX0020WebX0020ServiceSoap().getGenericData(qm);
        
        out.print(toString((Node) result.getAny()));
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
    
    private NodeList xpathToNodeList(String expression, Node target) throws ServletException
    {
        try
        {
            XPathExpression xpe = newXpathExpression(expression);
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

    private XPathExpression newXpathExpression(String expression) throws XPathExpressionException
    {
        XPathExpression xpe;
        XPathFactory xpf = XPathFactory.newInstance();
        XPath xp = xpf.newXPath();
        xpe = xp.compile(expression);
        return xpe;
    }
    
    
}
