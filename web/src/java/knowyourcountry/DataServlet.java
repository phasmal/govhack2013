/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knowyourcountry;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import knowyourcountry.abs.ABSStatX0020SDMXX0020WebX0020Service;
import knowyourcountry.abs.GetGenericData;
import knowyourcountry.abs.GetGenericDataResponse;
import org.w3c.dom.Document;
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
        ABSStatX0020SDMXX0020WebX0020Service service = new ABSStatX0020SDMXX0020WebX0020Service();
        
        GetGenericData.QueryMessage qm = new GetGenericData.QueryMessage();
        
        Document doc = docFromResource("/knowyourcountry/abs/hardcoded.xml");
        
        qm.setAny(doc.getDocumentElement());
        
        GetGenericDataResponse.GetGenericDataResult result
            = service.getABSStatX0020SDMXX0020WebX0020ServiceSoap().getGenericData(qm);
        
        PrintWriter out = new PrintWriter(resp.getOutputStream());
        out.print(result.getAny().getClass());
        out.flush();
        out.close();
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
