/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package knowyourcountry;

import java.util.Iterator;
import java.util.Map;
import javax.xml.namespace.NamespaceContext;

/**
 *
 * @author rob
 */
public class NsContext implements NamespaceContext
{
    private Map<String,String> ns;
    public NsContext(Map<String,String> namespaces)
    {
        this.ns = namespaces;
    }
    
    public String getNamespaceURI(String prefix)
    {
        return ns.get(prefix);
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
}
