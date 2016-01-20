/***********************************************************
 * Software: instrument client
 * Module:   network messaging class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 25.11.2008
 *
 ***********************************************************/

package networking;

import org.apache.log4j.Logger;
import yami.*;
import instruments.LA40StateEnum;
import components.configObject;
import interfaces.MessageCallbackInterface;
import interfaces.MessageInterface;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;
import oh3ebf.lib.common.exceptions.genericException;

public class netCommand implements Callback, MessageInterface {
    
    // general commands
    //private byte[] CloseCon = {0x00};
    
    // logic analyser commands
    private byte[] LA40Clk = {0x01,0x03,0x00};
    private byte LA40TrigInt = 0x01;
    private byte LA40TrigExt = 0x02;
    private byte[] LA40CheckStatus = {0x01,0x07};
    private byte[] LA40ReadData = {0x00,0x00,0x00};
    private byte LA40Stop = 0x00;
    private byte LA40Armed = 0x01;
    
    // counter card test sets
    private byte[] LA40TestResetEnable = {0x00,0x00};
    private byte[] LA40TestResetDisable = {0x00,0x01};
    private byte[] LA40TestClrEnable = {0x01,0x01};
    private byte[] LA40TestClrDisable = {0x01,0x00};
    private byte[] LA40TestStepEnable = {0x02,0x00};
    private byte[] LA40TestStepDisable = {0x02,0x01};
    private byte[] LA40TestD8Disable = {0x03,0x00};
    private byte[] LA40TestD8Enable = {0x03,0x01};
    private byte[] LA40TestTrigClrDisable = {0x04,0x00};
    private byte[] LA40TestTrigClrEnable = {0x04,0x01};
    private byte[] LA40TestMem32k = {0x05,0x00};
    private byte[] LA40TestMem64k = {0x05,0x01};
    private byte[] LA40TestDivDisable = {0x06,0x01};
    private byte[] LA40TestDivEnable = {0x06,0x00};
    private byte[] LA40TestStepLineEnable = {0x07,0x01};
    private byte[] LA40TestStepLineDisable = {0x07,0x00};
    private byte[] LA40TestClk = {0x08,0x00};
    private byte[] LA40TestTrigInternal = {0x09,0x00};
    private byte[] LA40TestTrigExternal = {0x09,0x01};
    private byte[] LA40TestU11 = {0x0a,0x00};
    private byte[] LA40TestU12 = {0x0b,0x00};
    private byte[] LA40TestStatus = {0x0c,0x00};
    
    // ram card test sets
    private byte[] LA40TestRamOEEnable = {0x00,0x00,0x00};
    private byte[] LA40TestRamOEDisable = {0x00,0x01,0x00};
    private byte[] LA40TestRamInputEnable = {0x01,0x00,0x00};
    private byte[] LA40TestRamInputDisable = {0x01,0x01,0x00};
    private byte[] LA40TestRamOEExtEnable = {0x02,0x00,0x00};
    private byte[] LA40TestRamOEExtDisable = {0x02,0x01,0x00};
    private byte[] LA40TestRamInputExtEnable = {0x03,0x00,0x00};
    private byte[] LA40TestRamInputExtDisable = {0x03,0x01,0x00};
    private byte[] LA40TestRamInput = {0x04,0x00,0x00};
    private byte[] LA40TestRamInputExt = {0x05,0x00,0x00};
    
    
    // server targets
    
    
    // server command messages
    private String MessageConnect = "Connect";
    private String MessageDisconnect = "Disconnect";
    private String MessageExit = "Shutdown";
    private String MessageServerConfigGet = "ServerConfigGet";
    
    
    private String MessageStatusGet = "StatusGet";
    private String MessageStatusSet = "StatusSet";
    private String MessageTriggerSet = "TriggerSet";
    private String MessageSampleRateSet = "SampleRateSet";
    private String MessageCounterTest = "CounterTest";
    private String MessageRamCardTest = "RamCardTest";
    private String MessageRamDataRead = "RamDataRead";
    
