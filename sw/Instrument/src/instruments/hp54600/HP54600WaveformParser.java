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

import components.WaveformParserBase;
import components.XmlWriter;
import java.util.Iterator;
import java.util.List;
import lib.gui.models.DefaultWaveModel;
import org.jdom.Element;

public class HP54600WaveformParser extends WaveformParserBase {

    private HP54600 scope;

    public HP54600WaveformParser(HP54600 s) {
        super();
        scope = s;
    }

    /** Function reads waveform from xml data
     * 
     * @param ch channel to read
     * @param wm reference to wavemodel
     */
    @Override
    public void getWaveform(int ch, DefaultWaveModel w) {
        HP54600WaveModel wm = (HP54600WaveModel) w;

        // get document nodes
        List channels = waveSet.getChildren();
        Iterator channelIterator = channels.iterator();

        // iterate devices
        while (channelIterator.hasNext()) {
            Element channel = (Element) channelIterator.next();

            try {
                // get channel number
                if (ch == Integer.parseInt(channel.getAttributeValue(XmlWriter.XML_ATTRIBUTE_CHANNEL))) {
                    // get sub elements
                    List channelGroups = channel.getChildren();
                    Iterator dataIterator = channelGroups.iterator();

                    // iterate channel data section
                    while (dataIterator.hasNext()) {
                        Element dataItem = (Element) dataIterator.next();

                        // data
                        if (dataItem.getName().equals(XmlWriter.XML_ELEMENT_DATA)) {
                            try {
                                // 
                                wm.add(Double.parseDouble(dataItem.getValue()));
                                continue;
                            } catch (Exception ex) {
                                logger.error("failed to parse y reference" + ex.getMessage());
                            }
                        }

                        // y reference
                        if (dataItem.getName().equals(XmlWriter.XML_ELEMENT_Y_REFERENCE)) {
                            try {
                                // 
                                wm.setYReference(Double.parseDouble(dataItem.getValue()));
                                continue;
                            } catch (Exception ex) {
                                logger.error("failed to parse y reference" + ex.getMessage());
                            }
                        }

                        // y origin
                        if (dataItem.getName().equals(XmlWriter.XML_ELEMENT_Y_ORIGIN)) {
                            try {
                                // 
                                wm.setYOrigin(Double.parseDouble(dataItem.getValue()));
                                continue;
                            } catch (Exception ex) {
                                logger.error("failed to parse y origin" + ex.getMessage());
                            }
                        }

                        // y increment
                        if (dataItem.getName().equals(XmlWriter.XML_ELEMENT_Y_INCREMENT)) {
                            try {
                                // 
                                wm.setYIncrement(Double.parseDouble(dataItem.getValue()));
                                continue;
                            } catch (Exception ex) {
                                logger.error("failed to parse y increment" + ex.getMessage());
                            }
                        }

                        // x reference
                        if (dataItem.getName().equals(XmlWriter.XML_ELEMENT_X_REFERENCE)) {
                            try {
                                // 
                                wm.setXReference(Double.parseDouble(dataItem.getValue()));
                                continue;
                            } catch (Exception ex) {
                                logger.error("failed to parse x reference" + ex.getMessage());
                            }
                        }

                        // x origin
                        if (dataItem.getName().equals(XmlWriter.XML_ELEMENT_X_ORIGIN)) {
                            try {
                                // 
                                wm.setXOrigin(Double.parseDouble(dataItem.getValue()));
                                continue;
                            } catch (Exception ex) {
                                logger.error("failed to parse x origin" + ex.getMessage());
                            }
                        }

                        // x increment
                        if (dataItem.getName().equals(XmlWriter.XML_ELEMENT_X_INCREMENT)) {
                            try {
                                // 
                                wm.setXIncrement(Double.parseDouble(dataItem.getValue()));
                                continue;
                            } catch (Exception ex) {
                                logger.error("failed to parse x increment" + ex.getMessage());
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                logger.error("failed to parse channel number" + ex.getMessage());
            }
        }
    }
}

