/***********************************************************
 * Software: instrument client
 * Module:   configuration object class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 17.11.2012
 *
 ***********************************************************/
package components;

import java.util.Hashtable;

public class configObject {

    private String name;
    private String type;
    private Hashtable<String, String> properties;
    private InstanceBase instance;

    public configObject() {
        properties = new Hashtable<String, String>();
        instance = null;
    }

    /** Function return device name
     * 
     * @return name
     * 
     */
    public String getName() {
        return name;
    }

    /** Function sets device name
     * 
     * @param name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /** function returns device type
     * 
     * @return device type
     */
    public String getType() {
        return type;
    }

    /** Function sets device type
     * 
     * @param type to use
     */
    public void setType(String type) {
        this.type = type;
    }

    /** Function saves property in hashtable
     * 
     * @param name of property
     * @param value of property
     * 
     */
    public void setProperty(String name, String value) {
        // insert or update values
        properties.put(name, value);
    }

    /** Function returns single property value
     * 
     * @param key to property
     * @return property value
     * 
     */
    public String getProperty(String key) {
        return (properties.get(key));
    }

    /** Function returns properties collection
     * 
     * @return reference to properties
     */
    public Hashtable<String, String> getProperties() {
        return (properties);
    }

    /** Function returns reference to device instance
     * 
     * @return
     */
    public InstanceBase getInstance() {
        return instance;
    }

    /** Function sets reference to device instance
     * 
     * @param instance
     */
    public void setInstance(InstanceBase instance) {
        this.instance = instance;
    }

    /** Function returns device instance status
     * 
     * @return true if instance initialized
     */
    public boolean validInstance() {
        if (instance != null) {
            return (true);
        }
        return (false);
    }
}
