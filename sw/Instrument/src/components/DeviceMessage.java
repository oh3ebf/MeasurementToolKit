/***********************************************************
 * Software: instrument client
 * Module:   Device messaging class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.9.2012
 *
 ***********************************************************/
package components;

import interfaces.MessageCallbackInterface;
import interfaces.MessageInterface;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;
import javax.swing.JComboBox;
import networking.netCommand;
import yami.ParamSet;
import org.apache.log4j.Logger;

public class DeviceMessage {
    private MessageInterface msgIf;
    private static Logger logger;
    private String MessageName;
    private ParamSet PsetResp;
    private String id;
    private Vector<Object> resultSet;
    private boolean deviceFound = true;
    private boolean enabled = true;
    public static final int MeasurementCmdCapture = 1;
    public static final int MeasurementCmdGenerate = 2;
    public static final int MeasurementCmdSweep = 3;
    public static final String MessageMeasurementStart = "MeasurementStart";
    public static final String MessageMeasurementStop = "MeasurementStop";
    public static final String MessageMeasurementState = "MeasurementState";
    public static final String MessageMeasurementUpdate = "MeasurementUpdate";
    public static final String MessageCheckOnLine = "CheckOnLine";
    public static final String StreamDataOk = "DataOk";
    public static final String MessageReset = "Reset";
    public static final String MessageTest = "Test";

    public DeviceMessage(String name, MessageInterface msg) {
        // get logger instance for this class".
        logger = Logger.getLogger(DeviceMessage.class);

        // save messaging context
        msgIf = msg;
        MessageName = name;
        
        // get device online status
        deviceFound = sendString(MessageCheckOnLine);
    }

    /** Function adds message callback listener
     *
     * @param name hashtable key
     * @param msgIf callback function
     *
     * @return true if ok
     *
     */
    public boolean addMessageCallback(String name, MessageCallbackInterface msg) {
        if (msgIf.addMessageCallback(name, msg)) {
            return (true);
        } else {
            return (false);
        }
    }

    /** Function returns device name used in messages
     * 
     * @return name
     */
    
    public String getName() {
        return(MessageName);
    }
    
    /** Function sends message
     *
     * @param param message parameters
     *
     * @return true/false
     *
     */
    public boolean sendString(String msg) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(msg);
        