    // response messages
    private static final String MessageConnectResp = "OKconnect";
    private static final String MessageDisconnectResp = "OKdisconnect";
    private static final String MessageExitResp = "OKexit";
    
    private String MessageStoppedResp ="OKStoppedLA40";
    private String MessageArmedResp ="OKArmedLA40";
    private String MessageTriggeredResp ="OKTriggeredLA40";
    private static final String MessageTriggerResp ="OKTriggerLA40";
    private static final String MessageSampleRateResp ="OKClkLA40";
    private static final String MessageCounterTestResp ="OKTestLA40";
    
    public static final String MessageCommandRespOk ="CommandOK";
    public static final String MessageCommandRespFail ="CommandFail";
    
    private Server yServerCon;
    private String serverAddr;
    private String ServerInstrument;
    private int serverPort;
    
    private Client yClientCon;
    private String clientName;
    private String clientObj;
    private String clientAddr;
    private int clientPort;
    
    private static Logger logger;
    private boolean connectStatus = false;
    private Hashtable<String, MessageCallbackInterface>callbacks;
    
    /** Creates a new instance of netCommand */
    public netCommand(String srvName, String srvAddress, int srvSocket, 
            String cliName, String cliObj, int cliPort) {
        
        // get logger instance for this class
        logger = Logger.getLogger(netCommand.class);
        callbacks = new Hashtable<String, MessageCallbackInterface>();
        
        // set server parameters
        ServerInstrument = srvName;
        serverAddr = srvAddress;
        serverPort = srvSocket;
        clientName = cliName;
        clientObj = cliObj;
        clientPort = cliPort;
        
        try {
            clientAddr = InetAddress.getLocalHost().getHostAddress();
        } catch(UnknownHostException ex) {
            logger.warn("Host ip address not found!");
        }
    }
    
    // mihin tätä käytetään????
    public void SetConnection(netConnect n) {
        //ncon = n;
    }
    
    /** Function creates yami client
     *
     * @return true when connection created, otherwise false
     *
     */
    
    public boolean Open() {
        // open connection if not opened
        if(yClientCon == null) {
            try {
                yClientCon = new Client(serverAddr, serverPort);
                return(true);
            } catch (Exception e) {
                logger.error("Failed to connect server:" + serverAddr + ":" + serverPort);
                yClientCon = null;
            }
        }
        
        return(false);
    }
    
    /** Function creates yami client
     *
     * @param NetAddress server ip address
     * @param NetSocket socket number to connect
     *
     * @return true when connection created, otherwise false
     *
     */
    
    public boolean Open(String srvName, String srvAddress, int srvSocket, String cliName, int cliPort) {
        // set server parameters
        ServerInstrument = srvName;
        serverAddr = srvAddress;
        serverPort = srvSocket;
        clientName = cliName;
        clientPort = cliPort;
        
        // return status after opening client
        return(Open());
    }
    
    /** Function disconnects from server
     *
     * @return true when disconnect ok, otherwise false
     *
     */
    
    public boolean Connect() {
        ParamSet PsetMsg, PsetResp;
        String Msg;
        
        // if no connection yet
        if(yClientCon == null) {
            // try to open one
            if(!Open()) {
                logger.error("No message client available");
                return(false);
            }
        }
        // add client data stream server parameters
        PsetMsg = new ParamSet();
        PsetMsg.append(clientName);
        PsetMsg.append(clientObj);
        PsetMsg.append(clientAddr);
        PsetMsg.append(clientPort);
        
        try {
            // send connect message
            PsetResp = yClientCon.send(ServerInstrument, MessageConnect, PsetMsg);
            // get response
            Msg = PsetResp.extractString();
            //
            if(Msg.compareTo(MessageConnectResp) == 0) {
                logger.info("Connected to server: " + serverAddr + ":" + serverPort);
                connectStatus = true;
                startServer(clientPort);
                return(true);
            }
        } catch (Exception e) {
            logger.error("Connection to server failed");
            return(false);
        }
        
        return(true);
    }
    
