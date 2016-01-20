/***********************************************************
 * Software: instrument client
 * Module:   HP8922 GSM testester class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 16.9.2013
 *
 ***********************************************************/
package instruments.hp8922;

import components.*;
import interfaces.MessageCallbackInterface;
import interfaces.MessageInterface;
import java.util.Hashtable;
import javax.swing.JTabbedPane;
import oh3ebf.lib.gui.SpectrumPlot;
import oh3ebf.lib.gui.interfaces.WaveformDataInterface;
import org.apache.log4j.Logger;
import yami.ParamSet;

public class HP8922 extends javax.swing.JInternalFrame implements MessageCallbackInterface {
    //private MessageInterface msgIf;
    private static Logger logger;
    private DeviceMessage message;
    //private String MessageName;
    private SpectrumPlot spectrum;
    private WaveformDataInterface dIf[];

    /** Creates new form HP54600 */
    public HP8922(String name, Hashtable<String, String> properties, MessageInterface msg) {
        // get logger instance for this class
        logger = Logger.getLogger(HP8922.class);
        // save messaging context        
        message = new DeviceMessage(name, msg);
        // add message listener
        message.addMessageCallback(name, this);

        initComponents();

        dIf = new WaveformDataInterface[1];

        spectrum = new SpectrumPlot();
        dIf[0] = spectrum.getDataInterface(0);
        //HP8922WaveModel wave = new HP8922WaveModel();

        jPanel1.add(spectrum);
        setSize(800, 600);
    }

    /** Function sends message
     *
     * @param param message parameters
     *
     * @return true/false
     *
     */
    public boolean DataStreamCallback(ParamSet param) {
        //int ch = 0;
        logger.debug("data stream parametercount: " + param.getNumberOfEntries());

        try {
            // check for valid message
            if (param.extractString().equals(DeviceMessage.StreamDataOk)) {
                // read data array size
                int dataLength = param.getNumberOfEntries() - 2;
                double[] d = new double[dataLength];

                // loop all data sets
                for (int i = 0; i < dataLength; i++) {

                    //logger.debug("data set found ch: " + ch);
                    d[i] = param.extractDouble();
                    // set data to correct channel
                    dIf[0].updateData(d);
                    // update display
                    spectrum.repaint();
                }
            } else {
                logger.warn("streaming data fail");
            }
        } catch (Exception ex) {
            logger.error("failed to extract parameter from response" + ex.getMessage());
        }
        return (true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        SpectrumPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        CenterFreqSpinner = new javax.swing.JSpinner();
        SpanSpinner = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox();
        RefenrenceLevelSpinner = new javax.swing.JSpinner();
        jFormattedTextField1 = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        ScopePanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(196, 204, 223));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setVisible(true);

        jTabbedPane1.setBackground(new java.awt.Color(196, 204, 223));
        jTabbedPane1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jTabbedPane1StateChanged(evt);
            }
        });

        SpectrumPanel.setBackground(new java.awt.Color(196, 204, 223));
        SpectrumPanel.setLayout(new java.awt.BorderLayout());

        jPanel1.setBackground(new java.awt.Color(196, 204, 223));
        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel1.setMinimumSize(new java.awt.Dimension(400, 400));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 400));
        jPanel1.setLayout(new java.awt.BorderLayout());
        SpectrumPanel.add(jPanel1, java.awt.BorderLayout.CENTER);

        jPanel2.setBackground(new java.awt.Color(196, 204, 223));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 270, -1, -1));

        CenterFreqSpinner.setToolTipText("Center frequency");
        jPanel2.add(CenterFreqSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 100, 20));

        SpanSpinner.setToolTipText("Span");
        jPanel2.add(SpanSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 95, 100, -1));

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "In/Out", "Aux In" }));
        jComboBox1.setToolTipText("Max hold");
        jPanel2.add(jComboBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 100, 20));

        RefenrenceLevelSpinner.setToolTipText("Refrence level");
        jPanel2.add(RefenrenceLevelSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 100, -1));

        jFormattedTextField1.setEditable(false);
        jFormattedTextField1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jFormattedTextField1.setText("0.0db");
        jPanel2.add(jFormattedTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 25, 100, -1));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel1.setText("Level:");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel2.setText("Center freq:");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 45, -1, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel3.setText("Span:");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel4.setText("Ref. level:");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 115, -1, -1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel6.setText("Rf input:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, -1, -1));

        jCheckBox1.setBackground(new java.awt.Color(196, 204, 223));
        jCheckBox1.setFont(new java.awt.Font("SansSerif", 0, 10));
        jCheckBox1.setText("Max hold");
        jCheckBox1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jCheckBox1.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        jPanel2.add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 155, 110, 20));

        SpectrumPanel.add(jPanel2, java.awt.BorderLayout.LINE_END);

        jTabbedPane1.addTab("Spectrum", SpectrumPanel);
        jTabbedPane1.addTab("Scope", ScopePanel);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ParamSet PsetMsg = new ParamSet();
        // command
        PsetMsg.append(DeviceMessage.MessageMeasurementStart);

        PsetMsg.append((byte) DeviceMessage.MeasurementCmdCapture);
        // no channels
        PsetMsg.append((byte) 0);
        // no repeat
        PsetMsg.append(0);
        // active yes
        PsetMsg.append(1);

        message.commandMessage(PsetMsg);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTabbedPane1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jTabbedPane1StateChanged
        if (evt.getSource() instanceof JTabbedPane) {

            JTabbedPane pane = (JTabbedPane) evt.getSource();
            switch (pane.getSelectedIndex()) {
                case 0:
                    message.sendValue(HP8922Constants.MessageDisplaySelect, HP8922Constants.SCREEN_SPECTRUM_ANALYZER);
                    break;
                case 1:
                    message.sendValue(HP8922Constants.MessageDisplaySelect, HP8922Constants.SCREEN_OSCILLOSSCOPE);
                    break;

            }

        }
    }//GEN-LAST:event_jTabbedPane1StateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JSpinner CenterFreqSpinner;
    private javax.swing.JSpinner RefenrenceLevelSpinner;
    private javax.swing.JPanel ScopePanel;
    private javax.swing.JSpinner SpanSpinner;
    private javax.swing.JPanel SpectrumPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JFormattedTextField jFormattedTextField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
