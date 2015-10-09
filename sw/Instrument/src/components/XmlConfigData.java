/***********************************************************
 * Software: instrument client
 * Module:   instrument setup xml data object class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 18.12.2013
 *
 ***********************************************************/

package components;

import java.util.Hashtable;


public class XmlConfigData {
    private String value;
    private Hashtable<String, String> attributes;
    
    public XmlConfigData() {
        // storage for parameter attibues
        attributes = new Hashtable<String, String>();
    }

    /** Function gets parameter value
     * 
     * @return value as string
     */
    public String getValue() {
        return value;
    }

    /** Function sets parameter value
     * 
     * @param value as string
     */
    
    public void setValue(String value) {
        this.value = value;
    }
    
    /** Functions add new attribute to configuration data
     * 
     * @param key attribute name
     * @param value of attribute
     */
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }
    
    /** Function returns given attribute value
     * 
     * @param key name of attibute
     * @return value
     */
    public String getAttribute(String key) {
        return (attributes.get(key));
    }
    
    /** Function return current number off stored attributes
     * 
     * @return attribute count
     */
    
    public int getAttributeCount() {
        return(attributes.size());
    }
}
