
package knowyourcountry.abs;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This service gives access to .Stat data and tries to be fully SDMX 2.0 compliant. However, the following additional rules are applied:<br><br><b>(1)</b> .Stat provides currently only annual, bi-annual, quarterly, monthly and non-time-series data.<br>Independently from the presence of a FREQUENCY dimension, the requested time frequency should be specified using the TIME_FORMAT attribute as in this example:<br><br>&lt;And&gt;<br>&nbsp;&nbsp;<b>&lt;Attribute name="TIME_FORMAT"&gt;P1M&lt;/Attribute&gt;</b><br>&nbsp;&nbsp;&lt;Time&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&lt;StartTime&gt;1960-01&lt;/StartTime&gt;<br>&nbsp;&nbsp;&nbsp;&nbsp;&lt;EndTime&gt;2005-10&lt;/EndTime&gt;<br>&nbsp;&nbsp;&lt;/Time&gt;<br>&lt;/And&gt;<br><br>The TIME_FORMAT attribute should have the following value: 'P1Y' for annual, 'P6M' for bi-annual, 'P3M' for quarterly and 'P1M' for monthly data. This attribute is then also present in the resulting generic data message at the Series level of time-series data.<br>For non-time-series data, the TIME_PERIOD attribute and the 'Time' node should be omitted in the query message. In this case, the response messages use '9999' for the obligatory time periods.<br><br><b>(2)</b> In the .Stat data warehouse each dataset (multidimensional cube) has its own Data Structure Definition.<br><br><b>(3)</b> Structure your query by defining separate data 'cubes'. Each data cube must have a 'DataSet' node. This 'DataSet' node must be a direct child of one separate 'And' clause which must include all necessary dimension codes at any lower (child) level, and which itself must be a direct child of either of the clauses 'DataWhere' or 'Or'.<br><br><b>(4)</b> For simplification reasons in this web service, the returned message headers only contain required information. As the 'message:ID' field is not (yet) used, its content is filled with a placeholder to conform to the standard.<br><br><b>(5)</b> Our new SDMX web service version implements Streaming, and can therefore not set the 'Truncated' element in the response message. We are thus unable to take the 'defaultLimit' attribute in the Query message into account. To avoid errors, either implement streaming also at your (client) side to allow for greater-sized results or restrict the selection in the query message according to your limitation. In any case, please don't use the 'defaultLimit' attribute in the Query message.<br><br>
 * 
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "ABS.Stat_x0020_SDMX_x0020_web_x0020_service", targetNamespace = "http://stats.oecd.org/OECDStatWS/SDMX/", wsdlLocation = "http://stat.abs.gov.au/sdmxws/sdmx.asmx?wsdl")
public class ABSStatX0020SDMXX0020WebX0020Service
    extends Service
{

    private final static URL ABSSTATX0020SDMXX0020WEBX0020SERVICE_WSDL_LOCATION;
    private final static WebServiceException ABSSTATX0020SDMXX0020WEBX0020SERVICE_EXCEPTION;
    private final static QName ABSSTATX0020SDMXX0020WEBX0020SERVICE_QNAME = new QName("http://stats.oecd.org/OECDStatWS/SDMX/", "ABS.Stat_x0020_SDMX_x0020_web_x0020_service");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://stat.abs.gov.au/sdmxws/sdmx.asmx?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        ABSSTATX0020SDMXX0020WEBX0020SERVICE_WSDL_LOCATION = url;
        ABSSTATX0020SDMXX0020WEBX0020SERVICE_EXCEPTION = e;
    }

    public ABSStatX0020SDMXX0020WebX0020Service() {
        super(__getWsdlLocation(), ABSSTATX0020SDMXX0020WEBX0020SERVICE_QNAME);
    }

    public ABSStatX0020SDMXX0020WebX0020Service(WebServiceFeature... features) {
        super(__getWsdlLocation(), ABSSTATX0020SDMXX0020WEBX0020SERVICE_QNAME, features);
    }

    public ABSStatX0020SDMXX0020WebX0020Service(URL wsdlLocation) {
        super(wsdlLocation, ABSSTATX0020SDMXX0020WEBX0020SERVICE_QNAME);
    }

    public ABSStatX0020SDMXX0020WebX0020Service(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, ABSSTATX0020SDMXX0020WEBX0020SERVICE_QNAME, features);
    }

    public ABSStatX0020SDMXX0020WebX0020Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ABSStatX0020SDMXX0020WebX0020Service(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns ABSStatX0020SDMXX0020WebX0020ServiceSoap
     */
    @WebEndpoint(name = "ABS.Stat_x0020_SDMX_x0020_web_x0020_serviceSoap")
    public ABSStatX0020SDMXX0020WebX0020ServiceSoap getABSStatX0020SDMXX0020WebX0020ServiceSoap() {
        return super.getPort(new QName("http://stats.oecd.org/OECDStatWS/SDMX/", "ABS.Stat_x0020_SDMX_x0020_web_x0020_serviceSoap"), ABSStatX0020SDMXX0020WebX0020ServiceSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ABSStatX0020SDMXX0020WebX0020ServiceSoap
     */
    @WebEndpoint(name = "ABS.Stat_x0020_SDMX_x0020_web_x0020_serviceSoap")
    public ABSStatX0020SDMXX0020WebX0020ServiceSoap getABSStatX0020SDMXX0020WebX0020ServiceSoap(WebServiceFeature... features) {
        return super.getPort(new QName("http://stats.oecd.org/OECDStatWS/SDMX/", "ABS.Stat_x0020_SDMX_x0020_web_x0020_serviceSoap"), ABSStatX0020SDMXX0020WebX0020ServiceSoap.class, features);
    }

    private static URL __getWsdlLocation() {
        if (ABSSTATX0020SDMXX0020WEBX0020SERVICE_EXCEPTION!= null) {
            throw ABSSTATX0020SDMXX0020WEBX0020SERVICE_EXCEPTION;
        }
        return ABSSTATX0020SDMXX0020WEBX0020SERVICE_WSDL_LOCATION;
    }

}