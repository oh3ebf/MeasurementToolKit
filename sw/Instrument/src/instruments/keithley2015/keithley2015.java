/***********************************************************
 * Software: instrument client
 * Module:   Keithley 2015 multimeter class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 1.4.2013
 *
 ***********************************************************/
package instruments.keithley2015;

import components.*;
import interfaces.MessageCallbackInterface;
import interfaces.MessageInterface;
import interfaces.OptionsInterface;
import interfaces.commandExecutionInterface;
import java.awt.Dimension;
import java.util.Hashtable;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.Document;
import oh3ebf.lib.ui.buttons.AdvancedButtonGroup;
import oh3ebf.lib.ui.buttons.ModelButton;
import oh3ebf.lib.common.utilities.ConfigurationInstance;
import org.apache.log4j.Logger;
import yami.ParamSet;

public class keithley2015 extends javax.swing.JInternalFrame implements
        MessageCallbackInterface, OptionsInterface, commandExecutionInterface {

    private static Logger logger;
    private DeviceMessage message;
    private AdvancedButtonGroup ad;
    private ConfigurationInstance config;
    private String name;
    private int mode;
    private String cmdId = null;
    private double measuredValue = 0.12340D;
    private int captureInterval = 1000;
    private DefaultFormatterFactory voltage;
    private DefaultFormatterFactory current;
    private DefaultFormatterFactory resistance;
    private DefaultFormatterFactory frequency;
    private DefaultFormatterFactory temperature;
    private UnitNumberFormatter vFormat;
    private UnitNumberFormatter aFormat;
    private UnitNumberFormatter rFormat;
    private UnitNumberFormatter fFormat;
    private UnitNumberFormatter tFormat;
    private Document doc;
    
    private static final int MODE_DC_VOLTAGE = 0;
    private static final int MODE_AC_VOLTAGE = 1;
    private static final int MODE_DC_CURRENT = 2;
    private static final int MODE_AC_CURRENT = 3;
    private static final int MODE_2_RESISTANCE = 4;
    private static final int MODE_4_RESISTANCE = 5;
    private static final int MODE_PERIOD = 6;
    private static final int MODE_FREQUENCY = 7;
    private static final int MODE_TEMPERATURE = 8;
    private static final int MODE_DIODE = 9;
    private static final int MODE_CONTUINITY = 10;
    private static final int MODE_DISTORTION = 11;
    private static final String PARAM_MODE = "mode";
    private static final String MessageMode = "Mode";

    /** Creates new form */
    public keithley2015(String name, Hashtable<String, String> properties, MessageInterface msg) {
        // get logger instance for this class
        logger = Logger.getLogger(keithley2015.class);

        // save local instance name
        this.name = name;
        // save messaging context        
        message = new DeviceMessage(name, msg);
        // add message listener
        message.addMessageCallback(name, this);

        config = ConfigurationInstance.getConfiguration();

        // set default mode, before reading from device
        mode = MODE_DC_VOLTAGE;

        initComponents();

        ad = new AdvancedButtonGroup();
        ad.add(DCVmodelButton);
        ad.add(ACVmodelButton);
        ad.add(DCAmodelButton);
        ad.add(ACAmodelButton);
        ad.add(resistance2ModelButton);
        ad.add(resistance4ModelButton);
        ad.add(frequencyModelButton);
        ad.add(temperatureModelButton);

        vFormat = new UnitNumberFormatter("V", "#,##0.#######");
        aFormat = new UnitNumberFormatter("A", "#,##0.#######");
        rFormat = new UnitNumberFormatter("\u03a9", "#,##0.#######");
        fFormat = new UnitNumberFormatter("Hz", "#,##0.#######");
        tFormat = new UnitNumberFormatter("\u2103", "#,##0.#######");

        voltage = new DefaultFormatterFactory(vFormat, vFormat, vFormat);
        current = new DefaultFormatterFactory(aFormat, aFormat, aFormat);

        resistance = new DefaultFormatterFactory(rFormat, rFormat, rFormat);
        frequency = new DefaultFormatterFactory(fFormat, fFormat, fFormat);
        temperature = new DefaultFormatterFactory(tFormat, tFormat, tFormat);

        // set default behaviour
        measureTextField.setFormatterFactory(voltage);
        measureTextField.setValue(measuredValue);
        ad.setSelected(0);

        // set default mode to device
        message.sendValue(MessageMode, MODE_DC_VOLTAGE);
        doc = loggingTextArea.getDocument();

        // read configuration
        update();

        //setSize(440, 290);
    }

    /** Function sends message
     *
     * @param param message parameters
     *
     * @return true/false
     *
     */
    @Override
    public boolean DataStreamCallback(ParamSet param) {
        //logger.debug("data stream parametercount: " + param.getNumberOfEntries());
        UnitNumberFormatter formatter = (UnitNumberFormatter) measureTextField.getFormatter();

        try {
            // check for valid message
            if (param.extractString().equals(DeviceMessage.StreamDataOk)) {
                measuredValue = param.extractDouble();
                measureTextField.setValue(measuredValue);
                loggingTextArea.append(formatter.valueToString(measuredValue));

            } else {
                logger.warn("streaming data fail");
            }
        } catch (Exception ex) {
            logger.error("failed to extract parameter from response" + ex.getMessage());
        }

        return (true);
    }

    /** Function return device instance name
     * 
     * @return name in string
     */
    @Override
    public String getName() {
        return (name);
    }

    /** Function implements callback from script engine command handling
     * 
     * @param slot
     * @param relay
     * @param state
     * @return
     */
    @Override
    public boolean executeCmd(int slot, int relay, int state) {
        throw new UnsupportedOperationException("Not supported in Keithley 2015");
    }

    /** Function implements callback from script engine command handling
     * 
     * @param cmd
     * @return
     */
    @Override
    public boolean executeCmd(String cmd, String attr) {
        if (cmd.equals("mode")) {
            ad.clearSelection();

            // dc voltage
            if (attr.equals("dcv")) {
                mode = MODE_DC_VOLTAGE;
                ad.setSelected(MODE_DC_VOLTAGE);
                setFunction();
                return (true);
            }

            // dc current
            if (attr.equals("dca")) {
                mode = MODE_DC_CURRENT;
                ad.setSelected(MODE_DC_CURRENT);
                setFunction();
                return (true);
            }

            // ac voltage
            if (attr.equals("acv")) {
                mode = MODE_AC_VOLTAGE;
                ad.setSelected(MODE_AC_VOLTAGE);
                setFunction();
                return (true);
            }

            // ac current
            if (attr.equals("aca")) {
                mode = MODE_AC_CURRENT;
                ad.setSelected(MODE_AC_CURRENT);
                setFunction();
                return (true);
            }

            // 2 wire resistance
            if (attr.equals("2res")) {
                mode = MODE_2_RESISTANCE;
                ad.setSelected(MODE_2_RESISTANCE);
                setFunction();
                return (true);
            }

            // 4 wire resistance
            if (attr.equals("4res")) {
                mode = MODE_4_RESISTANCE;
                ad.setSelected(MODE_4_RESISTANCE);
                setFunction();
                return (true);
            }

            // frequency
            if (attr.equals("freq")) {
                mode = MODE_FREQUENCY;
                ad.setSelected(MODE_FREQUENCY);
                setFunction();
                return (true);
            }

            // temperature
            if (attr.equals("temp")) {
                mode = MODE_TEMPERATURE;
                ad.setSelected(MODE_TEMPERATURE);
                setFunction();
                return (true);
            }
        }

        if (cmd.equals("measure")) {
            // start capturing values
            if (attr.equals("start")) {
                captureModelButton.setSelected(true);
                cmdId = capture(true, cmdId);
                return (true);
            }
            // stop capturing values
            if (attr.equals("stop")) {
                captureModelButton.setSelected(false);
                cmdId = capture(false, cmdId);
                return (true);
            }
        }

        return (false);
    }

    /** Function implements callback from script engine value reading 
     * 
     * @return double value
     */
    @Override
    public double readValueDouble() {
        return (measuredValue);
    }

    /** Function sets new measurement mode
     * 
     * @param mode to set
     */
    private void setFunction() {
        //ad.clearSelection();

        switch (mode) {
            case MODE_DC_VOLTAGE:
                // set display presentation
                measureTextField.setFormatterFactory(voltage);
                // set dc voltage measurement mode
                message.sendValue(MessageMode, MODE_DC_VOLTAGE);
                break;
            case MODE_DC_CURRENT:
                // set display presentation
                measureTextField.setFormatterFactory(current);
                // set dc current measurement mode
                message.sendValue(MessageMode, MODE_DC_CURRENT);
                break;
            case MODE_AC_VOLTAGE:
                // set display presentation
                measureTextField.setFormatterFactory(voltage);
                // set ac voltage measurement mode
                message.sendValue(MessageMode, MODE_AC_VOLTAGE);
                break;
            case MODE_AC_CURRENT:
                // set display presentation
                measureTextField.setFormatterFactory(current);
                // set ac current measurement mode
                message.sendValue(MessageMode, MODE_AC_CURRENT);
                break;
            case MODE_2_RESISTANCE:
                // set display presentation
                measureTextField.setFormatterFactory(resistance);
                // set 2 wire resistance measurement mode
                message.sendValue(MessageMode, MODE_2_RESISTANCE);
                break;
            case MODE_4_RESISTANCE:
                // set display presentation
                measureTextField.setFormatterFactory(resistance);
                // set 4 wire resistance measurement mode
                message.sendValue(MessageMode, MODE_4_RESISTANCE);
                break;
            case MODE_FREQUENCY:
                // set display presentation
                measureTextField.setFormatterFactory(frequency);
                // set frequency measurement mode
                message.sendValue(MessageMode, MODE_FREQUENCY);
                break;
            case MODE_TEMPERATURE:
                // set display presentation
                measureTextField.setFormatterFactory(temperature);
                // set temperature measurement mode
                message.sendValue(MessageMode, MODE_TEMPERATURE);
                break;
        }

    //ad.setSelected(mode);
    }

    /** Function handles capture request to device
     * 
     * @param state of measuring action
     */
    /*
    private void capture(boolean state) {
    if (state) {
    // disable option editing during capture
    optionsMenuItem.setEnabled(false);
    // start log writing
    ((loggingDocument) doc).openLog();
    ParamSet PsetMsg = new ParamSet();
    // command
    PsetMsg.append(DeviceMessage.MessageMeasurementStart);
    PsetMsg.append((byte) 0x0);
    PsetMsg.append(1);
    PsetMsg.append(interval);
    message.commandMessage(PsetMsg);
    } else {
    // enable option editing
    optionsMenuItem.setEnabled(true);
    // stop measurement
    message.sendString(DeviceMessage.MessageMeasurementStop);
    // stop log writing
    ((loggingDocument) doc).closeLog();
    }
    }*/
    /** Function handles data capture request
     * 
     * @param state true if capture eneble
     * @param id command unique id existing command
     * @return command unique id to new command
     */
    public String capture(boolean state, String id) {
        ParamSet PsetMsg = new ParamSet();

        if (!state) {
            // stop measurement             
            PsetMsg.append(DeviceMessage.MessageMeasurementStop);
            PsetMsg.append((byte) DeviceMessage.MeasurementCmdCapture);
            PsetMsg.append(id);

            message.commandMessage(PsetMsg);

            // stop log writing
            ((loggingDocument) doc).closeLog();

            return ("");
        } else {
            // start log writing
            if (!((loggingDocument) doc).openLog()) {
                // show error dialog to user
                JOptionPane.showMessageDialog(this, "Could not open log file!", "Parameter", JOptionPane.ERROR_MESSAGE);
            }

            // command
            PsetMsg.append(DeviceMessage.MessageMeasurementStart);
            PsetMsg.append((byte) DeviceMessage.MeasurementCmdCapture);
            PsetMsg.append((byte) 0);            // no channel info
            PsetMsg.append(1);                   // boolean inteval in use
            PsetMsg.append(1);                   // boolean measurement enabled
            PsetMsg.append(captureInterval);     // interval time

            // send message to server
            if (message.commandMessage(PsetMsg)) {
                // read command id
                return (message.getUniqueId());
            }

            return ("");
        }
    }

    /** Function reads and updates configuration parameters
     * 
     */
    public void update() {
        try {
            ((loggingDocument) doc).enableLogging(config.getBoolean(keithley2015Options.PARAM_LOG_STATE));
            ((loggingDocument) doc).setLogFileName(config.getString(keithley2015Options.PARAM_LOG_FILE));
            statusLabel.setText("Logging to file: " + config.getString(keithley2015Options.PARAM_LOG_FILE));
            captureInterval = config.getInt(keithley2015Options.PARAM_LOG_INTERVAL);
        } catch (Exception ex) {
            logger.warn("failed to read parameters from configuration, check options");
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
        r.parse(name);

        // clean buttons
        ad.clearSelection();
        // read mode and set
        mode = r.getInteger(PARAM_MODE);
        setFunction();
        ad.setSelected(mode);
    }

    /** Function stores scope setting to file
     * 
     * @param file to save
     */
    public void saveSettings(XmlWriter w) {
        // config section
        w.addConficSection(name);

        // save parameters
        w.addParamElement(PARAM_MODE, "", Integer.toString(mode));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        measureTextField = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        DCVmodelButton = new ModelButton("DC V", "DC V");
        ACVmodelButton = new ModelButton("AC V", "AC V");
        DCAmodelButton = new ModelButton("DC A", "DC A");
        ACAmodelButton = new ModelButton("AC A", "AC A");
        resistance2ModelButton = new ModelButton("\u03a9 2","\u03a9 2");
        resistance4ModelButton = new ModelButton("\u03a9 4","\u03a9 4");
        frequencyModelButton = new ModelButton("FREQ","FREQ");
        temperatureModelButton = new ModelButton("TEMP","TEMP");
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        loggingTextArea = new javax.swing.JTextArea();
        DefaultCaret caret = (DefaultCaret)loggingTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        jPanel5 = new javax.swing.JPanel();
        captureModelButton = new ModelButton("CAPTURE","CAPTURE");
        jPanel7 = new javax.swing.JPanel();
        statusLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu2 = new javax.swing.JMenu();
        optionsMenuItem = new javax.swing.JMenuItem();

        setBackground(new java.awt.Color(196, 204, 223));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Keithley 2015L");
        setMaximumSize(new java.awt.Dimension(440, 290));
        setMinimumSize(new java.awt.Dimension(440, 290));
        setPreferredSize(new java.awt.Dimension(440, 290));
        setVisible(true);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(196, 204, 223));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));
        jPanel2.setLayout(new java.awt.BorderLayout());

        measureTextField.setBackground(new java.awt.Color(51, 102, 0));
        measureTextField.setEditable(false);
        measureTextField.setForeground(new java.awt.Color(0, 255, 0));
        measureTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        measureTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        measureTextField.setCaretColor(new java.awt.Color(0, 255, 51));
        measureTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        measureTextField.setFocusable(false);
        measureTextField.setFont(new java.awt.Font("Dialog", 1, 18));
        measureTextField.setMaximumSize(new java.awt.Dimension(300, 40));
        measureTextField.setMinimumSize(new java.awt.Dimension(300, 40));
        measureTextField.setPreferredSize(new java.awt.Dimension(300, 40));
        jPanel2.add(measureTextField, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);
        jPanel2.getAccessibleContext().setAccessibleName("Display");

        jPanel3.setBackground(new java.awt.Color(196, 204, 223));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        DCVmodelButton.setText("DCV");
        DCVmodelButton.setColorModel(ModelButton.ModelNormal);
        DCVmodelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        DCVmodelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DCVmodelButton.setToggleMode(true);
        DCVmodelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DCVmodelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(DCVmodelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 65, 30));

        ACVmodelButton.setText("DCV");
        ACVmodelButton.setColorModel(ModelButton.ModelNormal);
        ACVmodelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        ACVmodelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ACVmodelButton.setToggleMode(true);
        ACVmodelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ACVmodelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ACVmodelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 65, 30));

        DCAmodelButton.setText("DCV");
        DCAmodelButton.setColorModel(ModelButton.ModelNormal);
        DCAmodelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        DCAmodelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        DCAmodelButton.setToggleMode(true);
        DCAmodelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DCAmodelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(DCAmodelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 10, 65, 30));

        ACAmodelButton.setText("DCV");
        ACAmodelButton.setColorModel(ModelButton.ModelNormal);
        ACAmodelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        ACAmodelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ACAmodelButton.setToggleMode(true);
        ACAmodelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ACAmodelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ACAmodelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 10, 65, 30));

        resistance2ModelButton.setText("DCV");
        resistance2ModelButton.setColorModel(ModelButton.ModelNormal);
        resistance2ModelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        resistance2ModelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        resistance2ModelButton.setToggleMode(true);
        resistance2ModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resistance2ModelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(resistance2ModelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 65, 30));

        resistance4ModelButton.setText("DCV");
        resistance4ModelButton.setColorModel(ModelButton.ModelNormal);
        resistance4ModelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        resistance4ModelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        resistance4ModelButton.setToggleMode(true);
        resistance4ModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resistance4ModelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(resistance4ModelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 50, 65, 30));

        frequencyModelButton.setText("DCV");
        frequencyModelButton.setColorModel(ModelButton.ModelNormal);
        frequencyModelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        frequencyModelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        frequencyModelButton.setToggleMode(true);
        frequencyModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                frequencyModelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(frequencyModelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 50, 65, 30));

        temperatureModelButton.setText("DCV");
        temperatureModelButton.setColorModel(ModelButton.ModelNormal);
        temperatureModelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        temperatureModelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        temperatureModelButton.setToggleMode(true);
        temperatureModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                temperatureModelButtonActionPerformed(evt);
            }
        });
        jPanel3.add(temperatureModelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 65, 30));

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        jPanel6.setBackground(new java.awt.Color(196, 204, 223));
        jPanel6.setLayout(new java.awt.BorderLayout());

        loggingTextArea.setColumns(20);
        loggingTextArea.setDocument(new loggingDocument());
        loggingTextArea.setEditable(false);
        loggingTextArea.setRows(5);
        jScrollPane1.setViewportView(loggingTextArea);

        jPanel6.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel6, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel5.setBackground(new java.awt.Color(196, 204, 223));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        captureModelButton.setText("CAPTURE");
        captureModelButton.setColorModel(ModelButton.ModelNormal);
        captureModelButton.setFont(new java.awt.Font("Dialog", 1, 12));
        captureModelButton.setMaximumSize(new java.awt.Dimension(90, 30));
        captureModelButton.setMinimumSize(new java.awt.Dimension(90, 30));
        captureModelButton.setPreferredSize(new java.awt.Dimension(90, 30));
        captureModelButton.setToggleMode(true);
        captureModelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                captureModelButtonActionPerformed(evt);
            }
        });
        jPanel5.add(captureModelButton);

        getContentPane().add(jPanel5, java.awt.BorderLayout.EAST);

        jPanel7.setBackground(new java.awt.Color(196, 204, 223));
        jPanel7.setLayout(new java.awt.BorderLayout());

        statusLabel.setFont(new java.awt.Font("SansSerif", 0, 10));
        statusLabel.setMinimumSize(new java.awt.Dimension(0, 14));
        statusLabel.setPreferredSize(new java.awt.Dimension(0, 14));
        jPanel7.add(statusLabel, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel7, java.awt.BorderLayout.SOUTH);

        jMenuBar1.setBackground(new java.awt.Color(196, 204, 223));

        jMenu2.setBackground(new java.awt.Color(196, 204, 223));
        jMenu2.setText("Tools");

        optionsMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        optionsMenuItem.setText("Options");
        optionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(optionsMenuItem);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void DCVmodelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DCVmodelButtonActionPerformed
        // set dc voltage measurement mode
        mode = MODE_DC_VOLTAGE;
        setFunction();
    }//GEN-LAST:event_DCVmodelButtonActionPerformed

    private void ACVmodelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACVmodelButtonActionPerformed
        // set ac voltage measurement mode
        mode = MODE_AC_VOLTAGE;
        setFunction();
    }//GEN-LAST:event_ACVmodelButtonActionPerformed

    private void DCAmodelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DCAmodelButtonActionPerformed
        // set dc current measurement mode
        mode = MODE_DC_CURRENT;
        setFunction();
    }//GEN-LAST:event_DCAmodelButtonActionPerformed

    private void resistance2ModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resistance2ModelButtonActionPerformed
        // set 2 wire resistance measurement mode
        mode = MODE_2_RESISTANCE;
        setFunction();
    }//GEN-LAST:event_resistance2ModelButtonActionPerformed

    private void resistance4ModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resistance4ModelButtonActionPerformed
        // set 4 wire resistance measurement mode
        mode = MODE_4_RESISTANCE;
        setFunction();
    }//GEN-LAST:event_resistance4ModelButtonActionPerformed

    private void frequencyModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_frequencyModelButtonActionPerformed
        // set frequency measurement mode
        mode = MODE_FREQUENCY;
        setFunction();
    }//GEN-LAST:event_frequencyModelButtonActionPerformed

    private void temperatureModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_temperatureModelButtonActionPerformed
        // set temperature measurement mode
        mode = MODE_TEMPERATURE;
        setFunction();
    }//GEN-LAST:event_temperatureModelButtonActionPerformed

    private void ACAmodelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ACAmodelButtonActionPerformed
        // set ac current measurement mode
        mode = MODE_AC_CURRENT;
        setFunction();
    }//GEN-LAST:event_ACAmodelButtonActionPerformed

    private void captureModelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_captureModelButtonActionPerformed
        // capture run / stop states
        cmdId = capture(captureModelButton.isSelected(), cmdId);  
    }//GEN-LAST:event_captureModelButtonActionPerformed

    private void optionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsMenuItemActionPerformed
        // show options
        addToDesktop(new keithley2015Options(this));              
}//GEN-LAST:event_optionsMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private oh3ebf.lib.ui.buttons.ModelButton ACAmodelButton;
    private oh3ebf.lib.ui.buttons.ModelButton ACVmodelButton;
    private oh3ebf.lib.ui.buttons.ModelButton DCAmodelButton;
    private oh3ebf.lib.ui.buttons.ModelButton DCVmodelButton;
    private oh3ebf.lib.ui.buttons.ModelButton captureModelButton;
    private oh3ebf.lib.ui.buttons.ModelButton frequencyModelButton;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea loggingTextArea;
    private javax.swing.JFormattedTextField measureTextField;
    private javax.swing.JMenuItem optionsMenuItem;
    private oh3ebf.lib.ui.buttons.ModelButton resistance2ModelButton;
    private oh3ebf.lib.ui.buttons.ModelButton resistance4ModelButton;
    private javax.swing.JLabel statusLabel;
    private oh3ebf.lib.ui.buttons.ModelButton temperatureModelButton;
    // End of variables declaration//GEN-END:variables
}
