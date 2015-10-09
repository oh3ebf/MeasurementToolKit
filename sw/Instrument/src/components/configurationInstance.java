/***********************************************************
 * Software: instrument client
 * Module:   configuration instance class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 16.4.2013
 *
 ***********************************************************/
package components;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.NestableException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

public class configurationInstance extends PropertiesConfiguration {

    private static Logger logger;
    private static configurationInstance config = null;
    private static boolean instantiated = false;

    public configurationInstance(String name) throws ConfigurationException {
        super(name);
    }

    /** Function returns configuration instance
     * 
     * @return configuration instance
     * 
     */
    public static configurationInstance getConfiguration() {
        return (config);
    }

    /** Function returns configuration instance
     * 
     * @return configuration instance
     * 
     */
    public static configurationInstance getConfiguration(String configName) {
        if (instantiated == false) {
            // get logger instance for this class
            logger = Logger.getLogger(configurationInstance.class);
            // Set up a simple configuration that logs on the console.
            BasicConfigurator.configure();

            try {
                // get configuration from file
                config = new configurationInstance(configName);
            } catch (NestableException ex) {
                logger.error("Configuration file not found");
            }
            // we now have instance
            instantiated = true;
        }

        return (config);
    }
   

    /** Function checks parameter existense and reads value
     * 
     * @param key to parameter
     * @param value to parameter initialization
     * @return current value as String
     * 
     */
    public String checkAndReadStringValue(String key, Object value) {
        if (config.containsKey(key)) {
            return (config.getString(key));
        } else {
            config.addProperty(key, value);
            try {
                config.save();
            } catch (Exception ex) {
                logger.error("failed to save configuration file");
            }
        }
        return (null);
    }

    /** Function checks parameter existense and reads value
     * 
     * @param key to parameter
     * @param value to parameter initialization
     * @return current value as boolean
     * 
     */
    public boolean checkAndReadBoolValue(String key, Object value) {
        if (config.containsKey(key)) {
            return (config.getBoolean(key));
        } else {
            config.addProperty(key, value);
            try {
                config.save();
            } catch (Exception ex) {
                logger.error("failed to save configuration file");
            }
        }
        return (false);
    }

    /** Function checks parameter existense and reads value
     * 
     * @param key to parameter
     * @param value to parameter initialization
     * @return current value as int
     * 
     */
    public int checkAndReadIntValue(String key, Object value) {
        if (config.containsKey(key)) {
            return (config.getInt(key));
        } else {

            config.addProperty(key, value);
            try {
                config.save();
            } catch (Exception ex) {
                logger.error("failed to save configuration file");
            }
        }
        return (0);
    }
}
