/***********************************************************
 * Software: instrument client
 * Module:   instrument setup xml reader class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 12.11.2013
 *
 ***********************************************************/
package instruments.hp54600;

import components.ChannelKey;
import components.XmlConfigData;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;

public class HP54600XmlData {

    private int channel = 0;
    private static Logger logger;
    private Vector<Double> data;
    private Hashtable<ChannelKey, XmlConfigData> configData = null;

    public HP54600XmlData(Hashtable<ChannelKey, XmlConfigData> c) {
        // get logger instance for this class".
        logger = Logger.getLogger(HP54600XmlData.class);
        configData = c;

    /*
    Element channel = null;
    //TODO tässä pitäisi luoda wavemodel objekti johon data kasataan
    // get logger instance for this class".
    logger = Logger.getLogger(XmlWriter.class);
    // get channel number
    channel.getAttributeValue(XmlWriter.XML_ATTRIBUTE_CHANNEL);
    // get sub elements
    List channelGroups = channel.getChildren();
    Iterator channelIterator = channelGroups.iterator();
    data = new Vector<Double>();
    // iterate channel data section
    while (channelIterator.hasNext()) {
    Element chItem = (Element) channelIterator.next();
    // handle waveform data
    if (!chItem.getName().equals(XmlWriter.XML_ELEMENT_DATA)) {
    try {
    // collect data to vector
    data.add(Integer.parseInt(chItem.getAttributeValue(XmlWriter.XML_ATTRIBUTE_INDEX)),
    Double.parseDouble(chItem.getValue()));
    } catch (Exception ex) {
    logger.error("failed to parse channel waveform data" + ex.getMessage());
    }
    } else {
    // parse waveform attributes and store to hash table
    //configItems.put(chItem.getAttributeValue(XmlWriter.XML_ATTRIBUTE_NAME), chItem.getValue());
    }
    }
     * */
    }

    /** Function parse integer value from saved configuration
     * 
     * @param key to parameter
     * @param ch channel info
     * @return parameter value
     */
    public int getInt(String key, int ch) {
        try {
            return (Integer.parseInt(configData.get(new ChannelKey(key, Integer.toBinaryString(ch))).getValue()));
        } catch (Exception ex) {
            logger.error("failed to parse " + key + " ch:" + ch);
        }

        return (0);
    }

    /** Function parse double value from saved configuration
     * 
     * @param key to parameter
     * @param ch channel info
     * @return parameter value
     */
    
    public double getDouble(String key, int ch) {
        try {
            return (Double.parseDouble(configData.get(new ChannelKey(key, Integer.toBinaryString(ch))).getValue()));
        } catch (Exception ex) {
            logger.error("failed to parse " + key + " ch:" + ch);
        }

        return (0.0D);
    }
}

