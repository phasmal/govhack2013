/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knowyourcountry.abs;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;

/**
 *
 * @author rob
 */
@WebServiceClient(name = "ABS.Stat_x0020_SDMX_x0020_web_x0020_service", targetNamespace = "http://stats.oecd.org/OECDStatWS/SDMX/", wsdlLocation = "http://stat.abs.gov.au/sdmxws/sdmx.asmx?wsdl")
public class Service extends javax.xml.ws.Service
{
    
    public Service()
    {
        super(newUrl("http://stat.abs.gov.au/sdmxws/sdmx.asmx?wsdl"), new QName(""));
    }
    
    private static URL newUrl(String urlText)
    {
        try
        {
            return new URL(urlText);
        }
        catch (MalformedURLException e)
        {
            throw new WebServiceException(e);
        }
    }
}
