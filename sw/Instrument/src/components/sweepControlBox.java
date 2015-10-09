/***********************************************************
 * Software: instrument client
 * Module:   sweep controls class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.2.2013
 *
 ***********************************************************/
package components;

import interfaces.progressUpdateInterface;
import java.awt.Color;
import lib.ui.buttons.ModelButton;
import interfaces.sweepContolBoxInterface;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.text.DefaultFormatter;
import lib.ui.comboboxes.uiHelpers;
import org.apache.log4j.Logger;

public class sweepControlBox extends javax.swing.JPanel implements progressUpdateInterface {

    private sweepContolBoxInterface sweepIf;
    private static Logger logger;
    private Hashtable<String, String> mainItems;
    private Hashtable<String, String> nestedItems;
    private double mainStart = 0;
    private double mainEnd = 0;
    private double mainStep = 0;
    private double nestedStart = 0;
    private double nestedEnd = 0;
    private double nestedStep = 0;
    private int timerSpeed = 1000;
    private String id = null;

    /** Creates new form sweep */
    public sweepControlBox(Hashtable<String, String> mainSweepItems,
            Hashtable<String, String> nestedSweepItems,
            sweepContolBoxInterface sweepIf, boolean progress) {

        // save callback interface
        this.sweepIf = sweepIf;

        // get logger instance for this class
        logger = Logger.getLogger(sweepControlBox.class);

        initComponents();

        // set initial timer value               
        timerSpinner.setValue(timerSpeed);

        // check for valid list
        if (mainSweepItems != null) {
            mainItems = mainSweepItems;
            Enumeration keys = mainSweepItems.keys();

            // add main sweep list
            while (keys.hasMoreElements()) {
                mainComboBox.addItem(uiHelpers.makeObj(keys.nextElement().toString()));
            }
        }

        // check for valid list
        if (nestedSweepItems != null) {
            nestedItems = nestedSweepItems;
            Enumeration keys = nestedSweepItems.keys();

            // add nested sweep list
            while (keys.hasMoreElements()) {
                nestedComboBox.addItem(uiHelpers.makeObj(keys.nextElement().toString()));
            }
        }

        // commit all changes immediately
        DefaultFormatter f = (DefaultFormatter) ((JSpinner.DefaultEditor) mainStartSpinner.getEditor()).getTextField().getFormatter();
        f.setCommitsOnValidEdit(true);
        f = (DefaultFormatter) ((JSpinner.DefaultEditor) mainEndSpinner.getEditor()).getTextField().getFormatter();
        f.setCommitsOnValidEdit(true);
        f = (DefaultFormatter) ((JSpinner.DefaultEditor) mainStepSpinner.getEditor()).getTextField().getFormatter();
        f.setCommitsOnValidEdit(true);
        f = (DefaultFormatter) ((JSpinner.DefaultEditor) nestedStartSpinner.getEditor()).getTextField().getFormatter();
        f.setCommitsOnValidEdit(true);
        f = (DefaultFormatter) ((JSpinner.DefaultEditor) nestedEndSpinner.getEditor()).getTextField().getFormatter();
        f.setCommitsOnValidEdit(true);
        f = (DefaultFormatter) ((JSpinner.DefaultEditor) nestedStepSpinner.getEditor()).getTextField().getFormatter();
        f.setCommitsOnValidEdit(true);
        f = (DefaultFormatter) ((JSpinner.DefaultEditor) timerSpinner.getEditor()).getTextField().getFormatter();
        f.setCommitsOnValidEdit(true);

        // set progress visibility
        mainProgressBar.setVisible(progress);
        nestedProgressBar.setVisible(progress);
    }

    /** Function sets new background color
     * 
     * @param bg color for background
     * 
     */
    public void setBgColor(Color bg) {
        this.setBackground(bg);
        jPanel1.setBackground(bg);
        jPanel2.setBackground(bg);
        nestedEnable.setBackground(bg);
        nestedStartSpinner.setBackground(bg);
        nestedEndSpinner.setBackground(bg);
        nestedStepSpinner.setBackground(bg);
        mainEndSpinner.setBackground(bg);
        mainStartSpinner.setBackground(bg);
    }

