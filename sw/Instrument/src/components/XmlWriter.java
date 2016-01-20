/***********************************************************
 * Software: instrument client
 * Module:   instrument setup xml writer class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 24.11.2013
 *
 ***********************************************************/
package components;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import oh3ebf.lib.common.utilities.TimeStamp;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class XmlWriter {

    public final static String XML_ELEMENT_INSTRUMENT = "instrument";
    public final static String XML_ELEMENT_DEVICE = "device";
    public final static String XML_ELEMENT_CONFIG = "configuration";
    public final static String XML_ELEMENT_WAVEFORM = "waveform";
    public final static String XML_ELEMENT_CHANNEL = "channel";
    public final static String XML_ELEMENT_PARAMETER = "parameter";
    public final static String XML_ELEMENT_TIMEDATE = "timestamp";
    public final static String XML_ELEMENT_X_INCREMENT = "x-increment";
    public final static String XML_ELEMENT_X_REFERENCE = "x-reference";
    public final static String XML_ELEMENT_X_ORIGIN = "y-origin";
    public final static String XML_ELEMENT_Y_INCREMENT = "y-increment";
    public final static String XML_ELEMENT_Y_REFERENCE = "y-reference";
    public final static String XML_ELEMENT_Y_ORIGIN = "x-origin";
    public final static String XML_ELEMENT_DATA = "data";
    public final static String XML_ELEMENT_SLOT = "slot";
    public final static String XML_ELEMENT_RELAY = "relay";
    public final static String XML_ATTRIBUTE_NAME = "name";
    public final static String XML_ATTRIBUTE_CHANNEL = "ch";
    public final static String XML_ATTRIBUTE_INDEX = "n";
    public final static String XML_ATTRIBUTE_TYPE = "type";
    public final static String XML_ATTRIBUTE_CH_COUNT = "channel_count";
    private static Logger logger;
    private Document doc;
    private Element activeElement = null;

    public XmlWriter() {
        // get logger instance for this class".
        logger = Logger.getLogger(XmlWriter.class);

        // create document
        Element instrument = new Element(XML_ELEMENT_INSTRUMENT);
        doc = new Document(instrument);
        doc.setRootElement(instrument);

        // add creation date
        doc.getRootElement().addContent(
                new Element(XML_ELEMENT_TIMEDATE).setText(TimeStamp.getDateTimeStamp()));
    }

    /** Function adds configuration to xml document
     * 
     * @param device name
     * @param type of device
     * @param ch_cnt channel count
     */
    public void addDeviceSection(String device, String type, String ch_cnt) {
        Element d = new Element(XML_ELEMENT_DEVICE);

        // set attributes
        d.setAttribute(XML_ATTRIBUTE_NAME, device);
        d.setAttribute(XML_ATTRIBUTE_TYPE, type);

        if (ch_cnt != null) {
            d.setAttribute(XML_ATTRIBUTE_CH_COUNT, ch_cnt);
        }

        doc.getRootElement().addContent(d);
    }

    /** Function adds configuration to xml document
     * 
     * @param device to add config section
     */
    public void addConficSection(String device) {
        Element root = doc.getRootElement();

        // get document nodes
        List deviceGroups = root.getChildren();
        Iterator deviceIterator = deviceGroups.iterator();

        // iterate devices
        while (deviceIterator.hasNext()) {
            Element dev = (Element) deviceIterator.next();

            if (dev.getName().equals(XmlWriter.XML_ELEMENT_DEVICE)) {
                // extract configuration 
                if (dev.getAttributeValue(XML_ATTRIBUTE_NAME).equals(device)) {
                    activeElement = new Element(XML_ELEMENT_CONFIG);
                    dev.addContent(activeElement);
                }
            }
        }
    }

    /** Function adds waveform to xml document
     * 
     * @param device to add waveform section
     */
    public void addWaveformSection(String device) {
        Element root = doc.getRootElement();

        // get document nodes
        List deviceGroups = root.getChildren();
        Iterator deviceIterator = deviceGroups.iterator();

        // iterate devices
        while (deviceIterator.hasNext()) {
            Element dev = (Element) deviceIterator.next();

            if (dev.getName().equals(XmlWriter.XML_ELEMENT_DEVICE)) {
                // extract configuration 
                if (dev.getAttributeValue(XML_ATTRIBUTE_NAME).equals(device)) {
                    activeElement = new Element(XML_ELEMENT_WAVEFORM);
                    dev.addContent(activeElement);
                }
            }
        }
    }

    /** Function adds channel to xml document
     * 
     */
    public void addChannelSection(int ch) {

        // add channel
        Element chan = new Element(XML_ELEMENT_CHANNEL);
        chan.setAttribute(XML_ATTRIBUTE_CHANNEL, Integer.toString(ch));
        activeElement.addContent(chan);
    }

    /** Function adds new slot section
     * 
     * @param device to put slot section
     * @param name of slot insertion card
     * @param type of slot insertion card
     */
    public void addSlotSection(String device, String name, String type) {
        Element root = doc.getRootElement();

        // get document nodes
        List deviceGroups = root.getChildren();
        Iterator deviceIterator = deviceGroups.iterator();

        // iterate devices
        while (deviceIterator.hasNext()) {
            Element dev = (Element) deviceIterator.next();

            if (dev.getName().equals(XmlWriter.XML_ELEMENT_DEVICE)) {
                // extract configuration 
                if (dev.getAttributeValue(XML_ATTRIBUTE_NAME).equals(device)) {
                    activeElement = new Element(XML_ELEMENT_SLOT);
                    activeElement.setAttribute(XML_ATTRIBUTE_NAME, name);
                    activeElement.setAttribute(XML_ATTRIBUTE_TYPE, type);
                    dev.addContent(activeElement);
                }
            }
        }
    }
    
    /** Function adds new relay section
     * 
     * @param n number of relay
     * @param state of relay
     */
    
    public void addRelaySection(int n, boolean state) {
        // add new relay to activeelement
        Element rl = new Element(XML_ELEMENT_RELAY);
        // set relay number
        rl.setAttribute(XML_ATTRIBUTE_INDEX,Integer.toString(n));
        // set relay state
        rl.setText(Boolean.toString(state));
        activeElement.addContent(rl);
    }
    
    /** Function adds new parameter to configuration set
     * 
     * @param name of parameter
     * @param channel info if needed
     * @param value of parameter
     * @return true if ok
     */
    public boolean addParamElement(String name, String channel, String value) {

        // check current element
        if (activeElement == null) {
            logger.warn("config section not found");
            return (false);
        }

        // add new parameter to configuation
        Element param = new Element(XML_ELEMENT_PARAMETER);
        param.setAttribute(XML_ATTRIBUTE_NAME, name);

        // any channel info available
        if ((channel != null) && (!channel.isEmpty())) {
            param.setAttribute(XML_ATTRIBUTE_CHANNEL, channel);
        }
        // add parameter value
        param.setText(value);

        // add to config
        activeElement.addContent(param);

        return (true);
    }

    /** Function adds wavform data to document
     * 
     * @param ch channel captures
     * @param data waveform samples
     * @return true if ok
     */
    public boolean addWaveform(int ch, Vector<Double> data,
            double xInc, double xRef, double xOrg, double yInc, double yRef, double yOrg) {

        Element channel = new Element(XML_ELEMENT_CHANNEL);
        channel.setAttribute(XML_ATTRIBUTE_CHANNEL, Integer.toString(ch));

        // waveform parameters from scope
        channel.addContent(new Element(XML_ELEMENT_X_INCREMENT).setText(Double.toString(xInc)));
        channel.addContent(new Element(XML_ELEMENT_X_REFERENCE).setText(Double.toString(xRef)));
        channel.addContent(new Element(XML_ELEMENT_X_ORIGIN).setText(Double.toString(xOrg)));
        channel.addContent(new Element(XML_ELEMENT_Y_INCREMENT).setText(Double.toString(yInc)));
        channel.addContent(new Element(XML_ELEMENT_Y_INCREMENT).setText(Double.toString(yRef)));
        channel.addContent(new Element(XML_ELEMENT_Y_INCREMENT).setText(Double.toString(yOrg)));

        // add data to document
        for (int i = 0; i < data.size(); i++) {
            // add new parameter to configuation
            Element item = new Element(XML_ELEMENT_DATA);
            item.setAttribute(XML_ATTRIBUTE_INDEX, Integer.toString(i));
            item.setText(Double.toString(data.elementAt(i)));
            // add to config
            channel.addContent(item);
        }

        activeElement.addContent(channel);
        return (true);
    }

    /** Function saves xml file
     * 
     * @param file to save
     * @return operation status
     */
    public boolean XmlSave(String file) {
        XMLOutputter xmlOutput = new XMLOutputter();

        // display nice nice
        xmlOutput.setFormat(Format.getPrettyFormat());

        try {
            xmlOutput.output(doc, new FileWriter(file));
            return (true);

        } catch (IOException ex) {
            logger.error("failed to write file: " + file);
        }

        return (false);
    }
}
