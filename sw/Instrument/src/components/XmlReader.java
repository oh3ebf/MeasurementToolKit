/***********************************************************
 * Software: instrument client
 * Module:   instrument setup xml reader class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 11.11.2013
 *
 ***********************************************************/
package components;

import java.io.File;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class XmlReader {

    private static Logger logger;
    private Document doc = null;
    private SAXBuilder builder = null;
    private Element rootNode;
    private String deviceName;
    private String timestamp;
    private Hashtable<ChannelKey, XmlConfigData> configItems = null;
    private Hashtable<Integer, XmlConfigData> slotData = null;
    private Vector<Double> data = null;
    private Element activeElement = null;
    private WaveformParserBase waveParser = null;
    private boolean waveFound = false;

    public XmlReader() {
        // get logger instance for this class".
        logger = Logger.getLogger(XmlReader.class);

        builder = new SAXBuilder();
    }

    public XmlReader(File fileName) {
        // get logger instance for this class".
        logger = Logger.getLogger(XmlReader.class);

        builder = new SAXBuilder();
        setXmlFile(fileName);
    }

    /* Function sets xml file name
     *
     * Parameters:
     * name: file name to parse
     *
     * Returns:
     *
     */
    public void setXmlFile(File fileName) {

        try {
            // start document handling
            doc = (Document) builder.build(fileName);
            rootNode = doc.getRootElement();
        } catch (Exception e) {
            logger.error("failed to set new xml file: " + fileName);
        }
    }

    /** Function checks if given name exist in device configuration
     * 
     * @param name of device
     * @return true if found
     */
    public boolean contains(String name) {
        // check valid root node
        if (rootNode == null) {
            logger.error("no valid root node found");
            return (false);
        }

        // get document nodes
        List mainGroups = rootNode.getChildren();

        Iterator mainIterator = mainGroups.iterator();

        // iterate document
        while (mainIterator.hasNext()) {
            Element device = (Element) mainIterator.next();
            if (device.getName().equals(XmlWriter.XML_ELEMENT_DEVICE)) {

                // check if device exists
                if (device.getAttributeValue(XmlWriter.XML_ATTRIBUTE_NAME).equals(name)) {
                    activeElement = device;
                    return (true);
                }
            }
        }

        return (false);
    }

    /** Function returns parsed device name
     * 
     * @return name
     */
    public String getDevice() {
        return (deviceName);
    }

    /** Function returns file creation timestamp
     * 
     * @return sql timestamp as string
     */
    public String getTimeStamp() {
        return (timestamp);
    }

    /** Function return configuation data found for device
     * 
     * @return objacts as key value pairs
     */
    public Hashtable<ChannelKey, XmlConfigData> getConfigData() {
        return (configItems);
    }

    /** Function parse integer value from saved configuration
     * 
     * @param key to parameter
     * @return parameter value
     */
    public int getInteger(String key) {
        try {
            return (Integer.parseInt(configItems.get(new ChannelKey(key, "")).getValue()));
        } catch (Exception ex) {
            logger.error("falied to parse " + key + " " + ex.getMessage());
        }

        return (0);
    }

    /** Function parse double value from saved configuration
     * 
     * @param key to parameter
     * @return parameter value
     */
    public double getDouble(String key) {
        try {
            return (Double.parseDouble(configItems.get(new ChannelKey(key, "")).getValue()));
        } catch (Exception ex) {
            logger.error("falied to parse " + key + " " + ex.getMessage());
        }

        return (0.0D);
    }
    
    /** Function parse float value from saved configuration
     * 
     * @param key to parameter
     * @return parameter value
     */
    public float getFloat(String key) {
        try {
            return (Float.parseFloat(configItems.get(new ChannelKey(key, "")).getValue()));
        } catch (Exception ex) {
            logger.error("falied to parse " + key + " " + ex.getMessage());
        }

        return (0.0F);
    }

    /** Function parse boolean value from saved configuration
     * 
     * @param key to parameter
     * @return parameter value
     */
    public boolean getBoolean(String key) {
        try {
            return (Boolean.parseBoolean(configItems.get(new ChannelKey(key, "")).getValue()));
        } catch (Exception ex) {
            logger.error("falied to parse " + key + " " + ex.getMessage());
        }

        return (false);
    }
    
    /** Function parse integer value from saved configuration
     * 
     * @param key to parameter
     * @param ch channel info
     * @return parameter value
     */
    public int getInteger(String key, int ch) {
        try {
            return (Integer.parseInt(configItems.get(new ChannelKey(key, Integer.toBinaryString(ch))).getValue()));
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
            return (Double.parseDouble(configItems.get(new ChannelKey(key, Integer.toBinaryString(ch))).getValue()));
        } catch (Exception ex) {
            logger.error("failed to parse " + key + " ch:" + ch);
        }

        return (0.0D);
    }
    
    /** Function parse float value from saved configuration
     * 
     * @param key to parameter
     * @param ch channel info
     * @return parameter value
     */
    public float getFloat(String key, int ch) {
        try {
            return (Float.parseFloat(configItems.get(new ChannelKey(key, Integer.toBinaryString(ch))).getValue()));
        } catch (Exception ex) {
            logger.error("failed to parse " + key + " ch:" + ch);
        }

        return (0.0F);
    }

    /** Function parse boolean value from saved configuration
     * 
     * @param key to parameter
     * @param ch channel info
     * @return parameter value
     */
    public boolean getBoolean(String key, int ch) {
        try {
            return (Boolean.parseBoolean(configItems.get(new ChannelKey(key, Integer.toBinaryString(ch))).getValue()));
        } catch (Exception ex) {
            logger.error("failed to parse " + key + " ch:" + ch);
        }

        return (false);
    }
    
    /** Function returns slot type
     * 
     * @param slot number
     * @return slot type as number
     */
    public int getSlotType(int slot) {
        int slotType = 9; // default not installed

        try {
            slotType = Integer.parseInt(slotData.get(slot).getValue());
        } catch (NumberFormatException ex) {
            logger.error("failed to parse slot type" + slot);
        }

        return (slotType);
    }

    /** Function returns relay states in given slot
     * 
     * @param slot number
     * @return relay states as boolean array
     */
    
    public boolean[] getSlotRelays(int slot) {
        XmlConfigData c = slotData.get(slot);
        boolean[] rl = new boolean[c.getAttributeCount()];
       
        try {
            // parse states from text presentation
            for (int i = 0; i < c.getAttributeCount(); i++) {
                rl[i] = Boolean.parseBoolean(c.getAttribute(Integer.toString(i)));
            }
        } catch (Exception ex) {
            logger.error("failed to parse relay states:" + slot);
        }
        
        return (rl);
    }

    /** Function sets waveform parsing callback
     * 
     * @param p reference to parsing callback class
     */
    public void setWaveformParser(WaveformParserBase p) {
        waveParser = p;
    }

    /** Function return status of waveform section found
     * 
     * @return true if waveform section exist
     */
    public boolean isWaveformFound() {
        return (waveFound);
    }

    /** Function parse configuration data for given device
     * 
     * @param name of device
     * @return parsing status
     */
    public boolean parse(String name) {
        int i = 0;
        String channel = "";
        waveFound = false;

        // get storage for config data
        configItems = new Hashtable<ChannelKey, XmlConfigData>();
        slotData = new Hashtable<Integer, XmlConfigData>();

        // check valid root node
        if (rootNode == null) {
            logger.error("no valid root node found");
            return (false);
        }

        // get document nodes
        List deviceGroups = activeElement.getChildren();
        Iterator deviceIterator = deviceGroups.iterator();

        // iterate devices
        while (deviceIterator.hasNext()) {
            Element e = (Element) deviceIterator.next();

            // extract configuration 
            if (e.getName().equals(XmlWriter.XML_ELEMENT_CONFIG)) {
                // get sub elements
                List configGroups = e.getChildren();
                Iterator configIterator = configGroups.iterator();

                // iterate parameters section
                while (configIterator.hasNext()) {
                    Element param = (Element) configIterator.next();
                    // store key, value and attributes to config object
                    XmlConfigData d = new XmlConfigData();
                    d.setValue(param.getValue());

                    // parse attributes
                    List attr = param.getAttributes();
                    // check if other than name attribute
                    if (attr.size() > 1) {
                        Iterator attrIterator = attr.iterator();

                        // loop attributes
                        while (attrIterator.hasNext()) {
                            Attribute a = (Attribute) attrIterator.next();

                            // handle channel attribute
                            if (a.getName().equals(XmlWriter.XML_ATTRIBUTE_CHANNEL)) {
                                channel = a.getValue();
                            }

                            // handle all other attributes
                            if (!a.getName().equals(XmlWriter.XML_ATTRIBUTE_NAME) &&
                                    !a.getName().equals(XmlWriter.XML_ATTRIBUTE_CHANNEL)) {
                                d.addAttribute(a.getName(), a.getValue());
                            }
                        }
                    } else {
                        // no channel attribute found, use empty
                        channel = "";
                    }

                    // save attributes to config data
                    configItems.put(new ChannelKey(param.getAttributeValue(XmlWriter.XML_ATTRIBUTE_NAME), channel), d);
                }
            }

            // handle waveform section
            if (e.getName().equals(XmlWriter.XML_ELEMENT_WAVEFORM)) {
                // handle waveform section
                if (waveParser != null) {
                    waveFound = true;
                    waveParser.setWaveData(e);
                }
            }

            // handle slot section
            if (e.getName().equals(XmlWriter.XML_ELEMENT_SLOT)) {
                XmlConfigData cardData = new XmlConfigData();

                // slot number
                int slot = Integer.parseInt(e.getAttributeValue(XmlWriter.XML_ATTRIBUTE_NAME));

                // set card type
                cardData.setValue(e.getAttributeValue(XmlWriter.XML_ATTRIBUTE_TYPE));

                // get sub elements
                List relayGroups = e.getChildren();
                Iterator relayIterator = relayGroups.iterator();

                // iterate slot section
                while (relayIterator.hasNext()) {
                    // get relay index and state
                    Element relay = (Element) relayIterator.next();
                    cardData.addAttribute(relay.getAttributeValue(XmlWriter.XML_ATTRIBUTE_INDEX), relay.getText());
                }

                slotData.put(slot, cardData);
            }
        }
        return (true);
    }
}