    /** Function makes connection to server
     *
     * @return true when connection created, otherwise false
     *
     */
    
    public boolean Disconnect() {
        ParamSet PsetResp;
        String Msg;
        
        if(yClientCon != null) {
            try {
                // send connect message
                PsetResp = yClientCon.send(ServerInstrument, MessageDisconnect);
                // get response
                Msg = PsetResp.extractString();
                //
                if(Msg.compareTo(MessageDisconnectResp) == 0) {
                    connectStatus = false;
                    // close data strean server
                    yServerCon.close();
                    yServerCon = null;
                    logger.info("Disconnected to server: " + serverAddr + ":" + serverPort);
                    return(true);
                }
            } catch (Exception e) {
                logger.error("Connection to server failed");
                return(false);
            }
        } else {
            logger.error("No message client available");
            return(false);
        }
        return(true);
    }
    
    /* Function send shutdown request to server
     *
     * @return connection close success true, otherwise false
     *
     */
    
    public boolean Shutdown() {
        ParamSet PsetResp;
        String Msg;
        
        try {
            // send shutdown message
            PsetResp = yClientCon.send(ServerInstrument, MessageExit);
            // get response
            Msg = PsetResp.extractString();
            // check shutdown status
            if(Msg.compareTo(MessageExitResp) == 0)
                logger.info("Connection to server closed");
        } catch (Exception e) {
            logger.error("Connection closing failed");
            return(false);
        }
        
        // close server connection
        if(yClientCon != null) {
            yClientCon.close();
        }
        
        return(true);
    }
    
    /** Function returns server configuration
     *
     * @return 
     *
     */
    
    public Hashtable<String, configObject> getServerConfig() {
        ParamSet PsetResp;
        String msg, key, propertyName, property;
        Hashtable<String, configObject> dev = new Hashtable<String, configObject>();
        int count = 0;
        configObject configObj;

        try {
            // send server configuration get message
            PsetResp = yClientCon.send(ServerInstrument, MessageServerConfigGet);
            count = PsetResp.getNumberOfEntries();
                                   
            // parse all data in message
            for(int i = 0; i < count; i++) {
            // get response
            msg = PsetResp.extractString();
            
            // get message target device
            key = msg.substring(0, msg.indexOf(':'));
            
            if(dev.containsKey(key)) {
                // has all ready config
                configObj = dev.get(key);                
            } else {
                // not yet, create one
                configObj = new configObject();
                // set device manufacturer type and save object
                configObj.setName(key);
                dev.put(key, configObj);                                
            }
            
            // get property from message
            propertyName = msg.substring(msg.indexOf(':') + 1, msg.lastIndexOf(':'));
            property = msg.substring(msg.lastIndexOf(':') + 1, msg.length());
            
            if(propertyName.equals("name")) {
                // set object name
                configObj.setType(property);
            } else {
                // set device property
                configObj.setProperty(propertyName, property);
            }
            
            logger.info("Property name: " + propertyName + " value: " + property);
            }
        } catch (Exception e) {
            logger.error("Failed to get server configuration");            
        }
                        
        return(dev);
    }
    
    /** Function returns connection state
     *
     * @return true when connection made, otherwise false
     *
     */
    
    public boolean isConnected() {
        return(connectStatus);
    }
    
    /** Function creates message listening server
     *
     * @return true when connection made, otherwise false
     *
     */
    
    private boolean startServer(int port) {        
        try {
            // get server instance
            yServerCon = new Server(port, this);
            return(true);
        } catch(YAMIException ex) {
            logger.warn("Listening server on port: " + port + " failed! " + ex.getMessage());
        }
        
        return(false);
    }
    
    /** Function creates message listening callback
     *
     * @param objectName message object name
     * @param messageName message name
     * @param parameters message parameters
     *
     * @return response message parameter set
     *
     */
    
