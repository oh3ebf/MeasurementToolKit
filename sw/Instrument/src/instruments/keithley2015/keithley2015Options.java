/***********************************************************
 * Software: instrument client
 * Module:   Keithley 2015 multimeter options class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 10.4.2013
 *
 ***********************************************************/
package instruments.keithley2015;

import interfaces.OptionsInterface;
import java.io.File;
import javax.swing.JFileChooser;
import oh3ebf.lib.common.utilities.ConfigurationInstance;
import org.apache.log4j.Logger;

public class keithley2015Options extends javax.swing.JInternalFrame {

    private String currentDir;
    private String fileName;
    private static Logger logger;
    private ConfigurationInstance config;
    private OptionsInterface opIface;
            
    public final static String PARAM_LOG_FILE = "keithley.log_file_name";
    public final static String PARAM_LOG_STATE = "keithley.log_state";
    public final static String PARAM_LOG_DIR = "keithley.log_dir_name";
    public final static String PARAM_LOG_INTERVAL = "keithley.log_interval";
    
    /** Creates new form keithley2015Options */
    public keithley2015Options(OptionsInterface iFace) {
        // get logger instance for this class
        logger = Logger.getLogger(keithley2015Options.class);
        // get configuration handler
        config = ConfigurationInstance.getConfiguration();
        this.opIface = iFace;
        
        initComponents();
        
        // read parametrs from file
        fileName = config.checkAndReadStringValue(PARAM_LOG_FILE, "log.txt");
        currentDir = config.checkAndReadStringValue(PARAM_LOG_DIR, ".");
        logFileNameTextField.setText(fileName);
        jCheckBox1.setSelected(config.checkAndReadBoolValue(PARAM_LOG_STATE, "false"));
        jSpinner1.setValue(config.checkAndReadIntValue(PARAM_LOG_INTERVAL, "1000"));
    }

    /** Function returns new working directory
     * 
     * @return current working directory
     * 
     */
    public String getCurrentDir() {
        return currentDir;
    }

    /** Function sets new working directory
     * 
     * @param str as an new working directory
     */
    public void setCurrentDir(String str) {
        this.currentDir = str;
    }

    /** Function returns selected log filename
     * 
     * @return file name
     */
    public String getFileName() {
        return fileName;
    }

    /** Function returns current state of logging enable
     * 
     * @return current state
     */
    public boolean getLoggingState() {
        return (jCheckBox1.isSelected());
    }

    /** Function returns capture interval parameter
     * 
     * @return capture interval value
     */
    
    public int getCaptureInterval() {
        return (Integer.parseInt(jSpinner1.getValue().toString()));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        closeButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        logFileNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        fileButton = new javax.swing.JButton();

        setBackground(new java.awt.Color(196, 204, 223));
        setTitle("Options");
        setMaximumSize(new java.awt.Dimension(400, 170));
        setMinimumSize(new java.awt.Dimension(400, 170));
        setPreferredSize(new java.awt.Dimension(400, 170));
        try {
            setSelected(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }
        setVisible(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        closeButton.setText("Cancel");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        getContentPane().add(closeButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 100, -1, -1));

        applyButton.setText("Apply");
        applyButton.setMaximumSize(new java.awt.Dimension(66, 24));
        applyButton.setMinimumSize(new java.awt.Dimension(66, 24));
        applyButton.setPreferredSize(new java.awt.Dimension(66, 24));
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });
        getContentPane().add(applyButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 100, 80, -1));

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel1.setText("Log file name:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, 20));

        logFileNameTextField.setMaximumSize(new java.awt.Dimension(420, 230));
        logFileNameTextField.setMinimumSize(new java.awt.Dimension(420, 230));
        getContentPane().add(logFileNameTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 200, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel2.setText("Capture interval:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 75, -1, -1));

        jSpinner1.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(1000), Integer.valueOf(10), null, Integer.valueOf(1)));
        jSpinner1.setMaximumSize(new java.awt.Dimension(430, 230));
        jSpinner1.setMinimumSize(new java.awt.Dimension(430, 230));
        jSpinner1.setPreferredSize(new java.awt.Dimension(430, 230));
        getContentPane().add(jSpinner1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 70, 90, 20));

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 10));
        jLabel3.setText("Enable logging:");
        jLabel3.setPreferredSize(new java.awt.Dimension(95, 14));
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 110, 20));

        jCheckBox1.setBackground(new java.awt.Color(196, 204, 223));
        getContentPane().add(jCheckBox1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, 80, 20));

        fileButton.setText("File");
        fileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileButtonActionPerformed(evt);
            }
        });
        getContentPane().add(fileButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 10, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        try {
            // save parameters to file
            config.setProperty(PARAM_LOG_FILE, fileName);
            config.setProperty(PARAM_LOG_STATE, jCheckBox1.isSelected());
            config.setProperty(PARAM_LOG_DIR, currentDir);
            config.setProperty(PARAM_LOG_INTERVAL, jSpinner1.getValue());
            config.save();
            
            
        } catch (Exception ex) {
            logger.error("failed to save configuration file");
        }

        if (opIface != null) {
            // update values
            opIface.update();
        }                
        
        // close window
        this.dispose();
}//GEN-LAST:event_applyButtonActionPerformed

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // close window
        this.dispose();
}//GEN-LAST:event_closeButtonActionPerformed

    private void fileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileButtonActionPerformed
        // create and show file chooser
        JFileChooser jfc = new JFileChooser();
        if (currentDir != null) {
            jfc.setCurrentDirectory(new File(currentDir));
        }

        int result = jfc.showOpenDialog(this);

        // check return value
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        // get user selected file
        fileName = jfc.getSelectedFile().toString();
        logFileNameTextField.setText(fileName);

        try {
            // get current path
            currentDir = fileName.substring(0, fileName.lastIndexOf(File.separator));
        } catch (Exception ex) {
            logger.error("failed to open file" + fileName);
        }
    }//GEN-LAST:event_fileButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton fileButton;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTextField logFileNameTextField;
    // End of variables declaration//GEN-END:variables
}
