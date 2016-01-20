/***********************************************************
 * Software: instrument client
 * Module:   Agilent E4421 instrument class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 30.1.2013
 *
 ***********************************************************/
package instruments.agilent4421b;

import components.*;
import interfaces.MessageCallbackInterface;
import interfaces.MessageInterface;
import interfaces.NumericControlInterface;
import interfaces.sweepContolBoxInterface;
import java.awt.Color;
import java.util.Hashtable;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import oh3ebf.lib.ui.buttons.AdvancedButtonGroup;
import oh3ebf.lib.ui.buttons.ModelButton;
import org.apache.log4j.Logger;
import yami.ParamSet;

public class E4421B extends javax.swing.JInternalFrame implements MessageCallbackInterface,
        NumericControlInterface, sweepContolBoxInterface {

    private static Logger logger;
    private DeviceMessage message;
    private static Hashtable<String, String> SweepItems;
    private double frequency = 3000000.0D;
    private String frequencyUnits[] = {"Hz", "kHz", "MHz"};
    private String waveforms[] = {"Sine", "Triangle", "Square wave", "Ramp", "Noise", "Dual sine", "Swept sine"};
    private String source[] = {"Internal", "Ext 1 DC", "Ext 1 AC", "Ext 2 DC", "Ext 2 AC"};
    private NumericInputControl frequencyControl;
    private double amplitude = -131.0D;
    private String amlitudeUnits[] = {"dBm"};
    private NumericInputControl amplitudeControl;
    private AdvancedButtonGroup ad;
    private sweepControlBox sweep;
    private int mode = 0;

    /** Creates new form Agilent E4421B */
    public E4421B(String name, Hashtable<String, String> properties, MessageInterface msg) {
        // get logger instance for this class
        logger = Logger.getLogger(E4421B.class);
        // save messaging context        
        message = new DeviceMessage(name, msg);
        // add message listener
        message.addMessageCallback(name, this);

        SweepItems = new Hashtable<String, String>() {

            {
                put("Frequency", "Hz");
                put("Amplitude", "dBm");
            }
        };

        // read initial frequency
        message.sendString(E4421Constants.MessageReadFrequency);
        frequency = message.readIntValue(0);

        // read initial amplitude
        message.sendString(E4421Constants.MessageReadAmplitude);
        amplitude = message.readDoubleValue(0);

        initComponents();

        // add modulation buttons
        ad = new AdvancedButtonGroup();
        ad.add(AMmodulationButton1);
        ad.add(AMmodulationButton2);
        ad.add(FMModulationButton1);
        ad.add(FMModulationButton2);
        ad.add(PhaseModulationButton1);
        ad.add(PhaseModulationButton2);

        // add sweep control to ui
        sweep = new sweepControlBox(SweepItems, SweepItems, this, false);
        sweep.setBgColor(new Color(196, 204, 223));
        jTabbedPane1.addTab("Sweep", sweep);

        // add frequency input control
        frequencyControl = new NumericInputControl("Frequency", 0, frequencyUnits, "##########.####",
                E4421Constants.frequencyMin, E4421Constants.frequencyMax, new frequencyScaler(), this);
        frequencyControl.setValue(frequency);
        jPanel1.add(frequencyControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 240, 70));

        // add amplitude input control
        amplitudeControl = new NumericInputControl("Amplitude", 1, amlitudeUnits, "####.00",
                E4421Constants.amplitudeMin, E4421Constants.amplitudeMax, new decibelScaler(), this);
        amplitudeControl.setValue(amplitude);
        jPanel1.add(amplitudeControl, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 270, 70));

        setSize(680, 390);

        setSpinnerUnits(AM1depthSpinner, "##.#", "%");
        setSpinnerUnits(AM2depthSpinner, "##.#", "%");
        setSpinnerUnits(FM1depthSpinner, "#####.#", "MHz");
        setSpinnerUnits(FM2depthSpinner, "#####.#", "MHz");
        setSpinnerUnits(PM1depthSpinner, "##.#", "rad");
        setSpinnerUnits(PM2depthSpinner, "##.#", "rad");
    }

    /** Function sends message
     *
     * @param param message parameters
     *
     * @return true/false
     *
     */
    public boolean DataStreamCallback(ParamSet param) {
        return (true);
    }

    /** Function implements callback handler for number control inputs
     * 
     * @param id of control
     * @param d current value of control
     */
    public void UpdateValue(int id, double d) {
        switch (id) {
            case E4421Constants.frequencyIndex:
                frequency = d;
                message.sendValue(E4421Constants.MessageFrequency, frequency);
                break;
            case E4421Constants.amplitudeIndex:
                amplitude = d;
                message.sendValue(E4421Constants.MessageAmplitude, amplitude);
                break;
            default:
                logger.warn("progress bar " + id + " does'n exists");
        }
    }

    /** Function implements callback from sweep control ui 
     * 
     * @param mode sweep mode
     * @param mainStart main sweep start
     * @param mainEnd main sweep end
     * @param mainStep main sweep step
     * @param nestedStart nested sweep start
     * @param nestedEnd nested sweep end
     * @param nestedStep nested sweep step
     */
    public String sweepValues(String id, boolean state, int cycle,
            double mainStart, double mainEnd, double mainStep,
            double nestedStart, double nestedEnd, double nestedStep) {

        int sweep_mode = E4421Constants.SWEEP_STOP;
        ParamSet PsetMsg = new ParamSet();

        if (!state) {
            // stop measurement             
            PsetMsg.append(DeviceMessage.MessageMeasurementStop);
            PsetMsg.append((byte) DeviceMessage.MeasurementCmdSweep);
            PsetMsg.append(id);

            message.commandMessage(PsetMsg);
            return ("");
        }

        // select sweep mode
        if (sweep.isNestedSweep()) {
            if (sweep.getNestedSweep().equals("Amplitude")) {
                sweep_mode = E4421Constants.SWEEP_AMPLITUDE_NESTED_FREQUENCY;
            } else {
                sweep_mode = E4421Constants.SWEEP_FREQUENCY_NESTED_AMPLITUDE;
            }
        } else {
            if (sweep.getMainSweep().equals("Frequency")) {
                sweep_mode = E4421Constants.SWEEP_FREQUENCY;
            } else {
                sweep_mode = E4421Constants.SWEEP_AMPLITUDE;
            }
        }

        // command
        PsetMsg.append(DeviceMessage.MessageMeasurementStart);
        PsetMsg.append((byte) DeviceMessage.MeasurementCmdSweep);
        PsetMsg.append((byte) 0x0);          // no channel info
        PsetMsg.append(1);                   // boolean inteval in use
        PsetMsg.append(1);                   // boolean measurement enabled
        PsetMsg.append(cycle);               // interval time

        PsetMsg.append((byte) sweep_mode);    // sweep mode
        PsetMsg.append(mainStart);           // frequency start
        PsetMsg.append(mainEnd);             // frequency end
        PsetMsg.append(mainStep);            // frequency step

        PsetMsg.append(nestedStart);         // amplitude start
        PsetMsg.append(nestedEnd);           // amplitude end
        PsetMsg.append(nestedStep);          // amplitude step

        if (message.commandMessage(PsetMsg)) {
            // read command id
            return (message.getUniqueId());
        }

        return ("");
    }

    /** Function sends combobox selection 
     * 
     * @param msg
     * @param modulation selection
     * @param path of modulation
     * @param selection of combobox
     */
    public void comboBoxMessage(String msg, int modulation, int path, int selection) {
        ParamSet PsetMsg = new ParamSet();

        // set parameters             
        PsetMsg.append(msg);
        PsetMsg.append((byte) modulation);
        PsetMsg.append((byte) path);
        PsetMsg.append((byte) selection);

        message.commandMessage(PsetMsg);
    }

    /** Function sends double value to device
     * 
     * @param msg
     * @param modulation selected
     * @param path of modulation 
     * @param value to set
     */
    public void spinnerMessage(String msg, int modulation, int path, double value) {
        ParamSet PsetMsg = new ParamSet();

        // set parameters       
        PsetMsg.append(msg);
        PsetMsg.append((byte) modulation);
        PsetMsg.append((byte) path);
        PsetMsg.append(value);

        message.commandMessage(PsetMsg);
    }

    /** Function restores setting from file
     * 
     * @param r reference to xml file reader
     */
    public void loadSettings(XmlReader r) {
        r.parse(message.getName());

        amplitude = r.getDouble(E4421Constants.PARAM_AMPLITUDE);
        amplitudeControl.setValue(amplitude);
        frequency = r.getDouble(E4421Constants.PARAM_FREQUENCY);
        frequencyControl.setValue(frequency);

        sweep.setTimerSpeed(r.getInteger(E4421Constants.PARAM_TIMER_STEP));
        sweep.setMainSweepIndex(r.getInteger(E4421Constants.PARAM_MAIN_SWEEP));
        sweep.setMainStart(r.getDouble(E4421Constants.PARAM_MAIN_SWEEP_START));
        sweep.setMainEnd(r.getDouble(E4421Constants.PARAM_MAIN_SWEEP_END));
        sweep.setMainStep(r.getDouble(E4421Constants.PARAM_MAIN_SWEEP_STEP));
        sweep.setNestedSweepIndex(r.getInteger(E4421Constants.PARAM_NESTED_SWEEP));
        sweep.setNestedSweepState(r.getBoolean(E4421Constants.PARAM_NESTED_SWEEP_ENABLED));
        sweep.setNestedStart(r.getDouble(E4421Constants.PARAM_NESTED_SWEEP_START));
        sweep.setNestedEnd(r.getDouble(E4421Constants.PARAM_NESTED_SWEEP_END));
        sweep.setNestedStep(r.getDouble(E4421Constants.PARAM_NESTED_SWEEP_STEP));
    }

    /** Function stores scope setting to file
     * 
     * @param file to save
     */
    public void saveSettings(XmlWriter w) {
        w.addConficSection(message.getName());

        // save parameters
        w.addParamElement(E4421Constants.PARAM_AMPLITUDE, "", Double.toString(amplitude));
        w.addParamElement(E4421Constants.PARAM_FREQUENCY, "", Double.toString(frequency));
        w.addParamElement(E4421Constants.PARAM_TIMER_STEP, "", Integer.toString(sweep.getTimerSpeed()));
        w.addParamElement(E4421Constants.PARAM_MAIN_SWEEP, "", Integer.toString(sweep.getMainSweepIndex()));
        w.addParamElement(E4421Constants.PARAM_MAIN_SWEEP_START, "", Double.toString(sweep.getMainStart()));
        w.addParamElement(E4421Constants.PARAM_MAIN_SWEEP_END, "", Double.toString(sweep.getMainEnd()));
        w.addParamElement(E4421Constants.PARAM_MAIN_SWEEP_STEP, "", Double.toString(sweep.getMainStep()));
        w.addParamElement(E4421Constants.PARAM_NESTED_SWEEP, "", Integer.toString(sweep.getNestedSweepIndex()));
        w.addParamElement(E4421Constants.PARAM_NESTED_SWEEP_ENABLED, "", Boolean.toString(sweep.isNestedSweep()));
        w.addParamElement(E4421Constants.PARAM_NESTED_SWEEP_START, "", Double.toString(sweep.getNestedStart()));
        w.addParamElement(E4421Constants.PARAM_NESTED_SWEEP_END, "", Double.toString(sweep.getNestedEnd()));
        w.addParamElement(E4421Constants.PARAM_NESTED_SWEEP_STEP, "", Double.toString(sweep.getNestedStep()));

    }

    private void setSpinnerUnits(JSpinner js, String format, String unit) {
        JFormattedTextField f = ((JSpinner.DefaultEditor) js.getEditor()).getTextField();
        f.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.NumberFormatter(
                new java.text.DecimalFormat(format + unit))));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        UnitPopupMenu = new javax.swing.JPopupMenu();
        MHzMenuItem = new javax.swing.JRadioButtonMenuItem();
        kHzMenuItem = new javax.swing.JRadioButtonMenuItem();
        UnitbuttonGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        rfcontrolButton = new ModelButton("RF OUT", "RF OUT");
        modulationControlButton = new ModelButton("MOD", "MOD");
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        AMmodulationButton1 = new ModelButton("AM 1", "AM 1");
        AMmodulationButton2 = new ModelButton("AM 2", "AM 2");
        FMModulationButton1 = new ModelButton("FM 1", "FM 1");
        FMModulationButton2 = new ModelButton("FM 2", "FM 2");
        PhaseModulationButton1 = new ModelButton("\u03a6M 1","\u03a6M 1");
        PhaseModulationButton2 = new ModelButton("\u03a6M 2","\u03a6M 2");
        jPanel2 = new javax.swing.JPanel();
        AM1depthSpinner = new javax.swing.JSpinner();
        AM1sourceComboBox = new javax.swing.JComboBox();
        AM1waveformComboBox = new javax.swing.JComboBox();
        AM1rateSpinner = new javax.swing.JSpinner();
        AM2rateSpinner = new javax.swing.JSpinner();
        AM2waveformComboBox = new javax.swing.JComboBox();
        AM2sourceComboBox = new javax.swing.JComboBox();
        AM2depthSpinner = new javax.swing.JSpinner();
        FM1depthSpinner = new javax.swing.JSpinner();
        FM1sourceComboBox = new javax.swing.JComboBox();
        FM1waveformComboBox = new javax.swing.JComboBox();
        FM1rateSpinner = new javax.swing.JSpinner();
        FM2rateSpinner = new javax.swing.JSpinner();
        FM2waveformComboBox = new javax.swing.JComboBox();
        FM2sourceComboBox = new javax.swing.JComboBox();
        FM2depthSpinner = new javax.swing.JSpinner();
        PM1depthSpinner = new javax.swing.JSpinner();
        PM1sourceComboBox = new javax.swing.JComboBox();
        PM1waveformComboBox = new javax.swing.JComboBox();
        PM1rateSpinner = new javax.swing.JSpinner();
        PM2rateSpinner = new javax.swing.JSpinner();
        PM2waveformComboBox = new javax.swing.JComboBox();
        PM2sourceComboBox = new javax.swing.JComboBox();
        PM2depthSpinner = new javax.swing.JSpinner();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        OptionsMenu = new javax.swing.JMenu();

        UnitbuttonGroup.add(MHzMenuItem);
        MHzMenuItem.setSelected(true);
        MHzMenuItem.setText("MHz");
        MHzMenuItem.setToolTipText("Value in MHz");
        UnitPopupMenu.add(MHzMenuItem);

        UnitbuttonGroup.add(kHzMenuItem);
        kHzMenuItem.setSelected(true);
        kHzMenuItem.setText("kHz");
        UnitPopupMenu.add(kHzMenuItem);

        setBackground(new java.awt.Color(196, 204, 223));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Agilent 4421B");
        setVisible(true);

        jPanel1.setBackground(new java.awt.Color(196, 204, 223));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rfcontrolButton.setText("RF OUT");
        rfcontrolButton.setColorModel(ModelButton.ModelNormal);
        rfcontrolButton.setPreferredSize(new java.awt.Dimension(75, 30));
        rfcontrolButton.setToggleMode(true);
        rfcontrolButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rfcontrolButtonActionPerformed(evt);
            }
        });
        jPanel1.add(rfcontrolButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 10, 75, 30));

        modulationControlButton.setColorModel(ModelButton.ModelNormal);
        modulationControlButton.setPreferredSize(new java.awt.Dimension(75, 30));
        modulationControlButton.setToggleMode(true);
        modulationControlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulationControlButtonActionPerformed(evt);
            }
        });
        jPanel1.add(modulationControlButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 50, 75, 30));

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jTabbedPane1.setBackground(new java.awt.Color(196, 204, 223));

        jPanel4.setBackground(new java.awt.Color(196, 204, 223));
        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel5.setBackground(new java.awt.Color(196, 204, 223));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setMinimumSize(new java.awt.Dimension(61, 50));
        jPanel5.setPreferredSize(new java.awt.Dimension(80, 100));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AMmodulationButton1.setText("AM 1");
        AMmodulationButton1.setColorModel(ModelButton.ModelNormal);
        AMmodulationButton1.setPreferredSize(new java.awt.Dimension(60, 30));
        AMmodulationButton1.setToggleMode(true);
        AMmodulationButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulationSelectButtonActionPerformed(evt);
            }
        });
        jPanel5.add(AMmodulationButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 15, -1, -1));

        AMmodulationButton2.setText("AM 2");
        AMmodulationButton2.setColorModel(ModelButton.ModelNormal);
        AMmodulationButton2.setMinimumSize(new java.awt.Dimension(60, 30));
        AMmodulationButton2.setPreferredSize(new java.awt.Dimension(60, 30));
        AMmodulationButton2.setToggleMode(true);
        AMmodulationButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulationSelectButtonActionPerformed(evt);
            }
        });
        jPanel5.add(AMmodulationButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        FMModulationButton1.setText("FM 1");
        FMModulationButton1.setColorModel(ModelButton.ModelNormal);
        FMModulationButton1.setPreferredSize(new java.awt.Dimension(60, 30));
        FMModulationButton1.setToggleMode(true);
        FMModulationButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulationSelectButtonActionPerformed(evt);
            }
        });
        jPanel5.add(FMModulationButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 85, -1, -1));

        FMModulationButton2.setText("FM 2");
        FMModulationButton2.setColorModel(ModelButton.ModelNormal);
        FMModulationButton2.setPreferredSize(new java.awt.Dimension(60, 30));
        FMModulationButton2.setToggleMode(true);
        FMModulationButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulationSelectButtonActionPerformed(evt);
            }
        });
        jPanel5.add(FMModulationButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, -1, -1));

        PhaseModulationButton1.setColorModel(ModelButton.ModelNormal);
        PhaseModulationButton1.setPreferredSize(new java.awt.Dimension(60, 30));
        PhaseModulationButton1.setToggleMode(true);
        PhaseModulationButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulationSelectButtonActionPerformed(evt);
            }
        });
        jPanel5.add(PhaseModulationButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 155, -1, -1));

        PhaseModulationButton2.setColorModel(ModelButton.ModelNormal);
        PhaseModulationButton2.setPreferredSize(new java.awt.Dimension(60, 30));
        PhaseModulationButton2.setToggleMode(true);
        PhaseModulationButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modulationSelectButtonActionPerformed(evt);
            }
        });
        jPanel5.add(PhaseModulationButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 190, -1, -1));

        jPanel4.add(jPanel5, java.awt.BorderLayout.WEST);

        jPanel2.setBackground(new java.awt.Color(196, 204, 223));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        AM1depthSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM1depthSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.1d, 100.0d, 0.1d));
        AM1depthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AM1depthSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(AM1depthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 20, 100, -1));

        AM1sourceComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM1sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(source));
        AM1sourceComboBox.setMinimumSize(new java.awt.Dimension(76, 18));
        AM1sourceComboBox.setPreferredSize(new java.awt.Dimension(76, 18));
        AM1sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AM1sourceComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(AM1sourceComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, 100, -1));

        AM1waveformComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM1waveformComboBox.setModel(new javax.swing.DefaultComboBoxModel(waveforms));
        AM1waveformComboBox.setMinimumSize(new java.awt.Dimension(101, 18));
        AM1waveformComboBox.setPreferredSize(new java.awt.Dimension(101, 18));
        AM1waveformComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AM1waveformComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(AM1waveformComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 100, -1));

        AM1rateSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM1rateSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        AM1rateSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AM1rateSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(AM1rateSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 100, -1));

        AM2rateSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM2rateSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        AM2rateSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AM2rateSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(AM2rateSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 57, 100, -1));

        AM2waveformComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM2waveformComboBox.setModel(new javax.swing.DefaultComboBoxModel(waveforms));
        AM2waveformComboBox.setMinimumSize(new java.awt.Dimension(101, 18));
        AM2waveformComboBox.setPreferredSize(new java.awt.Dimension(101, 18));
        AM2waveformComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AM2waveformComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(AM2waveformComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 57, 100, -1));

        AM2sourceComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM2sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(source));
        AM2sourceComboBox.setMinimumSize(new java.awt.Dimension(76, 18));
        AM2sourceComboBox.setPreferredSize(new java.awt.Dimension(76, 18));
        AM2sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AM2sourceComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(AM2sourceComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 57, 100, -1));

        AM2depthSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        AM2depthSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.1d, 100.0d, 0.1d));
        AM2depthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                AM2depthSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(AM2depthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 57, 100, -1));

        FM1depthSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM1depthSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.1d, 100.0d, 0.1d));
        FM1depthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FM1depthSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(FM1depthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 92, 100, -1));

        FM1sourceComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM1sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(source));
        FM1sourceComboBox.setMinimumSize(new java.awt.Dimension(76, 18));
        FM1sourceComboBox.setPreferredSize(new java.awt.Dimension(76, 18));
        FM1sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FM1sourceComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(FM1sourceComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 92, 100, -1));

        FM1waveformComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM1waveformComboBox.setModel(new javax.swing.DefaultComboBoxModel(waveforms));
        FM1waveformComboBox.setMinimumSize(new java.awt.Dimension(101, 18));
        FM1waveformComboBox.setPreferredSize(new java.awt.Dimension(101, 18));
        FM1waveformComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FM1waveformComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(FM1waveformComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 92, 100, -1));

        FM1rateSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM1rateSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        FM1rateSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FM1rateSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(FM1rateSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 92, 100, -1));

        FM2rateSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM2rateSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        FM2rateSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FM2rateSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(FM2rateSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 125, 100, -1));

        FM2waveformComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM2waveformComboBox.setModel(new javax.swing.DefaultComboBoxModel(waveforms));
        FM2waveformComboBox.setMinimumSize(new java.awt.Dimension(101, 18));
        FM2waveformComboBox.setPreferredSize(new java.awt.Dimension(101, 18));
        FM2waveformComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FM2waveformComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(FM2waveformComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 125, 100, -1));

        FM2sourceComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM2sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(source));
        FM2sourceComboBox.setMinimumSize(new java.awt.Dimension(76, 18));
        FM2sourceComboBox.setPreferredSize(new java.awt.Dimension(76, 18));
        FM2sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FM2sourceComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(FM2sourceComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 125, 100, -1));

        FM2depthSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        FM2depthSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.1d, 100.0d, 0.1d));
        FM2depthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                FM2depthSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(FM2depthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 125, 100, -1));

        PM1depthSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM1depthSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.1d, 100.0d, 0.1d));
        PM1depthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PM1depthSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(PM1depthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 162, 100, -1));

        PM1sourceComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM1sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(source));
        PM1sourceComboBox.setMinimumSize(new java.awt.Dimension(76, 18));
        PM1sourceComboBox.setPreferredSize(new java.awt.Dimension(76, 18));
        PM1sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PM1sourceComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(PM1sourceComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 162, 100, -1));

        PM1waveformComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM1waveformComboBox.setModel(new javax.swing.DefaultComboBoxModel(waveforms));
        PM1waveformComboBox.setMinimumSize(new java.awt.Dimension(101, 18));
        PM1waveformComboBox.setPreferredSize(new java.awt.Dimension(101, 18));
        PM1waveformComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PM1waveformComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(PM1waveformComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 162, 100, -1));

        PM1rateSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM1rateSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        PM1rateSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PM1rateSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(PM1rateSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 162, 100, -1));

        PM2rateSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM2rateSpinner.setModel(new javax.swing.SpinnerNumberModel(Double.valueOf(0.0d), null, null, Double.valueOf(1.0d)));
        PM2rateSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PM2rateSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(PM2rateSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 197, 100, -1));

        PM2waveformComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM2waveformComboBox.setModel(new javax.swing.DefaultComboBoxModel(waveforms));
        PM2waveformComboBox.setMinimumSize(new java.awt.Dimension(101, 18));
        PM2waveformComboBox.setPreferredSize(new java.awt.Dimension(101, 18));
        PM2waveformComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PM2waveformComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(PM2waveformComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 197, 100, -1));

        PM2sourceComboBox.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM2sourceComboBox.setModel(new javax.swing.DefaultComboBoxModel(source));
        PM2sourceComboBox.setMinimumSize(new java.awt.Dimension(76, 18));
        PM2sourceComboBox.setPreferredSize(new java.awt.Dimension(76, 18));
        PM2sourceComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PM2sourceComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(PM2sourceComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 197, 100, -1));

        PM2depthSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        PM2depthSpinner.setModel(new javax.swing.SpinnerNumberModel(0.1d, 0.1d, 100.0d, 0.1d));
        PM2depthSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                PM2depthSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(PM2depthSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 197, 100, -1));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel1.setText("Depth/deviation:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, -1, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel2.setText("Source:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 0, -1, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel3.setText("Rate:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, -1, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel4.setText("Waveform:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 0, -1, -1));

        jPanel4.add(jPanel2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("Modulation", jPanel4);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(196, 204, 223));

        FileMenu.setText("File");
        FileMenu.setEnabled(false);
        jMenuBar1.add(FileMenu);

        OptionsMenu.setText("Edit");
        OptionsMenu.setEnabled(false);
        jMenuBar1.add(OptionsMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void modulationSelectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modulationSelectButtonActionPerformed

        String action = evt.getActionCommand();
    /*
    // settings for AM 1 modulation
    if (action.equals("AM 1")) {
    mode = E4421Constants.MODE_AM_1;
    setSpinnerUnits(AM1depthSpinner, "##.#", "%");
    }
    // settings for AM 1 modulation
    if (action.equals("AM 2")) {
    mode = E4421Constants.MODE_AM_2;
    setSpinnerUnits(AM1depthSpinner, "##.#", "%");
    }
    // settings for FM 1 modulation
    if (action.equals("FM 1")) {
    mode = E4421Constants.MODE_FM_1;
    setSpinnerUnits(AM1depthSpinner, "#####.#", "MHz");
    }
    // settings for FM 2 modulation
    if (action.equals("FM 2")) {
    mode = E4421Constants.MODE_FM_2;
    setSpinnerUnits(AM1depthSpinner, "#####.#", "MHz");
    }
    // settings for Fii modulation
    if (action.equals("\u03a6M 1")) {
    mode = E4421Constants.MODE_M_1;
    setSpinnerUnits(AM1depthSpinner, "##.#", "rad");
    }
    // settings for Fii modulation
    if (action.equals("\u03a6M 2")) {
    mode = E4421Constants.MODE_M_2;
    setSpinnerUnits(AM1depthSpinner, "##.#", "rad");
    }
    // send selected modulation to device
    message.sendValue(E4421Constants.MessageModulationState, mode, ((ModelButton) evt.getSource()).isSelected());
     * */
}//GEN-LAST:event_modulationSelectButtonActionPerformed

    private void rfcontrolButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rfcontrolButtonActionPerformed
        // set rf output state
        message.sendValue(E4421Constants.MessageOutputSate, rfcontrolButton.isSelected());
}//GEN-LAST:event_rfcontrolButtonActionPerformed

    private void modulationControlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modulationControlButtonActionPerformed
        // set modulation state
        message.sendValue(E4421Constants.MessageModulationOutput, modulationControlButton.isSelected());
}//GEN-LAST:event_modulationControlButtonActionPerformed

    private void AM1waveformComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AM1waveformComboBoxActionPerformed
        // select waveform for modulation                
        comboBoxMessage(E4421Constants.MessageModulationWaveform,
                0, 1, AM1waveformComboBox.getSelectedIndex());
}//GEN-LAST:event_AM1waveformComboBoxActionPerformed

    private void AM1sourceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AM1sourceComboBoxActionPerformed
        // select waveform for modulation                
        comboBoxMessage(E4421Constants.MessageModulationSource,
                0, 1, AM1sourceComboBox.getSelectedIndex());
}//GEN-LAST:event_AM1sourceComboBoxActionPerformed

    private void AM1depthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AM1depthSpinnerStateChanged
        // set depth / deviation value
        spinnerMessage(E4421Constants.MessageModulationValue, 0, 1, (Double) AM1depthSpinner.getValue());
}//GEN-LAST:event_AM1depthSpinnerStateChanged

    private void AM1rateSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AM1rateSpinnerStateChanged
            // set modulation rate value
        spinnerMessage(E4421Constants.MessageModulationRate, 0, 1, (Double) AM1rateSpinner.getValue());
}//GEN-LAST:event_AM1rateSpinnerStateChanged

    private void AM2depthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AM2depthSpinnerStateChanged
        // set depth / deviation value
        spinnerMessage(E4421Constants.MessageModulationValue, 0, 2, (Double) AM2depthSpinner.getValue());
}//GEN-LAST:event_AM2depthSpinnerStateChanged

    private void AM2sourceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AM2sourceComboBoxActionPerformed
        // select input source for modulation                
        comboBoxMessage(E4421Constants.MessageModulationSource,
                0, 2, AM2sourceComboBox.getSelectedIndex());
}//GEN-LAST:event_AM2sourceComboBoxActionPerformed

    private void AM2waveformComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AM2waveformComboBoxActionPerformed
        // select waveform for modulation                
        comboBoxMessage(E4421Constants.MessageModulationWaveform,
                0, 2, AM2waveformComboBox.getSelectedIndex());        
}//GEN-LAST:event_AM2waveformComboBoxActionPerformed

    private void AM2rateSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_AM2rateSpinnerStateChanged
        // set modulation rate value
        spinnerMessage(E4421Constants.MessageModulationRate, 0, 2, (Double) AM2rateSpinner.getValue());
}//GEN-LAST:event_AM2rateSpinnerStateChanged

    private void FM1depthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FM1depthSpinnerStateChanged
        // set depth / deviation value
        spinnerMessage(E4421Constants.MessageModulationValue, 1, 1, (Double) FM1depthSpinner.getValue());
}//GEN-LAST:event_FM1depthSpinnerStateChanged

    private void FM1sourceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FM1sourceComboBoxActionPerformed
        // select input source for modulation                
        comboBoxMessage(E4421Constants.MessageModulationSource,
                1, 1, FM1sourceComboBox.getSelectedIndex());
}//GEN-LAST:event_FM1sourceComboBoxActionPerformed

    private void FM1waveformComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FM1waveformComboBoxActionPerformed
        // select waveform for modulation                
        comboBoxMessage(E4421Constants.MessageModulationWaveform,
                1, 1, FM1waveformComboBox.getSelectedIndex());
}//GEN-LAST:event_FM1waveformComboBoxActionPerformed

    private void FM1rateSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FM1rateSpinnerStateChanged
        // set modulation rate value
        spinnerMessage(E4421Constants.MessageModulationRate, 1, 1, (Double) FM1rateSpinner.getValue());
}//GEN-LAST:event_FM1rateSpinnerStateChanged

    private void FM2rateSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FM2rateSpinnerStateChanged
        // set modulation rate value
        spinnerMessage(E4421Constants.MessageModulationRate, 1, 2, (Double) FM2rateSpinner.getValue());
}//GEN-LAST:event_FM2rateSpinnerStateChanged

    private void FM2waveformComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FM2waveformComboBoxActionPerformed
        // select waveform for modulation                
        comboBoxMessage(E4421Constants.MessageModulationWaveform,
                1, 2, FM2waveformComboBox.getSelectedIndex());
}//GEN-LAST:event_FM2waveformComboBoxActionPerformed

    private void FM2sourceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FM2sourceComboBoxActionPerformed
        // select input source for modulation                
        comboBoxMessage(E4421Constants.MessageModulationSource,
                1, 2, FM2sourceComboBox.getSelectedIndex());
}//GEN-LAST:event_FM2sourceComboBoxActionPerformed

    private void FM2depthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_FM2depthSpinnerStateChanged
        // set depth / deviation value
        spinnerMessage(E4421Constants.MessageModulationValue, 1, 2, (Double) FM2depthSpinner.getValue());
}//GEN-LAST:event_FM2depthSpinnerStateChanged

    private void PM1depthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PM1depthSpinnerStateChanged
        // set depth / deviation value
        spinnerMessage(E4421Constants.MessageModulationValue, 2, 1, (Double) PM1depthSpinner.getValue());
}//GEN-LAST:event_PM1depthSpinnerStateChanged

    private void PM1sourceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PM1sourceComboBoxActionPerformed
        // select input source for modulation                
        comboBoxMessage(E4421Constants.MessageModulationSource,
                2, 1, PM1sourceComboBox.getSelectedIndex());
}//GEN-LAST:event_PM1sourceComboBoxActionPerformed

    private void PM1waveformComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PM1waveformComboBoxActionPerformed
        // select waveform for modulation                
        comboBoxMessage(E4421Constants.MessageModulationWaveform,
                2, 1, PM1waveformComboBox.getSelectedIndex());
}//GEN-LAST:event_PM1waveformComboBoxActionPerformed

    private void PM1rateSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PM1rateSpinnerStateChanged
        // set modulation rate value
        spinnerMessage(E4421Constants.MessageModulationRate, 2, 1, (Double) PM1rateSpinner.getValue());
}//GEN-LAST:event_PM1rateSpinnerStateChanged

    private void PM2rateSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PM2rateSpinnerStateChanged
        // set modulation rate value
        spinnerMessage(E4421Constants.MessageModulationRate, 2, 2, (Double) PM2rateSpinner.getValue());
}//GEN-LAST:event_PM2rateSpinnerStateChanged

    private void PM2waveformComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PM2waveformComboBoxActionPerformed
        // select waveform for modulation                
        comboBoxMessage(E4421Constants.MessageModulationWaveform,
                2, 2, PM2waveformComboBox.getSelectedIndex());
}//GEN-LAST:event_PM2waveformComboBoxActionPerformed

    private void PM2sourceComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PM2sourceComboBoxActionPerformed
        // select input source for modulation                
        comboBoxMessage(E4421Constants.MessageModulationSource,
                2, 2, PM2sourceComboBox.getSelectedIndex());
}//GEN-LAST:event_PM2sourceComboBoxActionPerformed

    private void PM2depthSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_PM2depthSpinnerStateChanged
        // set depth / deviation value
        spinnerMessage(E4421Constants.MessageModulationValue, 2, 2, (Double) PM2depthSpinner.getValue());
}//GEN-LAST:event_PM2depthSpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner AM1depthSpinner;
    private javax.swing.JSpinner AM1rateSpinner;
    private javax.swing.JComboBox AM1sourceComboBox;
    private javax.swing.JComboBox AM1waveformComboBox;
    private javax.swing.JSpinner AM2depthSpinner;
    private javax.swing.JSpinner AM2rateSpinner;
    private javax.swing.JComboBox AM2sourceComboBox;
    private javax.swing.JComboBox AM2waveformComboBox;
    private oh3ebf.lib.ui.buttons.ModelButton AMmodulationButton1;
    private oh3ebf.lib.ui.buttons.ModelButton AMmodulationButton2;
    private javax.swing.JSpinner FM1depthSpinner;
    private javax.swing.JSpinner FM1rateSpinner;
    private javax.swing.JComboBox FM1sourceComboBox;
    private javax.swing.JComboBox FM1waveformComboBox;
    private javax.swing.JSpinner FM2depthSpinner;
    private javax.swing.JSpinner FM2rateSpinner;
    private javax.swing.JComboBox FM2sourceComboBox;
    private javax.swing.JComboBox FM2waveformComboBox;
    private oh3ebf.lib.ui.buttons.ModelButton FMModulationButton1;
    private oh3ebf.lib.ui.buttons.ModelButton FMModulationButton2;
    private javax.swing.JMenu FileMenu;
    private javax.swing.JRadioButtonMenuItem MHzMenuItem;
    private javax.swing.JMenu OptionsMenu;
    private javax.swing.JSpinner PM1depthSpinner;
    private javax.swing.JSpinner PM1rateSpinner;
    private javax.swing.JComboBox PM1sourceComboBox;
    private javax.swing.JComboBox PM1waveformComboBox;
    private javax.swing.JSpinner PM2depthSpinner;
    private javax.swing.JSpinner PM2rateSpinner;
    private javax.swing.JComboBox PM2sourceComboBox;
    private javax.swing.JComboBox PM2waveformComboBox;
    private oh3ebf.lib.ui.buttons.ModelButton PhaseModulationButton1;
    private oh3ebf.lib.ui.buttons.ModelButton PhaseModulationButton2;
    private javax.swing.JPopupMenu UnitPopupMenu;
    private javax.swing.ButtonGroup UnitbuttonGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButtonMenuItem kHzMenuItem;
    private oh3ebf.lib.ui.buttons.ModelButton modulationControlButton;
    private oh3ebf.lib.ui.buttons.ModelButton rfcontrolButton;
    // End of variables declaration//GEN-END:variables
}