    @Override
    public ParamSet process(String objectName, String messageName, ParamSet parameters) throws Exception {
        logger.debug("called: objact - " + objectName + "  message - " + messageName);
        
        // tähän pitää keksiä paremmat nimet
        //if (objectName.equals(clientName)) {
        if (objectName.equals(clientObj)) {
            if (messageName.equals("shutdown")) {
                return yami.Server.noReply;
            }
            
            // data stream message
            if(messageName.equals("server status")) {
                logger.info("status update");
                return yami.Server.noReply;
            }
            
            if (!callbacks.isEmpty()) {
                // get and make call back if exists
                MessageCallbackInterface call = callbacks.get(messageName);
                try {
                    if (!call.DataStreamCallback(parameters)) {
                        logger.warn("data callback failed: " + messageName);
                    }
                } catch (Exception ex) {
                    logger.error("exception in data callback: " + ex.getMessage() + " object: " + objectName + " name:" + messageName);
                }
                return yami.Server.noReply;
            }                        
        }        
        throw new NoSuchObjectException();                
    }
    
    /** Function adds message callback listener
     *
     * @param name hashtable key
     * @param msgIf callback function
     *
     * @return true if ok
     *
     */
    
    @Override
    public boolean addMessageCallback(String name, MessageCallbackInterface msgIf) {
        // add new callback function
        callbacks.put(name, msgIf);
        
        return(true);
    }
    
    /** Function sends message
     *
     * @param message
     * @param param
     *
     * @return response message parameter set
     *
     */
    
    @Override
    public ParamSet MessageSend(String message, ParamSet param) throws genericException {
        ParamSet PsetResp = null;
        
        if(yClientCon == null) {
            throw new genericException(0, "netCommand", "MessageSend", "No client connection", null); 
        }
        
        try {
            // send message
            PsetResp = yClientCon.send(ServerInstrument, message, param);
        } catch (Exception e) {
            logger.error("Sending message: " + message + " failed");
        }
        // return response to caller
        return(PsetResp);
    }
    
    /* Function request current measurement state of logic analyser
     *
     * Parameters:
     *
     * Return:
     * LA40State: current state on measurement device
     *
     */
    
    public LA40StateEnum LA40StatusRequest() {
        ParamSet PsetResp;
        String Msg;
        
        try {
            // send status get message
            PsetResp = yClientCon.send(ServerInstrument, MessageStatusGet);
            // get response
            Msg = PsetResp.extractString();
            // check response message
            if(Msg.compareTo(MessageStoppedResp) == 0) {
                // system is in stopped state
                System.out.println("Instrument in Stopped state");
                return(LA40StateEnum.Stopped);
            } else {
                if(Msg.compareTo(MessageArmedResp) == 0) {
                    // system is in armed state
                    System.out.println("Instrument in Armed state");
                    return(LA40StateEnum.Armed);
                } else {
                    if(Msg.compareTo(MessageTriggeredResp) == 0) {
                        // System is in Ttriggered state
                        System.out.println("Instrument in Triggered state");
                        return(LA40StateEnum.Triggered);
                    } else {
                        System.out.println("Instrument state get failed");
                        return(LA40StateEnum.Fault);
                    }
                }
            }
            
        } catch (Exception e) {
            System.out.println("Instrument state get failed");
            return(LA40StateEnum.Fault);
        }
    }
    
     /* Function  current measurement state of logic analyser
      *
      * Parameters:
      *
      * Return:
      * LA40State: current state on measurement device
      *
      */
    