        return(commandMessage(PsetMsg));
    }

    /** Function send command message based on combobox selection
     * 
     * @param msg command string
     * @param box combobox selected
     * 
     */
    public boolean sendComboBoxSelection(String cmd, JComboBox box) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(box.getSelectedIndex());
        
        return(commandMessage(PsetMsg));
    }

    /** Function send command message based on combobox selection
     * 
     * @param msg command string
     * @param ch channel
     * @param box combobox selected
     * 
     */
    public boolean sendComboBoxSelection(String cmd, int ch, JComboBox box) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(ch);
        PsetMsg.append(box.getSelectedIndex());
        
        return(commandMessage(PsetMsg));
    }
    
    /** Function sends command and string value
     * 
     * @param cmd command
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, boolean value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);

        if (value) {
            PsetMsg.append(1);
        } else {
            PsetMsg.append(0);
        }
        
        return(commandMessage(PsetMsg));
    }

    /** Function sends command and string value
     * 
     * @param cmd command
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, String value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(value);
        return(commandMessage(PsetMsg));
    }

    /** Function sends command and byte value
     * 
     * @param cmd command
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, byte value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(value);
        
        return(commandMessage(PsetMsg));
    }

    /** Function sends command and integer value
     * 
     * @param cmd command
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, int value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(value);
        
        return(commandMessage(PsetMsg));
    }

    /** Function sends command and double value
     * 
     * @param cmd command
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, double value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(value);
        
        return(commandMessage(PsetMsg));
    }

    /** Function sends command chnanel and string value
     * 
     * @param cmd command
     * @param ch channel number
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, String ch, String value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(ch);
        PsetMsg.append(value);
        
        return(commandMessage(PsetMsg));
    }

    /** Function sends command chnanel and int value
     * 
     * @param cmd command
     * @param ch channel number
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, int ch, int value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(ch);
        PsetMsg.append(value);
        
        return(commandMessage(PsetMsg));
    }

    /** Function sends command channel and double value
     * 
     * @param cmd command
     * @param ch channel number
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, int ch, double value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(ch);
        PsetMsg.append(value);
        
        return(commandMessage(PsetMsg));
    }
    
    /** Function sends command channel and boolean value
     * 
     * @param cmd command
     * @param ch channel number
     * @param value to send
     * 
     */
    public boolean sendValue(String cmd, int ch, boolean value) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(ch);
        if (value) {
            PsetMsg.append(1);
        } else {
            PsetMsg.append(0);
        }
        
        return(commandMessage(PsetMsg));
    }

    /** Function writes command to given channel and reads booelan response
     * 
     * @param cmd command
     * @param index where boolean value is found in response set
     * @param ch channel to write command
     * @return
     */
    public boolean readBoolValue(String cmd, int index, int ch) {
        ParamSet PsetMsg = new ParamSet();

        // add message parameters
        PsetMsg.append(cmd);
        PsetMsg.append(ch);

        // read response
        if (commandMessage(PsetMsg)) {
            if (resultSet.elementAt(index) instanceof Byte) {
                if ((Byte) resultSet.elementAt(index) == 1) {
                    return (true);
                } else {
                    return (false);
                }
            }
        }
        return (false);
    }

    /*** Functions read boolean value from result set
     * 
     * @param index to read
     * @return boolean value
     */
    public boolean readBooleanValue(int index) {
        // case of byte
        if (resultSet.elementAt(index) instanceof Byte) {
            // check received value
            if ((Byte) resultSet.elementAt(index) == 1) {
                return (true);
            } else {
                return(false);
            }
        }
        
        // case of integer
        if (resultSet.elementAt(index) instanceof Integer) {
            // check received value
            if ((Integer) resultSet.elementAt(index) == 1) {
                return (true);
            } else {
                return(false);
            }
        }
        
        // not valid boolean message
        logger.warn("received boolean value sent as" + resultSet.elementAt(index).getClass().getName());
        return (false);
    }

        /*** Functions read int value from result set
     * 
     * @param index to read
     * @return int value
     */
    public byte readByteValue(int index) {
        if (resultSet.elementAt(index) instanceof Byte) {
            return ((Byte) resultSet.elementAt(index));
        }

        return (0);
    }
    
    /*** Functions read int value from result set
     * 
     * @param index to read
     * @return int value
     */
    public int readIntValue(int index) {
        if (resultSet.elementAt(index) instanceof Integer) {
            return ((Integer) resultSet.elementAt(index));
        }

        return (0);
    }

    /*** Functions read float value from result set
     * 
     * @param index to read
     * @return float value
     */
    public float readFloatValue(int index) {
        if (resultSet.elementAt(index) instanceof Float) {
            return ((Float) resultSet.elementAt(index));
        }

        return (0.0F);
    }

    /*** Functions read double value from result set
     * 
     * @param index to read
     * @return double value
     */
    public double readDoubleValue(int index) {
        if (resultSet.elementAt(index) instanceof Double) {
            return ((Double) resultSet.elementAt(index));
        }

        return (0.0D);
    }

    /** function converts float value to N3 format
     * 
     * @param value to send
     * 
     */
    public String formatN3Value(float value) {
        try {
            // conver to scientific format
            NumberFormat formatter = new DecimalFormat("0.######E0");
            return (formatter.format(value));

        } catch (NumberFormatException ex) {
            logger.error("failed to parse " + value + "to N3 format");
        }

        return ("0.0E0");
    }

    /** Function returns unique id related to message
     * 
     * @return id
     */
    public String getUniqueId() {
        return (id);
    }

    /** Functions sends command and parameters
     * 
     * @param cmd command
     * 
     */
    public boolean commandMessage(ParamSet attr) {
        return (commandMessage(null, attr));
    }

    /** Functions sends command and parameters
     * 
     * @params name of target server
     * @param cmd command
     * 
     */
    public boolean commandMessage(String name, ParamSet attr) {
        //ParamSet PsetResp;
        String Msg;

        // if no connection yet
        if (msgIf == null) {
            logger.error("No message client available");
            return (false);
        }

        if(!deviceFound) {
            logger.error(MessageName + ": No online device found");
            return(false);
        }
      
        if (name == null) {
            name = MessageName;
        }

        if (attr == null) {
            logger.error("Message parameters set missing");
            return (false);
        }

        try {
            // send message
            PsetResp = msgIf.MessageSend(name, attr);
            // get response
            Msg = PsetResp.extractString();
            // check command status
            if (Msg.equals(netCommand.MessageCommandRespOk)) {
                logger.debug("target " + name + " command ok");
                if (PsetResp.getNumberOfEntries() > 1) {
                    // get unique id from message
                    id = PsetResp.extractString();

                    // create new result storage
                    resultSet = new Vector<Object>();

                    // parse result data from message by type
                    for (int i = 2; i < PsetResp.getNumberOfEntries(); i++) {
                        switch (PsetResp.getType()) {
                            case Binary:
                                resultSet.add(i - 2, PsetResp.extractBinary());
                                break;
                            case Byte:
                                resultSet.add(i - 2, PsetResp.extractByte());
                                break;
                            case Double:
                                resultSet.add(i - 2, PsetResp.extractDouble());
                                break;
                            case Integer:
                                resultSet.add(i - 2, PsetResp.extractInt());
                                break;
                            case String:
                                resultSet.add(i - 2, PsetResp.extractString());
                                break;
                            case WString:
                                resultSet.add(i - 2, PsetResp.extractWString());
                                break;
                        }
                    }
                }
                return (true);
            }
        } catch (Exception ex) {
            logger.warn("target command failed exception thrown");
        }

        return (false);
    }
}
