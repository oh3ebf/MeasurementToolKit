package instruments;

import networking.netConnect;
/*
 * DSO40Testing.java
 *
 * Created on January 5, 2004, 6:17 PM
 */

/**
 *
 * @author  operator
 */
public class DSO40Testing extends javax.swing.JDialog {    
    // test patterns to send
    private byte[] DSO40TestResetEnable = {0x03,0x04,0x00,0x00};
    private byte[] DSO40TestResetDisable = {0x03,0x04,0x00,0x01};
    private byte[] DSO40TestClrEnable = {0x03,0x04,0x01,0x01};
    private byte[] DSO40TestClrDisable = {0x03,0x04,0x01,0x00};
    private byte[] DSO40TestStepEnable = {0x03,0x04,0x02,0x00};
    private byte[] DSO40TestStepDisable = {0x03,0x04,0x02,0x01};
    private byte[] DSO40TestRamEnable = {0x03,0x04,0x03,0x01};
    private byte[] DSO40TestRamDisable = {0x03,0x04,0x03,0x00};
    private byte[] DSO40TestTrigClrEnable = {0x03,0x04,0x04,0x01};
    private byte[] DSO40TestTrigClrDisable = {0x03,0x04,0x04,0x00};
    private byte[] DSO40TestMEM32k = {0x03,0x04,0x05,0x00};
    private byte[] DSO40TestMEM64k = {0x03,0x04,0x05,0x01};
    private byte[] DSO40TestAdcEnable = {0x03,0x04,0x06,0x01};
    private byte[] DSO40TestAdcDisable = {0x03,0x04,0x06,0x00};
    private byte[] DSO40TestCntStepHi = {0x03,0x04,0x07,0x00};
    private byte[] DSO40TestCntStepLo = {0x03,0x04,0x07,0x01};
    
    //private byte[] DSO40TestReset_0 = {1,2,3,4};
    //private byte[] DSO40TestReset_0 = {1,2,3,4};
    //private byte[] DSO40TestReset_0 = {1,2,3,4};
    //private byte[] DSO40TestReset_0 = {1,2,3,4};
    
    private int testSelection = 1;
    private int testValue = 0;
    private netConnect con;
    
    /** Creates new form DSO40Testing */
    public DSO40Testing(java.awt.Frame parent, boolean modal,netConnect c) {
        super(parent, modal);
        con = c;
        initComponents();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setTitle("DSO40 Testing");
        setLocationRelativeTo(this);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(new javax.swing.border.TitledBorder("Test selection"));
        jRadioButton1.setSelected(true);
        jRadioButton1.setText("RESET");
        buttonGroup1.add(jRadioButton1);
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton1);

