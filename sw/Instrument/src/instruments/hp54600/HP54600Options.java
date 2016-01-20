/***********************************************************
 * Software: instrument client
 * Module:   HP54600 scope instance options class
 * Version:  0.2
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 15.11.2013
 *
 ***********************************************************/
package instruments.hp54600;

import interfaces.OptionsInterface;
import oh3ebf.lib.common.utilities.ConfigurationInstance;
import org.apache.log4j.Logger;

public class HP54600Options extends javax.swing.JInternalFrame {

    private ConfigurationInstance config;
    private static Logger logger;
    private OptionsInterface opIface;
    public final static String HP54600_CAPTURE_INTERVAL = "hp54600.capture_interval";
    public final static String HP54600_WAVEFORM_MAX = "hp54600.waveform_storage_count";

    /** Creates new form HP54600Options */
    public HP54600Options(OptionsInterface i) {
        opIface = i;

        // get logger instance for this class".
        logger = Logger.getLogger(HP54600Options.class);
        // get configuration
        config = ConfigurationInstance.getConfiguration();

        initComponents();

        // read parameters and set initial values
        captureIntevalSpinner.setValue(config.checkAndReadIntValue(HP54600_CAPTURE_INTERVAL, 10));
        waveMaxCntSpinner.setValue(config.checkAndReadIntValue(HP54600_WAVEFORM_MAX, 10));
    }

    /** Function saves parameters
     * 
     */
    private void storeValues() {
        // set properties
        config.setProperty(HP54600_CAPTURE_INTERVAL, captureIntevalSpinner.getValue());
        config.setProperty(HP54600_WAVEFORM_MAX, waveMaxCntSpinner.getValue());

        try {
            // save values
            config.save();
        } catch (Exception ex) {
            logger.error("failed to save configuration" + ex.getMessage());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        calcelButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        captureIntevalSpinner = new javax.swing.JSpinner();
        waveMaxCntSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(196, 204, 223));
        setTitle("HP54600 Options");
        setMaximumSize(new java.awt.Dimension(400, 300));
        setMinimumSize(new java.awt.Dimension(400, 300));
        setPreferredSize(new java.awt.Dimension(400, 300));
        setVisible(true);

        jTabbedPane1.setBackground(new java.awt.Color(196, 204, 223));

        jPanel1.setBackground(new java.awt.Color(196, 204, 223));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        calcelButton.setFont(new java.awt.Font("Dialog", 0, 10));
        calcelButton.setText("Cancel");
        calcelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcelButtonActionPerformed(evt);
            }
        });
        jPanel1.add(calcelButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, -1, -1));

        applyButton.setFont(new java.awt.Font("Dialog", 0, 10));
        applyButton.setText("Apply");
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });
        jPanel1.add(applyButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 210, -1, -1));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel1.setText("Capture interval:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel2.setText("Waveform store max:");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 32, -1, -1));

        captureIntevalSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        captureIntevalSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(10), null, Integer.valueOf(10)));
        captureIntevalSpinner.setMinimumSize(new java.awt.Dimension(80, 20));
        captureIntevalSpinner.setPreferredSize(new java.awt.Dimension(80, 20));
        jPanel1.add(captureIntevalSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 7, 80, 20));

        waveMaxCntSpinner.setFont(new java.awt.Font("SansSerif", 0, 10));
        waveMaxCntSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
        waveMaxCntSpinner.setPreferredSize(new java.awt.Dimension(80, 20));
        jPanel1.add(waveMaxCntSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 30, 80, 20));

        jTabbedPane1.addTab("tab1", jPanel1);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 390, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel2);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void calcelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcelButtonActionPerformed
        // close window
        this.dispose();
}//GEN-LAST:event_calcelButtonActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        // save parameters
        storeValues();

        if (opIface != null) {
            // update values
            opIface.update();
        }

        this.dispose();
}//GEN-LAST:event_applyButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JButton calcelButton;
    private javax.swing.JSpinner captureIntevalSpinner;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JSpinner waveMaxCntSpinner;
    // End of variables declaration//GEN-END:variables
}