    public void LA40ReadData(int pod, byte hiByte[], byte loByte[]) {
        int i = 0, p = 0,offset = 0;
        //byte[] rBytes = new byte[65536];
        byte[] tmp = new byte[1024];
        ParamSet PsetMsg, PsetResp;
        String Msg;
        
        // pod number
        LA40ReadData[0] = (byte)pod;
        
        // lo byte
        LA40ReadData[1] = 0x00;
        
        // request packets and store data
        for(i = 0; i < 64;i++) {
            // set packet number
            LA40ReadData[2] = (byte)((i + 1) & 0x00FF);
            // create parameter set for message and append data
            PsetMsg = new ParamSet();
            PsetMsg.append(LA40ReadData);
            
            try {
                // send data read message
                PsetResp = yClientCon.send(ServerInstrument, MessageRamDataRead, PsetMsg);
                // get response
                tmp = PsetResp.extractBinary();
                
                // virheen käsittely lisättävä
                
            } catch (Exception e) {
                System.out.println("Data read 1 failed");
                //return(false);
            }
            
            // put it in receive buffer
            for(p = 0;p < 1024;p++) {
                loByte[p + offset] = tmp[p];
            }
            // receive buffer offset increment
            offset += 1024;
        }
        
        // hi byte
        LA40ReadData[1] = 0x01;
        // init offset again
        offset = 0;
        
        // request packets and store data
        for(i = 0; i < 64;i++) {
            // set packet number
            LA40ReadData[2] = (byte)((i + 1) & 0x00FF);
            // create parameter set for message and append data
            PsetMsg = new ParamSet();
            PsetMsg.append(LA40ReadData);
            
            try {
                // send data read message
                PsetResp = yClientCon.send(ServerInstrument, MessageRamDataRead, PsetMsg);
                // get response
                tmp = PsetResp.extractBinary();
                
                // virheen käsittely lisättävä
                
            } catch (Exception e) {
                System.out.println("Data read 2 failed");
                //return(false);
            }
            
            // put it in receive buffer
            for(p = 0;p < 1024;p++) {
                hiByte[p + offset] = tmp[p];
            }
            // receive buffer offset increment
            offset += 1024;
        }
        
        // init values
        LA40ReadData[0] = 0x00;
        LA40ReadData[1] = 0x00;
        LA40ReadData[2] = 0x00;
    }
    
    
    /* Function sets new sample rate to logic analyser
     *
     * Parameters:
     * clkSel: sample rate
     *
     * Retuns:
     * true: on success
     * false: on failure
     */
    
    public boolean LA40SetSampleRate(byte clkSel) {
        ParamSet PsetMsg, PsetResp;
        String Msg;
        
        PsetMsg = new ParamSet();
        PsetMsg.append(clkSel);
        
        try {
            // send stop message
            PsetResp = yClientCon.send(ServerInstrument, MessageSampleRateSet, PsetMsg);
            // get response
            Msg = PsetResp.extractString();
            // check response message
            if(Msg.compareTo(MessageSampleRateResp) == 0) {
                System.out.println("Sample rate set");
            } else {
                System.out.println("Sample rate set failed");
                return(false);
            }
            
        } catch (Exception e) {
            System.out.println("Sample rate set failed");
            return(false);
        }
        
        return(true);
    }
    
    
    /* Function stops active measurement
     *
     * Parameters:
     *
     * Retuns:
     * true: on success
     * false: on failure
     */
    
    public boolean LA40SetStopState() {
        ParamSet PsetMsg, PsetResp;
        String Msg;
        
        PsetMsg = new ParamSet();
        PsetMsg.append(LA40Stop);
        
        try {
            // send stop message
            PsetResp = yClientCon.send(ServerInstrument, MessageStatusSet, PsetMsg);
            // get response
            Msg = PsetResp.extractString();
            // check response message
            if(Msg.compareTo(MessageStoppedResp) >= 0) {
                System.out.println("Stop state set");
            } else {
                System.out.println("Set stop state failed");
                return(false);
            }
            
        } catch (Exception e) {
            System.out.println("Set stop state failed");
            return(false);
        }
        
        return(true);
    }
    
    /* Function gets device armed for triggering
     *
     * Parameters:
     *
     * Retuns:
     * true: on success
     * false: on failure
     */
    
    public boolean LA40SetArmedState() {
        ParamSet PsetMsg, PsetResp;
        String Msg;
        
        PsetMsg = new ParamSet();
        PsetMsg.append(LA40Armed);
        
        try {
            // send armed message
            PsetResp = yClientCon.send(ServerInstrument, MessageStatusSet, PsetMsg);
            // get response
            Msg = PsetResp.extractString();
            // check response message
            if(Msg.compareTo(MessageArmedResp) >= 0) {
                System.out.println("Armed state set");
            } else {
                System.out.println("Set armed state failed");
                return(false);
            }
            
        } catch (Exception e) {
            System.out.println("Set armed state failed");
            return(false);
        }
        
        return(true);
    }
    
