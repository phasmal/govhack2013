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
        
        ElementNSImpl el = ((ElementNSImpl)result.getAny());
        out.print(toString(el.getChildNodes()));
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
    
    private Document docFromResource(String resourcePath) throws ServletException, IOException
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(getClass().getResourceAsStream(resourcePath));
            return doc;
        }
        catch (ParserConfigurationException e)
        {
            throw new ServletException(e);
        }
        catch (SAXException e)
        {
            throw new ServletException(e);
        }
    }
    
    
}