        jRadioButton2.setText("CLR");
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton2);

        jRadioButton3.setText("EN_STEP");
        buttonGroup1.add(jRadioButton3);
        jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton3ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton3);

        jRadioButton4.setText("MEMSIZE");
        buttonGroup1.add(jRadioButton4);
        jRadioButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton4ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton4);

        jRadioButton5.setText("RAM_CS");
        buttonGroup1.add(jRadioButton5);
        jRadioButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton5ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton5);

        jRadioButton6.setText("TRIG_CLR");
        buttonGroup1.add(jRadioButton6);
        jRadioButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton6ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton6);

        jRadioButton7.setText("ADC ENABLE");
        buttonGroup1.add(jRadioButton7);
        jRadioButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton7ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton7);

        jRadioButton8.setText("COUNTER STEP");
        buttonGroup1.add(jRadioButton8);
        jRadioButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton8ActionPerformed(evt);
            }
        });

        jPanel2.add(jRadioButton8);

        jPanel1.add(jPanel2, java.awt.BorderLayout.WEST);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setViewportView(jTextArea1);

        jPanel4.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jPanel3.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel5.setBorder(new javax.swing.border.EtchedBorder());
        jButton1.setText("Run Test");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel5.add(jButton1);

        jButton2.setText("Close");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel5.add(jButton2);

        jPanel3.add(jPanel5, java.awt.BorderLayout.SOUTH);

        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }//GEN-END:initComponents

    private void jRadioButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton8ActionPerformed
        // Add your handling code here:
        // set test selection
        testSelection = 8;
    }//GEN-LAST:event_jRadioButton8ActionPerformed

    private void jRadioButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton7ActionPerformed
        // Add your handling code here:
        // set test selection adc chipselect
        testSelection = 7;
    }//GEN-LAST:event_jRadioButton7ActionPerformed

    private void jRadioButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton6ActionPerformed
        // Add your handling code here:
        // set test selection to trigger clear
        testSelection = 6;
    }//GEN-LAST:event_jRadioButton6ActionPerformed

    private void jRadioButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton5ActionPerformed
        // Add your handling code here:
        // set test selection to ram output enable
        testSelection = 5;
    }//GEN-LAST:event_jRadioButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Add your handling code here:
        // close window
        setVisible(false);
        dispose();        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Add your handling code here:
        //String response = new String();
        switch(testSelection) {
            case 1:
                // run reset test case
                if(testValue == 0)
                    con.netWrite(DSO40TestResetDisable);
                else
                    con.netWrite(DSO40TestResetEnable);
                break;
            case 2:
                // run clr test case
                if(testValue == 0)
                    con.netWrite(DSO40TestClrEnable);
                else
                    con.netWrite(DSO40TestClrDisable);
                break;
            case 3:
                // run memory stepping test case
                if(testValue == 0)
                    con.netWrite(DSO40TestStepDisable);
                else
                    con.netWrite(DSO40TestStepEnable);
                break;
            case 4:
                // run memory size test case
                if(testValue == 0)
                    con.netWrite(DSO40TestMEM64k);
                else
                    con.netWrite(DSO40TestMEM32k);
                break;
            case 5:
                // run ram chip select test case
                if(testValue == 0)
                    con.netWrite(DSO40TestRamDisable);
                else
                    con.netWrite(DSO40TestRamEnable);                
                break;
            case 6:
                // run trigger clear test case
                if(testValue == 0)
                    con.netWrite(DSO40TestTrigClrDisable);
                else
                    con.netWrite(DSO40TestTrigClrEnable);                
                break;
            case 7:
                // run ADC chipselect test case
                if(testValue == 0)
                    con.netWrite(DSO40TestAdcDisable);
                else
                    con.netWrite(DSO40TestAdcEnable);                   
                break;
            case 8:
                // run counter step test case
                if(testValue == 0)
                    con.netWrite(DSO40TestCntStepHi);
                else
                    con.netWrite(DSO40TestCntStepLo);                                   
                break;
        }
        // read server response
        String response = con.netRead();
        // get returned value
        testValue = Integer.parseInt(response);
    }//GEN-LAST:event_jButton1ActionPerformed
    
    private void jRadioButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton4ActionPerformed
        // Add your handling code here:
        // set test selection to memory size
        testSelection = 4;
    }//GEN-LAST:event_jRadioButton4ActionPerformed

    private void jRadioButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton3ActionPerformed
        // Add your handling code here:
        // set test selection to step enable line
        testSelection = 3;
    }//GEN-LAST:event_jRadioButton3ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // Add your handling code here:
        // set test selection to clr line
        testSelection = 2;
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // Add your handling code here:
        // set test selection to reset line
        testSelection = 1;
    }//GEN-LAST:event_jRadioButton1ActionPerformed
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false);
        dispose();
    }//GEN-LAST:event_closeDialog
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        new DSO40Testing(new javax.swing.JFrame(), true).show();
//    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JRadioButton jRadioButton8;
    private javax.swing.JRadioButton jRadioButton7;
    private javax.swing.JRadioButton jRadioButton6;
    private javax.swing.JRadioButton jRadioButton5;
    private javax.swing.JRadioButton jRadioButton4;
    private javax.swing.JRadioButton jRadioButton3;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
    
}