    /** Function returns status of nested sweep
     * 
     * @return state of sweep nesting
     * 
     */
    public boolean isNestedSweep() {
        return (nestedEnable.isSelected());
    }

    /** Function sets state of nested sweep
     * 
     * @param state of sweep
     */
    public void setNestedSweepState(boolean state) {
        nestedEnable.setSelected(state);
    }

    /** Function returns main sweep
     * 
     * @return main sweep
     */
    public String getMainSweep() {
        return (mainComboBox.getSelectedItem().toString());
    }

    /** Function returns main sweep selected index
     * 
     * @return main sweep selection index
     */
    public int getMainSweepIndex() {
        return (mainComboBox.getSelectedIndex());
    }

    /** Function sets main sweep selection index
     * 
     * @param sweep selection
     */
    public void setMainSweepIndex(int sweep) {
        mainComboBox.setSelectedIndex(sweep);
    }

    /** Function returns nested sweep
     * 
     * @return nested sweep
     */
    public String getNestedSweep() {
        return (nestedComboBox.getSelectedItem().toString());
    }

    /** Function returns main sweep selected index
     * 
     * @return main sweep selection index
     */
    public int getNestedSweepIndex() {
        return (nestedComboBox.getSelectedIndex());
    }

    /** Function sets nested sweep selection index
     * 
     * @param sweep selection
     */
    public void setNestedSweepIndex(int sweep) {
        nestedComboBox.setSelectedIndex(sweep);
    }

    /** Function returns main sweep start value
     * 
     * @return start value as double
     */
    public double getMainStart() {
        return mainStart;
    }

    /** Function sets main sweep start value
     * 
     * @param mainStart value
     */
    public void setMainStart(double mainStart) {
        this.mainStart = mainStart;
        mainStartSpinner.setValue(mainStart);
    }

    /** Function returns main sweep start value
     * 
     * @return start value
     */
    public double getMainEnd() {
        return mainEnd;
    }

    /** Function sets main sweep end value
     * 
     * @param mainEnd sweep end value
     */
    public void setMainEnd(double mainEnd) {
        this.mainEnd = mainEnd;
        mainEndSpinner.setValue(mainEnd);
    }

    /** Function returns main sweep end value
     * 
     * @return end value
     */
    public double getMainStep() {
        return mainStep;
    }

    /** Function sets main sweep step value
     * 
     * @param mainStep value
     */
    public void setMainStep(double mainStep) {
        this.mainStep = mainStep;
        mainStepSpinner.setValue(mainStep);
    }

    /** Function sets nested sweep start value
     * 
     * @return start value
     */
    public double getNestedStart() {
        return nestedStart;
    }

    /** Function returns nested sweep start value
     * 
     * @param nestedStart value
     */
    public void setNestedStart(double nestedStart) {
        this.nestedStart = nestedStart;
        nestedStartSpinner.setValue(nestedStart);
    }

    /** Function returns nested sweep end value
     * 
     * @return nestedEnd value
     */
    public double getNestedEnd() {
        return nestedEnd;
    }

    /** Function sets nested sweep end value
     * 
     * @param nestedEnd value
     */
    public void setNestedEnd(double nestedEnd) {
        this.nestedEnd = nestedEnd;
        nestedEndSpinner.setValue(nestedEnd);
    }

    /** Function  returns nested sweep step value
     * 
     * @return nestedStep value
     */
    public double getNestedStep() {
        return nestedStep;
    }

    /** Function sets nested sweep step value
     * 
     * @param nestedStep value
     */
    public void setNestedStep(double nestedStep) {
        this.nestedStep = nestedStep;
        nestedStepSpinner.setValue(nestedStep);
        enableNestedSweep();
    }

    /** Function returns timer speed value
     * 
     * @return timer speed in milliseconds
     */
    public int getTimerSpeed() {
        return timerSpeed;
    }

    /** Function sets timer speed value
     * 
     * @param timerSpeed in milliseconds
     */
    public void setTimerSpeed(int timerSpeed) {
        this.timerSpeed = timerSpeed;
        timerSpinner.setValue(timerSpeed);
    }