    /* Function sets trigger source
     *
     * Parameters:
     * trig: selected trigger source
     *
     * Retuns:
     * true: on success
     * false: on failure
     */
    
    public boolean LA40SetTriggerSource(boolean trig) {
        ParamSet PsetMsg, PsetResp;
        String Msg;
        
        PsetMsg = new ParamSet();
        
        // set trigger selection
        if(trig == true)
            PsetMsg.append(LA40TrigInt);
        else
            PsetMsg.append(LA40TrigExt);
        
        try {
            // send armed message
            PsetResp = yClientCon.send(ServerInstrument, MessageTriggerSet, PsetMsg);
            // get response
            Msg = PsetResp.extractString();
            // check response message
            if(Msg.compareTo(MessageTriggerResp) >= 0) {
                System.out.println("Trigger selection made");
            } else {
                System.out.println("Trigger selection failed");
                return(false);
            }
            
        } catch (Exception e) {
            System.out.println("Trigger selection failed");
            return(false);
        }
        
        return(true);
    }
    
    /* Function executes hardware tests
     *
     * Parameters:
     * testCase: Number on test case to run
     * state: State of signal line
     * value: Register value to write
     *
     * Retuns:
     * test case return value
     * -1: test failed
     */
    
    public int LA40TestExec(int testCase, boolean state, byte value) {
        ParamSet PsetMsg, PsetResp;
        String Msg;
        char result = ' ';
        
        PsetMsg = new ParamSet();
        
        switch(testCase) {
            case 1:
                // reset testing
                if(state == true)
                    // set reset active
                    PsetMsg.append(LA40TestResetDisable);
                else
                    // clear active reset
                    PsetMsg.append(LA40TestResetEnable);
                break;
            case 2:
                // clr testing
                if(state == true)
                    // set clr active
                    PsetMsg.append(LA40TestClrEnable);
                else
                    // clear clr
                    PsetMsg.append(LA40TestClrDisable);
                break;
            case 3:
                // step enable test case
                if(state == true)
                    // set step enable active
                    PsetMsg.append(LA40TestStepEnable);
                else
                    // clear step enable
                    PsetMsg.append(LA40TestStepDisable);
                break;
            case 4:
                // D8 test case
                if(state == true)
                    // set D8 active
                    PsetMsg.append(LA40TestD8Enable);
                else
                    // clear D8
                    PsetMsg.append(LA40TestD8Disable);
                break;
            case 5:
                // trig clear test case
                if(state == true)
                    // set trig clear active
                    PsetMsg.append(LA40TestTrigClrEnable);
                else
                    // diasable trig clear
                    PsetMsg.append(LA40TestTrigClrDisable);
                break;
            case 6:
                // memory size test case
                if(state == true)
                    // select 32k memory
                    PsetMsg.append(LA40TestMem32k);
                else
                    // select 64k memory
                    PsetMsg.append(LA40TestMem64k);
                break;
            case 7:
                // divider enable test case
                if(state == true)
                    // activate divider
                    PsetMsg.append(LA40TestDivEnable);
                else
                    // disable divider
                    PsetMsg.append(LA40TestDivDisable);
                break;
            case 8:
                // step line test case
                if(state == true)
                    // set step line active
                    PsetMsg.append(LA40TestStepLineEnable);
                else
                    // disable step line
                    PsetMsg.append(LA40TestStepLineDisable);
                break;
            case 9:
                // clk select test case
                LA40TestClk[1] = value;
                // set new clk value
                PsetMsg.append(LA40TestClk);
                LA40TestClk[1] = value;
                break;
            case 10:
                // trigger selection test case
                if(state == true)
                    // set external trigger source
                    PsetMsg.append(LA40TestTrigExternal);
                else
                    // set internal trigger source
                    PsetMsg.append(LA40TestTrigInternal);
                break;
            case 11:
                // U11 test case
                LA40TestU11[1] = value;
                // set new divider value
                PsetMsg.append(LA40TestU11);
                LA40TestU11[1] = value;
                break;
            case 12:
                // U12 test case
                LA40TestU12[1] = value;
                // set new divider value
                PsetMsg.append(LA40TestU12);
                LA40TestU12[1] = value;
                break;
            case 13:
                // Status register test case
                // get status register
                PsetMsg.append(LA40TestStatus);
                break;
            case 14:
                // RAM OE test case for base card
                if(state == true) {
                    // select desired card
                    LA40TestRamOEEnable[2] = value;
                    // set output enable state
                    PsetMsg.append(LA40TestRamOEEnable);
                    LA40TestRamOEEnable[2] = 0;
                } else {
                    // select desired card
                    LA40TestRamOEDisable[2] = value;
                    // set output enable state
                    PsetMsg.append(LA40TestRamOEDisable);
                    LA40TestRamOEDisable[2] = 0;
                }
                break;
            case 15:
                // RAM input enable test case for base card
                if(state == true) {
                    // select desired card
                    LA40TestRamInputEnable[2] = value;
                    // set input enable value
                    PsetMsg.append(LA40TestRamInputEnable);
                    LA40TestRamInputEnable[2] = 0;
                } else {
                    // select desired card
                    LA40TestRamInputDisable[2] = value;
                    // set input enable value
                    PsetMsg.append(LA40TestRamInputDisable);
                    LA40TestRamInputDisable[2] = 0;
                }
                break;
            case 16:
                // RAM OE test case for extension card
                if(state == true) {
                    // select desired card
                    LA40TestRamOEExtEnable[2] = value;
                    // set output enable state
                    PsetMsg.append(LA40TestRamOEExtEnable);
                    LA40TestRamOEExtEnable[2] = 0;
                } else {
                    // select desired card
                    LA40TestRamOEExtDisable[2] = value;
                    //set output enable state
                    PsetMsg.append(LA40TestRamOEExtDisable);
                    LA40TestRamOEExtDisable[2] = 0;
                }
                break;
            case 17:
                // RAM input enable test case for extension card
                if(state == true) {
                    // select desired card
                    LA40TestRamInputExtEnable[2] = value;
                    // set input enable value
                    PsetMsg.append(LA40TestRamInputExtEnable);
                    LA40TestRamInputExtEnable[2] = 0;
                } else {
                    // select desired card
                    LA40TestRamInputExtDisable[2] = value;
                    // set input enable value
                    PsetMsg.append(LA40TestRamInputExtDisable);
                    LA40TestRamInputExtDisable[2] = 0;
                }
                break;
            case 18:
                // RAM input read test for base card
                PsetMsg.append(LA40TestRamInput);
                break;
            case 19:
                // RAM input read test for extension card
                PsetMsg.append(LA40TestRamInputExt);
                break;
            default:
                return(-1);
        }
        
        try {
            if(testCase < 14 && testCase >= 1) {
                // send test request message to counter card
                PsetResp = yClientCon.send(ServerInstrument, MessageCounterTest, PsetMsg);
            } else {
                // send test request message to ram card
                PsetResp = yClientCon.send(ServerInstrument, MessageRamCardTest, PsetMsg);
            }
            
            // get response
            Msg = PsetResp.extractString();
            
            result = Msg.charAt(Msg.length() - 1);
            Msg = Msg.substring(0, Msg.length() - 1);
            
            // check response message
            if(Msg.compareTo(MessageCounterTestResp) == 0) {
                System.out.println("Counter test success");
            } else {
                System.out.println("Counter test failed");
                return(-1);
            }
            
        } catch (Exception e) {
            System.out.println("Counter test failed");
            return(-1);
        }
        
        return(Character.getNumericValue(result));
    }
}
