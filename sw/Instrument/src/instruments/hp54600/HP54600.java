/***********************************************************
 * Software: instrument client
 * Module:   HP54600 scope instances managing class
 * Version:  0.2
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.9.2012
 *
 ***********************************************************/
package instruments.hp54600;

import components.DeviceMessage;
import instruments.scopedemo.*;
import javax.swing.*;
import lib.gui.*;
import lib.gui.interfaces.PlotCursorInterface;
import org.apache.log4j.Logger;
import components.ScaleSpinnerBox;
import components.XmlReader;
import components.XmlWriter;
import lib.common.utilities.ConfigurationInstance;
import interfaces.MessageCallbackInterface;
import interfaces.MessageInterface;
import interfaces.OptionsInterface;
import interfaces.ScaleSpinnerBoxInterface;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Hashtable;
import lib.gui.interfaces.WaveformDataInterface;
import lib.gui.models.LinearVoltageScale;
import lib.gui.models.SampleScale;
import lib.gui.primitives.PlotCursor;
import lib.ui.buttons.ModelButton;
import lib.ui.comboboxes.ControlledComboBoxItem;
import lib.ui.comboboxes.ControlledComboBoxRenderer;
import lib.ui.comboboxes.uiHelpers;
import lib.ui.fileWidgets.AdvancedFileChooser;
import yami.ParamSet;