    /** Function implements progressbar update interface
     * 
     * @param n progressbar index
     * @param value of progress indicator
     * @return update status
     */
    public boolean updateProgressBar(int n, double value) {
        switch (n) {
            case 0:
                mainProgressBar.setValue((int) Math.round(value));
                break;
            case 1:
                nestedProgressBar.setValue((int) Math.round(value));
                break;
            default:
                return (false);
        }
        return (true);
    }

    /** Function updates visual state of nested sweep widgets
     * 
     */
    private void enableNestedSweep() {
        // enables or disables nested sweep
        if (nestedEnable.isSelected()) {
            nestedComboBox.setEnabled(true);
            nestedStartSpinner.setEnabled(true);
            nestedEndSpinner.setEnabled(true);
            nestedStepSpinner.setEnabled(true);
            nestedProgressBar.setEnabled(true);
        } else {
            nestedComboBox.setEnabled(false);
            nestedStartSpinner.setEnabled(false);
            nestedEndSpinner.setEnabled(false);
            nestedStepSpinner.setEnabled(false);
            nestedProgressBar.setEnabled(false);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        mainStartSpinner = new javax.swing.JSpinner();
        mainEndSpinner = new javax.swing.JSpinner();
        mainProgressBar = new javax.swing.JProgressBar();
        mainComboBox = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        mainStepSpinner = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        nestedStartSpinner = new javax.swing.JSpinner();
        nestedEndSpinner = new javax.swing.JSpinner();
        nestedProgressBar = new javax.swing.JProgressBar();
        nestedComboBox = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        nestedStepSpinner = new javax.swing.JSpinner();
        sweepButton = new ModelButton("Stop", "Start");
        timerSpinner = new javax.swing.JSpinner();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        nestedEnable = new javax.swing.JCheckBox();

        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), "Sweep"));
        setMaximumSize(new java.awt.Dimension(332, 391));
        setMinimumSize(new java.awt.Dimension(332, 391));
        setPreferredSize(new java.awt.Dimension(332, 391));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Main sweep"));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel1.setText("Parameter:");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 22, 80, -1));

        jLabel2.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel2.setText("Start:");
        jLabel2.setPreferredSize(new java.awt.Dimension(64, 14));
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, 70, -1));

        jLabel3.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel3.setText("End:");
        jLabel3.setPreferredSize(new java.awt.Dimension(64, 14));
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 77, -1, -1));

        mainStartSpinner.setMaximumSize(new java.awt.Dimension(100, 20));
        mainStartSpinner.setMinimumSize(new java.awt.Dimension(100, 20));
        mainStartSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        mainStartSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainStartSpinnerStateChanged(evt);
            }
        });
        jPanel1.add(mainStartSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 180, -1));

        mainEndSpinner.setMaximumSize(new java.awt.Dimension(100, 20));
        mainEndSpinner.setMinimumSize(new java.awt.Dimension(100, 20));
        mainEndSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        mainEndSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainEndSpinnerStateChanged(evt);
            }
        });
        jPanel1.add(mainEndSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 75, 180, -1));

        mainProgressBar.setForeground(new java.awt.Color(255, 153, 255));
        mainProgressBar.setStringPainted(true);
        jPanel1.add(mainProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 270, -1));

        mainComboBox.setPreferredSize(new java.awt.Dimension(100, 20));
        mainComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mainComboBoxActionPerformed(evt);
            }
        });
        jPanel1.add(mainComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 180, -1));

        jLabel5.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel5.setText("Step size:");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        mainStepSpinner.setMaximumSize(new java.awt.Dimension(100, 20));
        mainStepSpinner.setMinimumSize(new java.awt.Dimension(100, 20));
        mainStepSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        mainStepSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainStepSpinnerStateChanged(evt);
            }
        });
        jPanel1.add(mainStepSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 180, -1));

        add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 310, 160));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Nested sweep"));
        jPanel2.setEnabled(false);
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel8.setText("Parameter:");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 22, 80, -1));

        jLabel9.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel9.setText("Start:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 52, -1, -1));

        jLabel10.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel10.setText("End:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 77, -1, -1));

        nestedStartSpinner.setEnabled(false);
        nestedStartSpinner.setMaximumSize(new java.awt.Dimension(100, 20));
        nestedStartSpinner.setMinimumSize(new java.awt.Dimension(100, 20));
        nestedStartSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        nestedStartSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nestedStartSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(nestedStartSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 50, 180, -1));

        nestedEndSpinner.setEnabled(false);
        nestedEndSpinner.setMaximumSize(new java.awt.Dimension(100, 20));
        nestedEndSpinner.setMinimumSize(new java.awt.Dimension(100, 20));
        nestedEndSpinner.setPreferredSize(new java.awt.Dimension(100, 20));
        nestedEndSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nestedEndSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(nestedEndSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 75, 180, -1));

        nestedProgressBar.setForeground(new java.awt.Color(255, 153, 255));
        nestedProgressBar.setEnabled(false);
        nestedProgressBar.setStringPainted(true);
        jPanel2.add(nestedProgressBar, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 270, -1));

        nestedComboBox.setEnabled(false);
        nestedComboBox.setMaximumSize(new java.awt.Dimension(100, 20));
        nestedComboBox.setMinimumSize(new java.awt.Dimension(100, 20));
        nestedComboBox.setPreferredSize(new java.awt.Dimension(100, 20));
        nestedComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nestedComboBoxActionPerformed(evt);
            }
        });
        jPanel2.add(nestedComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 20, 180, -1));

        jLabel6.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel6.setText("Step size:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, -1, -1));

        nestedStepSpinner.setEnabled(false);
        nestedStepSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                nestedStepSpinnerStateChanged(evt);
            }
        });
        jPanel2.add(nestedStepSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 100, 180, -1));

        add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 20, 310, 160));

        sweepButton.setText("Start");
        sweepButton.setColorModel(ModelButton.ModelNormal);
        sweepButton.setToggleMode(true);
        sweepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sweepButtonActionPerformed(evt);
            }
        });
        add(sweepButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 185, -1, 30));

        timerSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(10), null, Integer.valueOf(10)));
        timerSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(timerSpinner, "####### ms"));
        timerSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                timerSpinnerStateChanged(evt);
            }
        });
        add(timerSpinner, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 190, 120, -1));

        jLabel7.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel7.setText("Sweep cycle:");
        add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 187, -1, 20));

        jLabel4.setFont(new java.awt.Font("SansSerif", 0, 11));
        jLabel4.setText("Nested Sweep:");
        add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 110, 20));

        nestedEnable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nestedEnableActionPerformed(evt);
            }
        });
        add(nestedEnable, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 190, -1, -1));
    }// </editor-fold>//GEN-END:initComponents
    private void nestedEnableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nestedEnableActionPerformed
        enableNestedSweep();
}//GEN-LAST:event_nestedEnableActionPerformed

    private void sweepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sweepButtonActionPerformed
        boolean state = false;

        // start / stop sweep
        if (sweepButton.isSelected()) {
            state = true;
            // main sweep initial values
            mainProgressBar.setMinimum((int) Math.round(mainStart));
            mainProgressBar.setMaximum((int) Math.round(mainEnd));

            // only if nested sweep is enable
            if (nestedEnable.isSelected()) {
                // nested sweep initial values
                nestedProgressBar.setMinimum((int) Math.round(nestedStart));
                nestedProgressBar.setMaximum((int) Math.round(nestedEnd));
            }

            // disable cycle editing
            timerSpinner.setEnabled(false);

        } else {
            // enable cycle editing
            timerSpinner.setEnabled(true);
        }

        id = sweepIf.sweepValues(id, state, timerSpeed, mainStart, mainEnd, mainStep,
                nestedStart, nestedEnd, nestedStep);
}//GEN-LAST:event_sweepButtonActionPerformed

    private void mainStartSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainStartSpinnerStateChanged
        try {
            // get new start value
            mainStart = Double.parseDouble(mainStartSpinner.getValue().toString());
        } catch (Exception ex) {
            logger.error("failed to parse main sweep start value " + ex.getMessage());
        }
    }//GEN-LAST:event_mainStartSpinnerStateChanged

    private void mainEndSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainEndSpinnerStateChanged
        try {
            // get new end value
            mainEnd = Double.parseDouble(mainEndSpinner.getValue().toString());
        } catch (Exception ex) {
            logger.error("failed to parse main sweep end value " + ex.getMessage());
        }
    }//GEN-LAST:event_mainEndSpinnerStateChanged

    private void mainStepSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainStepSpinnerStateChanged
        try {
            // get new step value
            mainStep = Double.parseDouble(mainStepSpinner.getValue().toString());
        } catch (Exception ex) {
            logger.error("failed to parse main sweep step value " + ex.getMessage());
        }
    }//GEN-LAST:event_mainStepSpinnerStateChanged

    private void nestedStartSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nestedStartSpinnerStateChanged
        try {
            // get new start value
            nestedStart = Double.parseDouble(nestedStartSpinner.getValue().toString());
        } catch (Exception ex) {
            logger.error("failed to parse nested sweep start value " + ex.getMessage());
        }
    }//GEN-LAST:event_nestedStartSpinnerStateChanged

    private void nestedEndSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nestedEndSpinnerStateChanged
        try {
            // get new end value
            nestedEnd = Double.parseDouble(nestedEndSpinner.getValue().toString());
        } catch (Exception ex) {
            logger.error("failed to parse nested sweep end value " + ex.getMessage());
        }
    }//GEN-LAST:event_nestedEndSpinnerStateChanged

    private void nestedStepSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_nestedStepSpinnerStateChanged
        try {
            // get new step value
            nestedStep = Double.parseDouble(nestedStepSpinner.getValue().toString());
        } catch (Exception ex) {
            logger.error("failed to parse nested sweep step value " + ex.getMessage());
        }
    }//GEN-LAST:event_nestedStepSpinnerStateChanged

    private void timerSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_timerSpinnerStateChanged
        try {
            // get new timer value
            timerSpeed = Integer.parseInt(timerSpinner.getValue().toString());
        } catch (Exception ex) {
            logger.error("failed to parse sweep cycle value " + ex.getMessage());
        }
}//GEN-LAST:event_timerSpinnerStateChanged

    private void mainComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mainComboBoxActionPerformed
        String selection = mainComboBox.getSelectedItem().toString();

        String unit = mainItems.get(selection);

        mainStartSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(mainStartSpinner, "####### " + unit));
        mainEndSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(mainEndSpinner, "####### " + unit));
        mainStepSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(mainStepSpinner, "####### " + unit));        
    }//GEN-LAST:event_mainComboBoxActionPerformed

    private void nestedComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nestedComboBoxActionPerformed
        String unit = nestedItems.get(nestedComboBox.getSelectedItem().toString());

        if (nestedComboBox.getSelectedItem().toString().equals(mainComboBox.getSelectedItem().toString()) && nestedEnable.isSelected()) {
            // show error dialog to user
            JOptionPane.showMessageDialog(this, "Main and nested sweep cannot be same!",
                    "Sweep", JOptionPane.WARNING_MESSAGE);
        }

        nestedStartSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(nestedStartSpinner, "####### " + unit));
        nestedEndSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(nestedEndSpinner, "####### " + unit));
        nestedStepSpinner.setEditor(new javax.swing.JSpinner.NumberEditor(nestedStepSpinner, "####### " + unit));
    }//GEN-LAST:event_nestedComboBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JComboBox mainComboBox;
    private javax.swing.JSpinner mainEndSpinner;
    private javax.swing.JProgressBar mainProgressBar;
    private javax.swing.JSpinner mainStartSpinner;
    private javax.swing.JSpinner mainStepSpinner;
    private javax.swing.JComboBox nestedComboBox;
    private javax.swing.JCheckBox nestedEnable;
    private javax.swing.JSpinner nestedEndSpinner;
    private javax.swing.JProgressBar nestedProgressBar;
    private javax.swing.JSpinner nestedStartSpinner;
    private javax.swing.JSpinner nestedStepSpinner;
    private lib.ui.buttons.ModelButton sweepButton;
    private javax.swing.JSpinner timerSpinner;
    // End of variables declaration//GEN-END:variables
}