public class HP54600 extends javax.swing.JInternalFrame implements
        MessageCallbackInterface, ScaleSpinnerBoxInterface, PlotCursorInterface,
        OptionsInterface {

    private ScopePlot scope;
    private int ch_cnt;
    private static Logger logger;
    private ConfigurationInstance config;
    private boolean initDone = false;
    private DeviceMessage ScopeMsg;
    private Color backGround1;
    private Color backGround2;
    private Color foreGround;
    private double hScaler;
    private String hScalerUnit;
    private WaveformDataInterface dIf[];
    private String cmdId = null;
    private int captureInterval = 100;
    private String settingsFile = "";
    private File currentDir;
    private ScaleSpinnerBox bx[];
    private ChannelControls cs[];
    private static final String MessageAutoscale = "Autoscale";
    private static final String MessageRun = "Run";
    private static final String MessageStop = "Stop";
    private static final String MessageTimebaseRange = "TimebaseRange";
    private static final String MessageTimebaseParamsRead = "TimebaseParametersRead";
    private static final String MessageTriggerMode = "TrigMode";
    private static final String MessageTriggerSource = "TrigSource";
    private static final String MessageTriggerSlope = "TrigSlope";
    private static final String MessageTriggerCouple = "TrigCouple";
    private static final String MessageTriggerReject = "TrigReject";
    private static final String MessageTriggerNoiseReject = "TrigNoiseReject";
    private static final String MessageTriggerPolarity = "TrigPolarity";
    private static final String MessageTriggerTVMode = "TrigTVMode";
    private static final String MessageTriggerTVHFReject = "TrigTVHFReject";
    private static final String MessageTriggerLevel = "TrigLevel";
    private static final String MessageTriggerHoldOff = "TrigHoldOff";
    private static final String MessageTriggerParamsRead = "TrigParametersRead";
    private static final String MessageVoltageLevel = "VoltageScale";
    private static final String MessageVoltageOffset = "VoltageOffset";
    private static final String MessageChannelStatus = "ChannelStatus";
    private static final String MessageChannelStatusRead = "ChannelStatusRead";
    private static final String MessageChannelParamsRead = "ChannelParametersRead";
    private static final String MessageWaveformPreambleRead = "WaveformPreambleRead";
    private Object[] tvTriggerItems = {
        new ControlledComboBoxItem("Field1"),
        new ControlledComboBoxItem("Field2"),
        new ControlledComboBoxItem("LINE"),
        new ControlledComboBoxItem("Vertical", false)
    };
    private static final String[] channels = {"Ch1", "Ch2", "Ch3", "Ch4"};
    private static final String[] vertScale = {
        "5 V/div", "2 V/div", "1 V/div",
        "500 mV/div", "200 mV/div", "100 mV/div",
        "50 mV/div", "20 mV/div", "10 mV/div",
        "5 mV/div", "2 mV/div", "1 mV/div"
    };
    private static final double[] scaleVoltage = {
        5.0, 2.0, 1.0, 0.5, 0.2, 0.1, 0.05, 0.02, 0.01,
        0.005, 0.002, 0.001
    };
    private static final String[] hScale = {
        "500 ms/div", "200 ms/div", "100 ms/div",
        "50 ms/div", "20 ms/div", "10 ms/div",
        "5 ms/div", "2 ms/div", "1 ms/div",
        "500 us/div", "200 us/div", "100 us/div",
        "50 us/div", "20 us/div", "10 us/div",
        "5 us/div", "2 us/div", "1 us/div",
        "500 ns/div", "200 ns/div", "100 ns/div",
        "50 ns/div", "20 ns/div", "10 ns/div"
    };
    private static final double[] hScaleTime = {
        0.5, 0.2, 0.1, 0.05, 0.02, 0.01, 5E-3, 2E-3, 1E-3, 5E-4,
        2E-4, 1E-4, 5E-5, 2E-5, 1E-5, 5E-6,
        2E-6, 1E-6, 5E-7, 2E-7, 1E-7, 5E-8,
        2E-8, 1E-8
    };
    private static final double[] hScalerTable = {1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0,
        1E6, 1E6, 1E6, 1E6, 1E6, 1E6, 1E6, 1E6, 1E6, 1E9, 1E9, 1E9, 1E9, 1E9, 1E9
    };

    /** Creates new form DSODemo */
    public HP54600(String name, Hashtable<String, String> properties, MessageInterface msg) {
        // get logger instance for this class".
        logger = Logger.getLogger(HP54600.class);
        // get configuration
        config = ConfigurationInstance.getConfiguration();
        // add message helper
        ScopeMsg = new DeviceMessage(name, msg);
        // add message listener
        ScopeMsg.addMessageCallback(name, this);

        try {
            // get number of channels
            ch_cnt = Integer.parseInt(properties.get("ch_count"));
        } catch (Exception ex) {
            logger.error("channel count can't be parsed");
        }

        // default colors
        backGround1 = new Color(196, 204, 223);
        backGround2 = new Color(196, 204, 223);
        foreGround = new Color(0, 0, 0);

        initComponents();
        bx = new ScaleSpinnerBox[ch_cnt];
        cs = new ChannelControls[ch_cnt];

        // read configuration variables
        update();

        hScaler = hScalerTable[0];
        hScalerUnit = "ms";

        // initialize scope component
        scope = new ScopePlot(ch_cnt, ((PlotCursorInterface) this));

        jPanel23.add(scope, java.awt.BorderLayout.CENTER);

        // add scope waveform data listeners 
        dIf = new WaveformDataInterface[ch_cnt];

        ScopeMsg.sendString(MessageTimebaseParamsRead);

        // round to nearest value
        double timeScale = new BigDecimal(ScopeMsg.readDoubleValue(1) / 10.0D).setScale(7, RoundingMode.HALF_UP).doubleValue();

        //logger.debug("timescale " + timeScale);

        for (int i = 0; i < hScaleTime.length; i++) {
            // find current time scale
            if (hScaleTime[i] == timeScale) {
                // set for user control
                ((SpinnerListModel) timeScaleSpinner.getModel()).setValue(hScale[i]);
                break;
            }
        }

        // add channel components
        for (int i = 0; i < ch_cnt; i++) {
            jPanel7.add(bx[i] = new ScaleSpinnerBox(this, i, channels[i], false,
                    new SpinnerListModel(vertScale), new SpinnerNumberModel(0.0, -20.0, 20.0, 0.01)));

            bx[i].setBgColor(backGround1);
            bx[i].setSpinnerColors(backGround2, foreGround);

            // add default waveform data model to channel
            HP54600WaveModel waveModel = new HP54600WaveModel(i);

            // set wave model scales    
            SampleScale s = new SampleScale();
            // set time full scale value for unit scaling
            s.setUnitScale(timeScale * 10.0D);

            LinearVoltageScale v = new LinearVoltageScale();
            waveModel.addVerticalScale(v);
            waveModel.addHorizontalScale(s);
            scope.setWaveModel(i, waveModel);

            // read waveform preamble information 
            ScopeMsg.sendValue(MessageWaveformPreambleRead, i + 1);

            // set slider according data count
            int sampleCount = ScopeMsg.readIntValue(2);
            s.setSampleScale(sampleCount);
            jScrollBar1.setMaximum(1);

            // set waveform data parameters
            waveModel.setPreambleVariables(
                    ScopeMsg.readDoubleValue(3),
                    ScopeMsg.readDoubleValue(5),
                    ScopeMsg.readDoubleValue(4),
                    ScopeMsg.readDoubleValue(6),
                    ScopeMsg.readDoubleValue(8),
                    ScopeMsg.readDoubleValue(7));

            // read channel parameter information 
            ScopeMsg.sendValue(MessageChannelParamsRead, i + 1);

            // set scales, full scale value
            double voltsFullScale = ScopeMsg.readDoubleValue(0);
            scope.setVoltageScale(i, voltsFullScale);

            for (int j = 0; j < vertScale.length; j++) {
                if ((voltsFullScale / HP54600Constants.yScaleFactor) == scaleVoltage[j]) {
                    // set initial voltage scale value to spinner
                    bx[i].setSpinner1Value(vertScale[j]);
                }
            }

            // set gnd symbols
            scope.setGndRef(i, ScopeMsg.readDoubleValue(1));

            // get current state from scope 
            ScopeMsg.sendValue(MessageChannelStatusRead, i + 1);
            boolean state = ScopeMsg.readBooleanValue(0);

            // set state to components
            bx[i].setSelected(state);
            scope.setChActive(i, state);

            jComboBox1.addItem(uiHelpers.makeObj(channels[i]));
            jComboBox2.addItem(uiHelpers.makeObj(channels[i]));

            // TODO pitäisikö tehdä lista dynaamisesti....
            //triggerSrcComboBox.addItem(uiHelpers.makeObj(channels[i]));

            // add channel controls
            cs[i] = new ChannelControls(i, ScopeMsg);
            jTabbedPane1.add(cs[i]);

            // get channel data interfaces
            dIf[i] = scope.getDataInterface(i);
        }

        triggerUpdate();

        setSize(769, 629);
        initDone = true;
    }

    /** Function updates all trigger components
     * 
     */
    public void triggerUpdate() {
        ScopeMsg.sendString(MessageTriggerParamsRead);
        // TODO tarkasta toiminta
        triggerModeComboBox.setSelectedIndex(ScopeMsg.readByteValue(0));
        triggerSrcComboBox.setSelectedIndex(ScopeMsg.readByteValue(1));
        triggerLevelSpinner.getModel().setValue(ScopeMsg.readDoubleValue(2));
        triggerHoldoffSpinner.getModel().setValue(ScopeMsg.readDoubleValue(3));
        triggerSlopeComboBox.setSelectedIndex(ScopeMsg.readByteValue(4));
        triggerCplComboBox.setSelectedIndex(ScopeMsg.readByteValue(5));
        triggerRejectComboBox.setSelectedIndex(ScopeMsg.readByteValue(6));
        triggerNoiseRejectComboBox.setSelectedIndex(ScopeMsg.readByteValue(7));
        triggerPolarityComboBox.setSelectedIndex(ScopeMsg.readByteValue(8));
        triggerTVModeComboBox.setSelectedIndex(ScopeMsg.readByteValue(9));
        triggerTVHFRejectComboBox.setSelectedIndex(ScopeMsg.readByteValue(10));
    }

    public void updatePreamble(int ch) {
        // read waveform preamble information 
        ScopeMsg.sendValue(MessageWaveformPreambleRead, ch + 1);
        HP54600WaveModel wm = (HP54600WaveModel) scope.getWavemodel(ch);

        // set slider according data count
        int sampleCount = ScopeMsg.readIntValue(2);
        ((SampleScale) wm.getHorizontalScale()).setSampleScale(sampleCount);
        //s.setSampleScale(sampleCount);
        jScrollBar1.setMaximum(1);

        // set waveform data parameters
        wm.setPreambleVariables(
                ScopeMsg.readDoubleValue(3),
                ScopeMsg.readDoubleValue(5),
                ScopeMsg.readDoubleValue(4),
                ScopeMsg.readDoubleValue(6),
                ScopeMsg.readDoubleValue(8),
                ScopeMsg.readDoubleValue(7));
    }

    /** Function handles waveform visual state callbacks
     *
     * @param boxNumber identity of spinner box
     * @param state selection state
     *
     */
    @Override
    public void SpinnerBoxisEnabled(int boxNumber, boolean state) {
        int chState = (state) ? 1 : 0;

        // enable or disable wave form
        scope.setChActive(boxNumber, state);
        // set new channel state
        ScopeMsg.sendValue(MessageChannelStatus, boxNumber + 1, chState);
    }

    /** Function handles spinner 1 modification callbacks
     *
     * @param boxNumber identity of spinner box
     * @param value spinner value as object
     *
     */
    @Override
    public void SpinnerBox1ValueChanged(int boxNumber, Object value) {
        try {
            // evaluate voltage scale value
            for (int i = 0; i < vertScale.length; i++) {
                if (vertScale[i].equals(value)) {
                    // set voltage scale to ch[boxNumber] kerrottava 8 saadaan fullscale arvo
                    scope.setVoltageScale(boxNumber, scaleVoltage[i] * HP54600Constants.yScaleFactor);
                    ScopeMsg.sendValue(MessageVoltageLevel, boxNumber + 1, scaleVoltage[i]);
                    updatePreamble(boxNumber);
                }
            }
        } catch (NumberFormatException e) {
            logger.error("Failed to convert String to Double: " + e.getMessage());
        }
    }

    /** Function handles spinner 2 modification callbacks
     *
     * @param boxNumber identity of spinner box
     * @param value spinner value as object
     *
     */
    @Override
    public void SpinnerBox2ValueChanged(int boxNumber, Object value) {
        try {
            Double d = Double.parseDouble(value.toString());

            // set gnd reference value to ch[boxNumber]
            scope.setGndRef(boxNumber, d);
            ScopeMsg.sendValue(MessageVoltageOffset, boxNumber + 1, d);

        } catch (NumberFormatException e) {
            logger.error("Failed to convert String to Double: " + e.getMessage());
        }
    }

    /** Function xx cursor raw values
     * 
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * 
     */
    @Override
    public void cursorPositionRawValues(int x1, int x2, int y1, int y2) {
    //cursorValueY1.setText(String.valueOf(x1));
    //cursorValueX2.setText(String.valueOf(x2));
        /* TODO mitä pitäisi laskea
    - x1 paikka
    x2 paikka
    delta x1-x2
    y1 paikka
    y2 paikka
    operaatio(y1,y2) +,- ainakin
    y1,y2  pitää voida sitoa aaltomuotoon
    miten haetaan huippuarvo, näytetään arvo kursorin kohdassa
     */
    }

    /** Function updates cursor values
     * 
     * @param x1 scaled cursor value
     * @param x2 scaled cursor value
     * @param y1 scaled cursor value
     * @param y2 scaled cursor value
     * 
     */
    @Override
    public void cursorPositionScaledValues(double x1, double x2, double y1, double y2) {
        DecimalFormat f = new DecimalFormat("0.00");
        cursorValueX1.setText(f.format(x1 * hScaler) + hScalerUnit);
        cursorValueX2.setText(f.format(x2 * hScaler) + hScalerUnit);
        cursorDeltaX.setText(f.format((x1 - x2) * hScaler) + hScalerUnit);
        cursorValueY1.setText(f.format(y1) + "V");
        cursorValueY2.setText(f.format(y2) + "V");
        cursorDeltaY.setText(f.format(y1 - y2) + "V");
    }

    /** Function handles data capture request
     * 
     * @param state true if capture eneble
     * @param id command unique id existing command
     * @return command unique id to new command
     */
    public String capture(boolean state, String id) {
        byte chSelect = 0;

        ParamSet PsetMsg = new ParamSet();

        if (!state) {
            // stop measurement             
            PsetMsg.append(DeviceMessage.MessageMeasurementStop);
            PsetMsg.append((byte) DeviceMessage.MeasurementCmdCapture);
            PsetMsg.append(id);

            ScopeMsg.commandMessage(PsetMsg);
            return ("");
        } else {
            // add channel selections        
            for (int i = 0; i < ch_cnt; i++) {
                if (scope.getChActive(i)) {
                    chSelect |= 1 << i;
                }
            }

            // command
            PsetMsg.append(DeviceMessage.MessageMeasurementStart);
            PsetMsg.append((byte) DeviceMessage.MeasurementCmdCapture);
            PsetMsg.append((byte) chSelect);     // no channel info
            PsetMsg.append(1);                   // boolean inteval in use
            PsetMsg.append(1);                   // boolean measurement enabled
            PsetMsg.append(captureInterval);     // interval time

            // send message to server
            if (ScopeMsg.commandMessage(PsetMsg)) {
                // read command id
                return (ScopeMsg.getUniqueId());
            }

            return ("");
        }
    }

    /** Function implemenst data stream handler
     * 
     * @param param send data from server
     * @return
     * 
     */
    public boolean DataStreamCallback(ParamSet param) {
        int ch = 0;
        logger.debug("data stream parametercount: " + param.getNumberOfEntries());

        try {
            // check for valid message
            if (param.extractString().equals(DeviceMessage.StreamDataOk)) {
                // read data array size
                int dataLength = param.extractInt();
                // loop all data sets
                for (int i = 0; i < ((param.getNumberOfEntries() - 2) / 2); i++) {
                    logger.debug("data set found ch: " + ch);
                    ch = param.extractInt();
                    // set data to correct channel
                    dIf[ch - 1].updateData(param.extractBinary());
                    // update display
                    scope.repaint();
                }
            } else {
                logger.warn("streaming data fail");
            }
        } catch (Exception ex) {
            logger.error("failed to extract parameter from response" + ex.getMessage());
        }
        return (true);
    }

    /** Function reads and updates configuration parameters
     * 
     */
    public void update() {
        try {
            captureInterval = config.getInt(HP54600Options.HP54600_CAPTURE_INTERVAL);
        } catch (Exception ex) {

        }
    }

    /** Function adds new frame to desktop
     * 
     * @param e frame to add
     * 
     */
    public void addToDesktop(JInternalFrame f) {
        // set loaction on desktop        
        Dimension desktopSize = this.getDesktopPane().getSize();
        Dimension jInternalFrameSize = f.getSize();
        f.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        // add dialog to desktop
        this.getParent().add(f);
        // bring dialog to top window
        this.moveToBack();
    }

    /** Function restores setting from file
     * 
     * @param r reference to xml file reader
     */
    public void loadSettings(XmlReader r) {
        HP54600WaveformParser parser = new HP54600WaveformParser(this);
        r.setWaveformParser(parser);

        if (r.parse(ScopeMsg.getName())) {
            // trigger parameters
            triggerModeComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_MODE));
            triggerSrcComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_SOURCE));
            triggerLevelSpinner.setValue(r.getDouble(HP54600Constants.PARAM_TRIGGER_LEVEL));
            triggerHoldoffSpinner.setValue(r.getDouble(HP54600Constants.PARAM_TRIGGER_HOLDOFF));
            triggerSlopeComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_SLOPE));
            triggerCplComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_COUPLING));
            triggerRejectComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_REJECT));
            triggerNoiseRejectComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_NOISE_REJECT));
            triggerPolarityComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_POLARITY));
            triggerTVModeComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_TV_MODE));
            triggerTVHFRejectComboBox.setSelectedIndex(r.getInteger(HP54600Constants.PARAM_TRIGGER_TV_HF_REJECT));

            // get timescale
            double timeScale = r.getDouble(HP54600Constants.PARAM_TIME_SCALE);

            for (int i = 0; i < hScaleTime.length; i++) {
                // find current time scale
                if (hScaleTime[i] == timeScale) {
                    // set for user control
                    ((SpinnerListModel) timeScaleSpinner.getModel()).setValue(hScale[i]);
                    break;
                }
            }

            // parse voltage scale parameter
            for (int j = 1; j < ch_cnt; j++) {
                // evaluate voltage scale value

                double v = r.getDouble(HP54600Constants.PARAM_VOLTAGE_SCALE, j);

                for (int a = 0; a < vertScale.length; a++) {
                    if (v == scaleVoltage[a]) {
                        // set initial voltage scale value to spinner
                        bx[j].setSpinner1Value(vertScale[a]);
                    }
                }

                // offset value
                bx[j].setSpinner2Value(r.getDouble(HP54600Constants.PARAM_VOLTAGE_OFFSET, j));
                // channel parameters
                cs[j].setBwLimit(r.getInteger(HP54600Constants.PARAM_CHANNEL_BW_LIMIT, j));
                cs[j].setCoupling(r.getInteger(HP54600Constants.PARAM_CHANNEL_COUPLING, j));
                cs[j].setInvert(r.getInteger(HP54600Constants.PARAM_CHANNEL_INVERT, j));
                cs[j].setProbe(r.getInteger(HP54600Constants.PARAM_CHANNEL_PROBE, j));
                cs[j].setVernier(r.getInteger(HP54600Constants.PARAM_CHANNEL_VERNIER, j));

                parser.getWaveform(j, scope.getWavemodel(j));
            }
        }
    }

    /** Function stores scope setting to file
     * 
     * @param file to save
     */
    public void saveSettings(XmlWriter w) {

        String name = ScopeMsg.getName();

        // add new configuration 
        w.addConficSection(name);

        // time scale parameter
        for (int i = 0; i < hScale.length; i++) {
            if (hScale[i].equals(timeScaleSpinner.getValue()) == true) {
                // set parameter value
                w.addParamElement(HP54600Constants.PARAM_TIME_SCALE, "", Double.toString(hScaleTime[i]));
            }
        }

        // parse voltage scale parameter
        for (int j = 1; j < ch_cnt; j++) {
            // evaluate voltage scale value
            for (int i = 0; i < vertScale.length; i++) {
                if (vertScale[i].equals(bx[j].getSpinner1Value())) {
                    // voltage scale
                    w.addParamElement(HP54600Constants.PARAM_VOLTAGE_SCALE,
                            Integer.toString(j), Double.toString(scaleVoltage[i]));
                }
            }

            // offset value
            w.addParamElement(HP54600Constants.PARAM_VOLTAGE_OFFSET,
                    Integer.toString(j), Double.toString(bx[j].getSpinner2Value()));
            // channel parameters
            w.addParamElement(HP54600Constants.PARAM_CHANNEL_BW_LIMIT, Integer.toString(j), Integer.toString(cs[j].getBwLimit()));
            w.addParamElement(HP54600Constants.PARAM_CHANNEL_COUPLING, Integer.toString(j), Integer.toString(cs[j].getCoupling()));
            w.addParamElement(HP54600Constants.PARAM_CHANNEL_INVERT, Integer.toString(j), Integer.toString(cs[j].getInvert()));
            w.addParamElement(HP54600Constants.PARAM_CHANNEL_PROBE, Integer.toString(j), Integer.toString(cs[j].getProbe()));
            w.addParamElement(HP54600Constants.PARAM_CHANNEL_VERNIER, Integer.toString(j), Integer.toString(cs[j].getVernier()));
        }

        // trigger parameters
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_MODE, "", Integer.toString(triggerModeComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_SOURCE, "", Integer.toString(triggerSrcComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_LEVEL, "", triggerLevelSpinner.getValue().toString());
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_HOLDOFF, "", triggerHoldoffSpinner.getValue().toString());
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_SLOPE, "", Integer.toString(triggerSlopeComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_COUPLING, "", Integer.toString(triggerCplComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_REJECT, "", Integer.toString(triggerRejectComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_NOISE_REJECT, "", Integer.toString(triggerNoiseRejectComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_POLARITY, "", Integer.toString(triggerPolarityComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_TV_MODE, "", Integer.toString(triggerTVModeComboBox.getSelectedIndex()));
        w.addParamElement(HP54600Constants.PARAM_TRIGGER_TV_HF_REJECT, "", Integer.toString(triggerTVHFRejectComboBox.getSelectedIndex()));

        //if (storeWaveforms) {
        w.addWaveformSection(name);

        // save waveform data
        for (int i = 0; i < ch_cnt; i++) {
            HP54600WaveModel wm = (HP54600WaveModel) scope.getWavemodel(i);
            w.addWaveform(i + 1, dIf[i].getData(),
                    wm.getXIncrement(), wm.getXReference(), wm.getXOrigin(),
                    wm.getYIncrement(), wm.getYReference(), wm.getYOrigin());
        }
    //}
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cursorButtonGroup = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel14 = new javax.swing.JPanel();
        timeScaleSpinner = new javax.swing.JSpinner();
        jSpinner1 = new javax.swing.JSpinner();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        controlsPanel = new javax.swing.JPanel();
        autoscaleModelButton = new lib.ui.buttons.ModelButton();
        runModelButton = new ModelButton("Stop", "Run");
        cursorPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cursorValueX1 = new javax.swing.JTextField();
        labelX1 = new javax.swing.JLabel();
        labelX2 = new javax.swing.JLabel();
        cursorValueX2 = new javax.swing.JTextField();
        labelY1 = new javax.swing.JLabel();
        cursorValueY1 = new javax.swing.JTextField();
        labelY2 = new javax.swing.JLabel();
        cursorValueY2 = new javax.swing.JTextField();
        cursorDeltaX = new javax.swing.JTextField();
        labelDeltaX = new javax.swing.JLabel();
        labelDeltaY = new javax.swing.JLabel();
        cursorDeltaY = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        CursorNoneRadioButton = new javax.swing.JRadioButton();
        CursorXRadioButton = new javax.swing.JRadioButton();
        CursorYRadioButton = new javax.swing.JRadioButton();
        CursorXYRadioButton = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        triggerPanel = new javax.swing.JPanel();
        triggerModeComboBox = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        triggerSrcComboBox = new javax.swing.JComboBox();
        jLabel9 = new javax.swing.JLabel();
        triggerLevelSpinner = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        triggerSlopeComboBox = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        triggerCplComboBox = new javax.swing.JComboBox();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel12 = new javax.swing.JLabel();
        triggerRejectComboBox = new javax.swing.JComboBox();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        triggerHoldoffSpinner = new javax.swing.JSpinner();
        triggerNoiseRejectComboBox = new javax.swing.JComboBox();
        jLabel14 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel15 = new javax.swing.JLabel();
        triggerPolarityComboBox = new javax.swing.JComboBox();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel16 = new javax.swing.JLabel();
        triggerTVModeComboBox = new javax.swing.JComboBox(tvTriggerItems);
        triggerTVModeComboBox.setRenderer(new ControlledComboBoxRenderer());
        jSeparator10 = new javax.swing.JSeparator();
        jLabel17 = new javax.swing.JLabel();
        triggerTVHFRejectComboBox = new javax.swing.JComboBox();
        jSeparator11 = new javax.swing.JSeparator();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jPanel12 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        OpenMenuItem = new javax.swing.JMenuItem();
        SaveMenuItem = new javax.swing.JMenuItem();
        SaveAsMenuItem = new javax.swing.JMenuItem();
        ExportMenuItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        PrintMenuItem = new javax.swing.JMenuItem();
        CloseMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        OptionsMenuItem = new javax.swing.JMenuItem();

        setBackground(new java.awt.Color(196, 204, 223));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("HP54600");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });

        jPanel2.setBackground(new java.awt.Color(196, 204, 223));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel8.setBackground(new java.awt.Color(196, 204, 223));
        jPanel8.setLayout(new java.awt.BorderLayout());

        jScrollBar1.setMaximum(1);
        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });
        jPanel8.add(jScrollBar1, java.awt.BorderLayout.NORTH);

        jPanel14.setBackground(new java.awt.Color(196, 204, 223));
        jPanel14.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        timeScaleSpinner.setBackground(new java.awt.Color(194, 225, 255));
        timeScaleSpinner.setModel(new SpinnerListModel(hScale));
        timeScaleSpinner.setToolTipText("Time scale");
        timeScaleSpinner.setPreferredSize(new java.awt.Dimension(100, 24));
        timeScaleSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timeScaleSpinnerStateChanged(evt);
            }
        });
        jPanel14.add(timeScaleSpinner);

        jSpinner1.setBackground(new java.awt.Color(194, 225, 255));
        jSpinner1.setForeground(new java.awt.Color(0, 0, 0));
        jSpinner1.setModel(new SpinnerNumberModel(0.0,-20.0,20.0,0.01));
        jSpinner1.setToolTipText("Trigger position");
        jSpinner1.setPreferredSize(new java.awt.Dimension(100, 24));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });
        jPanel14.add(jSpinner1);

        jPanel8.add(jPanel14, java.awt.BorderLayout.CENTER);

        jTabbedPane2.setBackground(new java.awt.Color(196, 204, 223));

        jPanel7.setBackground(new java.awt.Color(196, 204, 223));
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel9.setBackground(new java.awt.Color(196, 204, 223));
        jPanel9.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel9.setLayout(new java.awt.GridBagLayout());
        jPanel7.add(jPanel9);

        jTabbedPane2.addTab("Channel", jPanel7);

        jPanel18.setBackground(new java.awt.Color(196, 204, 223));
        jTabbedPane2.addTab("Values", jPanel18);

        jPanel8.add(jTabbedPane2, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel23.setBackground(new java.awt.Color(196, 204, 223));
        jPanel23.setMaximumSize(new java.awt.Dimension(540, 440));
        jPanel23.setMinimumSize(new java.awt.Dimension(540, 440));
        jPanel23.setPreferredSize(new java.awt.Dimension(540, 440));
        jPanel23.setLayout(new java.awt.BorderLayout());
        jPanel2.add(jPanel23, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.setBackground(new java.awt.Color(196, 204, 223));
        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        jTabbedPane1.setFont(new java.awt.Font("Dialog", 0, 10));

        controlsPanel.setBackground(new java.awt.Color(196, 204, 223));
        controlsPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        autoscaleModelButton.setText("Autoscale");
        autoscaleModelButton.setColorModel(ModelButton.ModelNormal);
        autoscaleModelButton.setMaximumSize(new java.awt.Dimension(90, 24));
        autoscaleModelButton.setMinimumSize(new java.awt.Dimension(90, 24));
        autoscaleModelButton.setPreferredSize(new java.awt.Dimension(90, 24));
        autoscaleModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                autoscaleModelButtonActionPerformed(evt);
            }
        });
        controlsPanel.add(autoscaleModelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 5, 120, 30));

        runModelButton.setColorModel(ModelButton.ModelNormal);
        runModelButton.setToggleMode(true);
        runModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                runModelButtonActionPerformed(evt);
            }
        });
        controlsPanel.add(runModelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 120, 30));

        jTabbedPane1.addTab("Controls", controlsPanel);

        cursorPanel.setBackground(new java.awt.Color(196, 204, 223));
        cursorPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(196, 204, 223));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Values"));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cursorValueX1.setEditable(false);
        cursorValueX1.setForeground(new java.awt.Color(0, 255, 0));
        cursorValueX1.setAutoscrolls(false);
        cursorValueX1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueX1.setFocusable(false);
        cursorValueX1.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueX1.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueX1.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueX1.setRequestFocusEnabled(false);
        cursorValueX1.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueX1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, 20));

        labelX1.setFont(new java.awt.Font("Dialog", 0, 10));
        labelX1.setText("X1:");
        labelX1.setEnabled(false);
        labelX1.setMaximumSize(new java.awt.Dimension(30, 25));
        labelX1.setMinimumSize(new java.awt.Dimension(30, 25));
        labelX1.setPreferredSize(new java.awt.Dimension(30, 25));
        labelX1.setRequestFocusEnabled(false);
        labelX1.setVerifyInputWhenFocusTarget(false);
        labelX1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(labelX1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        labelX2.setFont(new java.awt.Font("Dialog", 0, 10));
        labelX2.setText("X2:");
        labelX2.setEnabled(false);
        labelX2.setMaximumSize(new java.awt.Dimension(30, 25));
        labelX2.setMinimumSize(new java.awt.Dimension(30, 25));
        labelX2.setPreferredSize(new java.awt.Dimension(30, 25));
        labelX2.setRequestFocusEnabled(false);
        labelX2.setVerifyInputWhenFocusTarget(false);
        labelX2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(labelX2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        cursorValueX2.setEditable(false);
        cursorValueX2.setForeground(new java.awt.Color(0, 255, 0));
        cursorValueX2.setAutoscrolls(false);
        cursorValueX2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueX2.setFocusable(false);
        cursorValueX2.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueX2.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueX2.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueX2.setRequestFocusEnabled(false);
        cursorValueX2.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueX2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, 20));

        labelY1.setFont(new java.awt.Font("Dialog", 0, 10));
        labelY1.setText("Y1:");
        labelY1.setEnabled(false);
        labelY1.setMaximumSize(new java.awt.Dimension(30, 25));
        labelY1.setMinimumSize(new java.awt.Dimension(30, 25));
        labelY1.setPreferredSize(new java.awt.Dimension(30, 25));
        labelY1.setRequestFocusEnabled(false);
        labelY1.setVerifyInputWhenFocusTarget(false);
        labelY1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(labelY1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        cursorValueY1.setEditable(false);
        cursorValueY1.setForeground(new java.awt.Color(0, 255, 0));
        cursorValueY1.setAutoscrolls(false);
        cursorValueY1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueY1.setFocusable(false);
        cursorValueY1.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueY1.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueY1.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueY1.setRequestFocusEnabled(false);
        cursorValueY1.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueY1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, 20));

        labelY2.setFont(new java.awt.Font("Dialog", 0, 10));
        labelY2.setText("Y2:");
        labelY2.setEnabled(false);
        labelY2.setMaximumSize(new java.awt.Dimension(30, 25));
        labelY2.setMinimumSize(new java.awt.Dimension(30, 25));
        labelY2.setPreferredSize(new java.awt.Dimension(30, 25));
        labelY2.setRequestFocusEnabled(false);
        labelY2.setVerifyInputWhenFocusTarget(false);
        labelY2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(labelY2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, -1, -1));

        cursorValueY2.setEditable(false);
        cursorValueY2.setForeground(new java.awt.Color(0, 255, 0));
        cursorValueY2.setAutoscrolls(false);
        cursorValueY2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueY2.setFocusable(false);
        cursorValueY2.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueY2.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueY2.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueY2.setRequestFocusEnabled(false);
        cursorValueY2.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueY2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 140, -1, 20));

        cursorDeltaX.setEditable(false);
        cursorDeltaX.setForeground(new java.awt.Color(0, 255, 0));
        cursorDeltaX.setAutoscrolls(false);
        cursorDeltaX.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorDeltaX.setFocusable(false);
        cursorDeltaX.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorDeltaX.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorDeltaX.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorDeltaX.setRequestFocusEnabled(false);
        cursorDeltaX.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorDeltaX, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, 20));

        labelDeltaX.setFont(new java.awt.Font("Dialog", 0, 10));
        labelDeltaX.setText("\u0394X:");
        labelDeltaX.setEnabled(false);
        labelDeltaX.setMaximumSize(new java.awt.Dimension(30, 25));
        labelDeltaX.setMinimumSize(new java.awt.Dimension(30, 25));
        labelDeltaX.setPreferredSize(new java.awt.Dimension(30, 25));
        labelDeltaX.setRequestFocusEnabled(false);
        labelDeltaX.setVerifyInputWhenFocusTarget(false);
        labelDeltaX.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(labelDeltaX, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        labelDeltaY.setFont(new java.awt.Font("Dialog", 0, 10));
        labelDeltaY.setText("\u0394Y:");
        labelDeltaY.setEnabled(false);
        labelDeltaY.setMaximumSize(new java.awt.Dimension(30, 25));
        labelDeltaY.setMinimumSize(new java.awt.Dimension(30, 25));
        labelDeltaY.setPreferredSize(new java.awt.Dimension(30, 25));
        labelDeltaY.setRequestFocusEnabled(false);
        labelDeltaY.setVerifyInputWhenFocusTarget(false);
        labelDeltaY.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(labelDeltaY, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 170, -1, -1));

        cursorDeltaY.setEditable(false);
        cursorDeltaY.setForeground(new java.awt.Color(0, 255, 0));
        cursorDeltaY.setAutoscrolls(false);
        cursorDeltaY.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorDeltaY.setFocusable(false);
        cursorDeltaY.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorDeltaY.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorDeltaY.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorDeltaY.setRequestFocusEnabled(false);
        cursorDeltaY.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorDeltaY, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, 20));

        cursorPanel.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 140, 210));

        jPanel20.setBackground(new java.awt.Color(196, 204, 223));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Type"));
        jPanel20.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CursorNoneRadioButton.setBackground(new java.awt.Color(196, 204, 223));
        cursorButtonGroup.add(CursorNoneRadioButton);
        CursorNoneRadioButton.setFont(new java.awt.Font("Dialog", 0, 10));
        CursorNoneRadioButton.setSelected(true);
        CursorNoneRadioButton.setText("None");
        CursorNoneRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CursorNoneRadioButtonActionPerformed(evt);
            }
        });
        jPanel20.add(CursorNoneRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 15, 70, 20));

        CursorXRadioButton.setBackground(new java.awt.Color(196, 204, 223));
        cursorButtonGroup.add(CursorXRadioButton);
        CursorXRadioButton.setFont(new java.awt.Font("Dialog", 0, 10));
        CursorXRadioButton.setText("X");
        CursorXRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CursorXRadioButtonActionPerformed(evt);
            }
        });
        jPanel20.add(CursorXRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 15, 50, -1));

        CursorYRadioButton.setBackground(new java.awt.Color(196, 204, 223));
        cursorButtonGroup.add(CursorYRadioButton);
        CursorYRadioButton.setFont(new java.awt.Font("Dialog", 0, 10));
        CursorYRadioButton.setText("Y");
        CursorYRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CursorYRadioButtonActionPerformed(evt);
            }
        });
        jPanel20.add(CursorYRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 40, 61, -1));

        CursorXYRadioButton.setBackground(new java.awt.Color(196, 204, 223));
        cursorButtonGroup.add(CursorXYRadioButton);
        CursorXYRadioButton.setFont(new java.awt.Font("Dialog", 0, 10));
        CursorXYRadioButton.setText("XY");
        CursorXYRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CursorXYRadioButtonActionPerformed(evt);
            }
        });
        jPanel20.add(CursorXYRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 50, -1));

        cursorPanel.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 140, 70));

        jPanel5.setBackground(new java.awt.Color(196, 204, 223));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Tracking"));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel5.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel5.setText("Cursor 1");
        jPanel5.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(8, 15, 61, 29));

        jComboBox1.setFont(new java.awt.Font("SansSerif", 0, 10));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursorBind1ActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 20, 60, 20));

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel6.setText("Cursor 2");
        jPanel5.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 48, 61, 29));

        jComboBox2.setFont(new java.awt.Font("SansSerif", 0, 10));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursorBind2ActionPerformed(evt);
            }
        });
        jPanel5.add(jComboBox2, new org.netbeans.lib.awtextra.AbsoluteConstraints(67, 54, 60, 20));

        cursorPanel.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 140, 90));

        jTabbedPane1.addTab("Cursors", cursorPanel);

        triggerPanel.setBackground(new java.awt.Color(196, 204, 223));
        triggerPanel.setName(""); // NOI18N
        triggerPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        triggerModeComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerModeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Auto lvl", "Auto", "Normal", "Single", "TV" }));
        triggerModeComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerModeComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerModeComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerModeComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerModeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 10, 80, 20));

        jLabel7.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel7.setText("Mode");
        triggerPanel.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 13, -1, -1));

        jLabel8.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel8.setText("Source");
        triggerPanel.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 43, -1, -1));

        triggerSrcComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerSrcComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ch1", "Ch2", "Ch3", "Ch4", "Ext", "Line" }));
        triggerSrcComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerSrcComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerSrcComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerSrcComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerSrcComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerSrcComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 40, 80, 20));

        jLabel9.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel9.setText("Slope");
        triggerPanel.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 73, -1, -1));

        triggerLevelSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerLevelSpinner.setModel(new SpinnerNumberModel(0.0, -20.0, 20.0, 0.01));
        triggerLevelSpinner.setMaximumSize(new java.awt.Dimension(65, 20));
        triggerLevelSpinner.setMinimumSize(new java.awt.Dimension(65, 20));
        triggerLevelSpinner.setPreferredSize(new java.awt.Dimension(65, 20));
        triggerLevelSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triggerLevelSpinnerStateChanged(evt);
            }
        });
        triggerPanel.add(triggerLevelSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 280, 80, 20));
        triggerPanel.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 65, 140, 5));
        triggerPanel.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 35, 140, 5));
        triggerPanel.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 95, 140, 5));

        jLabel10.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel10.setText("Level");
        triggerPanel.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 283, -1, -1));

        triggerSlopeComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerSlopeComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Falling", "Rising" }));
        triggerSlopeComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerSlopeComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerSlopeComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerSlopeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerSlopeComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerSlopeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 70, 80, 20));

        jLabel11.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel11.setText("Coupling");
        triggerPanel.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 103, -1, -1));

        triggerCplComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerCplComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Dc", "Ac" }));
        triggerCplComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerCplComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerCplComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerCplComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerCplComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerCplComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 100, 80, 20));
        triggerPanel.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 125, 140, -1));

        jLabel12.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel12.setText("Reject");
        triggerPanel.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 133, -1, -1));

        triggerRejectComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerRejectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Off", "Lf", "Hf" }));
        triggerRejectComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerRejectComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerRejectComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerRejectComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerRejectComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerRejectComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 130, 80, 20));
        triggerPanel.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 155, 140, -1));
        triggerPanel.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 305, 140, -1));

        jLabel13.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel13.setText("Hold off");
        triggerPanel.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 313, -1, -1));

        triggerHoldoffSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerHoldoffSpinner.setModel(new SpinnerNumberModel(0.0, -20.0, 20.0, 0.01));
        triggerHoldoffSpinner.setMaximumSize(new java.awt.Dimension(65, 20));
        triggerHoldoffSpinner.setMinimumSize(new java.awt.Dimension(65, 20));
        triggerHoldoffSpinner.setPreferredSize(new java.awt.Dimension(65, 20));
        triggerHoldoffSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                triggerHoldoffSpinnerStateChanged(evt);
            }
        });
        triggerPanel.add(triggerHoldoffSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 310, 80, 20));

        triggerNoiseRejectComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerNoiseRejectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Off", "On" }));
        triggerNoiseRejectComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerNoiseRejectComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerNoiseRejectComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerNoiseRejectComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerNoiseRejectComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerNoiseRejectComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 160, 80, 20));

        jLabel14.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel14.setText("Noise rej.");
        triggerPanel.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 163, -1, -1));
        triggerPanel.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 185, 140, -1));

        jLabel15.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel15.setText("Polarity");
        triggerPanel.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 223, -1, -1));

        triggerPolarityComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerPolarityComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Positive", "Negative" }));
        triggerPolarityComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerPolarityComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerPolarityComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerPolarityComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerPolarityComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerPolarityComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 220, 80, 20));
        triggerPanel.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 215, 140, -1));

        jLabel16.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel16.setText("TV mode");
        triggerPanel.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 193, -1, -1));

        triggerTVModeComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerTVModeComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerTVModeComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerTVModeComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerTVModeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerTVModeComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerTVModeComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 190, 80, 20));
        triggerPanel.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 245, 140, -1));

        jLabel17.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel17.setText("TV HF rej.");
        triggerPanel.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 253, -1, -1));

        triggerTVHFRejectComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        triggerTVHFRejectComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Off", "On" }));
        triggerTVHFRejectComboBox.setMaximumSize(new java.awt.Dimension(65, 24));
        triggerTVHFRejectComboBox.setMinimumSize(new java.awt.Dimension(65, 24));
        triggerTVHFRejectComboBox.setPreferredSize(new java.awt.Dimension(65, 24));
        triggerTVHFRejectComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                triggerTVHFRejectComboBoxActionPerformed(evt);
            }
        });
        triggerPanel.add(triggerTVHFRejectComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(55, 250, 80, 20));
        triggerPanel.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 275, 140, -1));

        jTabbedPane1.addTab("Trigger", triggerPanel);

        jPanel11.setBackground(new java.awt.Color(196, 204, 223));

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(jList1);

        jPanel11.add(jScrollPane1);

        jTabbedPane1.addTab("Memory", jPanel11);

        jPanel12.setBackground(new java.awt.Color(196, 204, 223));
        jTabbedPane1.addTab("None", jPanel12);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.EAST);

        jMenuBar1.setBackground(new java.awt.Color(196, 204, 223));

        jMenu1.setBackground(new java.awt.Color(196, 204, 223));
        jMenu1.setText("File");

        OpenMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        OpenMenuItem.setText("Open...");
        OpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(OpenMenuItem);

        SaveMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        SaveMenuItem.setText("Save");
        SaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(SaveMenuItem);

        SaveAsMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        SaveAsMenuItem.setText("Save As...");
        SaveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveAsMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(SaveAsMenuItem);

        ExportMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        ExportMenuItem.setText("Export...");
        ExportMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExportMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(ExportMenuItem);
        jMenu1.add(jSeparator6);

        PrintMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        PrintMenuItem.setText("Print");
        PrintMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(PrintMenuItem);

        CloseMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        CloseMenuItem.setText("Close");
        CloseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(CloseMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(196, 204, 223));
        jMenu2.setText("Tools");

        OptionsMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        OptionsMenuItem.setText("Options");
        OptionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OptionsMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(OptionsMenuItem);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cursorBind2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cursorBind2ActionPerformed
        String selection = jComboBox2.getSelectedItem().toString();
        if (selection.equals("Ch1")) {
            scope.setCursorTrackingCh(PlotCursor.CURSOR_SELECT_X2, 0);
        } else {
            scope.setCursorTrackingCh(PlotCursor.CURSOR_SELECT_X2, 1);
        }        
}//GEN-LAST:event_cursorBind2ActionPerformed

    private void cursorBind1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cursorBind1ActionPerformed
        String selection = jComboBox1.getSelectedItem().toString();
        if (selection.equals("Ch1")) {
            scope.setCursorTrackingCh(PlotCursor.CURSOR_SELECT_X1, 0);
        } else {
            scope.setCursorTrackingCh(PlotCursor.CURSOR_SELECT_X1, 1);
        }        
}//GEN-LAST:event_cursorBind1ActionPerformed

    private void CursorXYRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CursorXYRadioButtonActionPerformed
        // XY cursors
        scope.setCursorMode(lib.gui.primitives.PlotCursor.CURSOR_XY);
        labelX1.setEnabled(true);
        labelX2.setEnabled(true);
        labelDeltaX.setEnabled(true);
        labelY1.setEnabled(true);
        labelY2.setEnabled(true);
        labelDeltaY.setEnabled(true);
}//GEN-LAST:event_CursorXYRadioButtonActionPerformed

    private void CursorYRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CursorYRadioButtonActionPerformed
        // Y cursors
        scope.setCursorMode(lib.gui.primitives.PlotCursor.CURSOR_Y);
        labelX1.setEnabled(false);
        labelX2.setEnabled(false);
        labelDeltaX.setEnabled(false);
        labelY1.setEnabled(true);
        labelY2.setEnabled(true);
        labelDeltaY.setEnabled(true);
}//GEN-LAST:event_CursorYRadioButtonActionPerformed

    private void CursorXRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CursorXRadioButtonActionPerformed
        // X cursors
        scope.setCursorMode(lib.gui.primitives.PlotCursor.CURSOR_X);
        labelX1.setEnabled(true);
        labelX2.setEnabled(true);
        labelDeltaX.setEnabled(true);
        labelY1.setEnabled(false);
        labelY2.setEnabled(false);
        labelDeltaY.setEnabled(false);
}//GEN-LAST:event_CursorXRadioButtonActionPerformed

    private void CursorNoneRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CursorNoneRadioButtonActionPerformed
        // no cursors
        scope.setCursorMode(lib.gui.primitives.PlotCursor.CURSOR_NONE);
        labelX1.setEnabled(false);
        labelX2.setEnabled(false);
        labelDeltaX.setEnabled(false);
        labelY1.setEnabled(false);
        labelY2.setEnabled(false);
        labelDeltaY.setEnabled(false);
}//GEN-LAST:event_CursorNoneRadioButtonActionPerformed

    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar1AdjustmentValueChanged
        // set memory offset
        scope.setMemoryOffset(jScrollBar1.getValue());
    }//GEN-LAST:event_jScrollBar1AdjustmentValueChanged

    private void timeScaleSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timeScaleSpinnerStateChanged
        // find out current time/div value
        int i = 0;
        for (i = 0; i < hScale.length; i++) {
            if (hScale[i].equals(timeScaleSpinner.getValue()) == true) {
                // set new one
                scope.setTimeScale(hScaleTime[i] * 10.0D);
                hScaler = hScalerTable[i];
                if (hScaler == 1000.0D) {
                    hScalerUnit = "ms";
                }
                if (hScaler == 1E6D) {
                    hScalerUnit = "us";
                }
                if (hScaler == 1E9D) {
                    hScalerUnit = "ns";
                }
                break;
            }
        }
        if (initDone) {
            // send selected value to device
            ScopeMsg.sendValue(MessageTimebaseRange, hScaleTime[i]);
        }
}//GEN-LAST:event_timeScaleSpinnerStateChanged

    private void CloseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseMenuItemActionPerformed
        // hide window when close
        setVisible(false);
}//GEN-LAST:event_CloseMenuItemActionPerformed

    private void triggerModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerModeComboBoxActionPerformed
        if (initDone) {
            // trigger mode        
            ScopeMsg.sendComboBoxSelection(MessageTriggerMode, triggerModeComboBox);
        }
}//GEN-LAST:event_triggerModeComboBoxActionPerformed

    private void triggerSrcComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerSrcComboBoxActionPerformed
        if (initDone) {
            // trigger source
            ScopeMsg.sendComboBoxSelection(MessageTriggerSource, triggerSrcComboBox);
        }
}//GEN-LAST:event_triggerSrcComboBoxActionPerformed

    private void triggerSlopeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerSlopeComboBoxActionPerformed
        if (initDone) {
            // trigger slope
            ScopeMsg.sendComboBoxSelection(MessageTriggerSlope, triggerSlopeComboBox);
        }
}//GEN-LAST:event_triggerSlopeComboBoxActionPerformed

    private void autoscaleModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_autoscaleModelButtonActionPerformed
        if (initDone) {
            // do autoscale
            ScopeMsg.sendString(MessageAutoscale);
        }
}//GEN-LAST:event_autoscaleModelButtonActionPerformed

    private void triggerCplComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerCplComboBoxActionPerformed
        if (initDone) {
            // trigger coupling
            ScopeMsg.sendComboBoxSelection(MessageTriggerCouple, triggerCplComboBox);
        }
}//GEN-LAST:event_triggerCplComboBoxActionPerformed

    private void triggerRejectComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerRejectComboBoxActionPerformed
        if (initDone) {
            // trigger reject
            ScopeMsg.sendComboBoxSelection(MessageTriggerReject, triggerRejectComboBox);
        }
}//GEN-LAST:event_triggerRejectComboBoxActionPerformed

    private void triggerLevelSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triggerLevelSpinnerStateChanged
        if (initDone) {
            // trigger level
            try {
                // get updated level
                double level = Double.parseDouble(triggerLevelSpinner.getValue().toString());
                ScopeMsg.sendValue(MessageTriggerLevel, level);
            } catch (NumberFormatException ex) {
                logger.error("failed to parse trigger level");
            }
        }
}//GEN-LAST:event_triggerLevelSpinnerStateChanged

    private void PrintMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintMenuItemActionPerformed
        // TODO add your handling code here:
        lib.common.utilities.PrintUtilities.printComponent(this);
    }//GEN-LAST:event_PrintMenuItemActionPerformed

    private void runModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_runModelButtonActionPerformed
        // run / stop states
        cmdId = capture(runModelButton.isSelected(), cmdId);   
    }//GEN-LAST:event_runModelButtonActionPerformed

    private void OptionsMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OptionsMenuItem5ActionPerformed
        // create editor if not already exists
        /*if (options == null) {
        options = new HP54600Options(this);
        }
        addToDesktop(options);
         */
        addToDesktop(new HP54600Options(this));
    }//GEN-LAST:event_OptionsMenuItem5ActionPerformed

    private void OpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenMenuItemActionPerformed
        // save setting to named file
        AdvancedFileChooser jfc = new AdvancedFileChooser();

        if (currentDir != null) {
            jfc.setCurrentDirectory(currentDir);
        }

        int result = jfc.showOpenDialog(this);

        // check return value
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        // get user selected file
        //settingsFile = jfc.getSelectedFile().toString();
        XmlReader r = new XmlReader(jfc.getSelectedFile());

        r.parse(ScopeMsg.getName());
    }//GEN-LAST:event_OpenMenuItemActionPerformed

    private void SaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuItemActionPerformed
        // save settings to existing file
        if (!settingsFile.isEmpty()) {
            return;
        }

        SaveAsMenuItemActionPerformed(evt);
    }//GEN-LAST:event_SaveMenuItemActionPerformed

    private void SaveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveAsMenuItemActionPerformed
        // save setting to named file
        AdvancedFileChooser jfc = new AdvancedFileChooser();

        if (currentDir != null) {
            jfc.setCurrentDirectory(currentDir);
        }

        int result = jfc.showSaveDialog(this);

        // check return value
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        try {
            // get current path
            currentDir = jfc.getSelectedFile().getCanonicalFile();
        } catch (IOException ex) {
            logger.error("failed to parse current directory " + ex.getMessage());
        }

        // get user selected file
        settingsFile = jfc.getSelectedFile().toString();    
    }//GEN-LAST:event_SaveAsMenuItemActionPerformed

    private void ExportMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExportMenuItemActionPerformed
    // TODO add your handling code here:
    }//GEN-LAST:event_ExportMenuItemActionPerformed

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
    // TODO add your handling code here:
    }//GEN-LAST:event_jSpinner1StateChanged

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        System.out.println("size " + this.getSize().width + " " + this.getSize().height);
    }//GEN-LAST:event_formComponentResized

    private void triggerHoldoffSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_triggerHoldoffSpinnerStateChanged
        if (initDone) {
            // trigger hold off
            try {
                // get updated hold off
                double level = Double.parseDouble(triggerHoldoffSpinner.getValue().toString());
                ScopeMsg.sendValue(MessageTriggerHoldOff, level);
            } catch (NumberFormatException ex) {
                logger.error("failed to parse trigger hold off");
            }
        }
}//GEN-LAST:event_triggerHoldoffSpinnerStateChanged

    private void triggerNoiseRejectComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerNoiseRejectComboBoxActionPerformed
        if (initDone) {
            // trigger noise reject
            ScopeMsg.sendComboBoxSelection(MessageTriggerNoiseReject, triggerNoiseRejectComboBox);
        }
}//GEN-LAST:event_triggerNoiseRejectComboBoxActionPerformed

    private void triggerPolarityComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerPolarityComboBoxActionPerformed
        if (initDone) {
            // trigger polarity
            ScopeMsg.sendComboBoxSelection(MessageTriggerPolarity, triggerPolarityComboBox);
        }
}//GEN-LAST:event_triggerPolarityComboBoxActionPerformed

    private void triggerTVModeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerTVModeComboBoxActionPerformed
        if (initDone) {
            // trigger tv mode select
            ScopeMsg.sendComboBoxSelection(MessageTriggerTVMode, triggerTVModeComboBox);
        }
}//GEN-LAST:event_triggerTVModeComboBoxActionPerformed

    private void triggerTVHFRejectComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_triggerTVHFRejectComboBoxActionPerformed
        if (initDone) {
            // trigger tv mode HF reject
            ScopeMsg.sendComboBoxSelection(MessageTriggerTVHFReject, triggerTVHFRejectComboBox);
        }
}//GEN-LAST:event_triggerTVHFRejectComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CloseMenuItem;
    private javax.swing.JRadioButton CursorNoneRadioButton;
    private javax.swing.JRadioButton CursorXRadioButton;
    private javax.swing.JRadioButton CursorXYRadioButton;
    private javax.swing.JRadioButton CursorYRadioButton;
    private javax.swing.JMenuItem ExportMenuItem;
    private javax.swing.JMenuItem OpenMenuItem;
    private javax.swing.JMenuItem OptionsMenuItem;
    private javax.swing.JMenuItem PrintMenuItem;
    private javax.swing.JMenuItem SaveAsMenuItem;
    private javax.swing.JMenuItem SaveMenuItem;
    private lib.ui.buttons.ModelButton autoscaleModelButton;
    private javax.swing.JPanel controlsPanel;
    private javax.swing.ButtonGroup cursorButtonGroup;
    private javax.swing.JTextField cursorDeltaX;
    private javax.swing.JTextField cursorDeltaY;
    private javax.swing.JPanel cursorPanel;
    private javax.swing.JTextField cursorValueX1;
    private javax.swing.JTextField cursorValueX2;
    private javax.swing.JTextField cursorValueY1;
    private javax.swing.JTextField cursorValueY2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel labelDeltaX;
    private javax.swing.JLabel labelDeltaY;
    private javax.swing.JLabel labelX1;
    private javax.swing.JLabel labelX2;
    private javax.swing.JLabel labelY1;
    private javax.swing.JLabel labelY2;
    private lib.ui.buttons.ModelButton runModelButton;
    private javax.swing.JSpinner timeScaleSpinner;
    private javax.swing.JComboBox triggerCplComboBox;
    private javax.swing.JSpinner triggerHoldoffSpinner;
    private javax.swing.JSpinner triggerLevelSpinner;
    private javax.swing.JComboBox triggerModeComboBox;
    private javax.swing.JComboBox triggerNoiseRejectComboBox;
    private javax.swing.JPanel triggerPanel;
    private javax.swing.JComboBox triggerPolarityComboBox;
    private javax.swing.JComboBox triggerRejectComboBox;
    private javax.swing.JComboBox triggerSlopeComboBox;
    private javax.swing.JComboBox triggerSrcComboBox;
    private javax.swing.JComboBox triggerTVHFRejectComboBox;
    private javax.swing.JComboBox triggerTVModeComboBox;
    // End of variables declaration//GEN-END:variables
}
