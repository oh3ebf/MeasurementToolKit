package instruments;
/*
 * Timing.java
 *
 * Created on November 3, 2003, 3:26 PM
 */

/**
 *
 * @author  operator
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;
import components.waitDialog;

public class LA40Waveform extends javax.swing.JFrame {
    private LA40Wave wave;    
    
    private Vector labels;
    private Vector masks;
    private Vector data;
    private Vector result;
    
    private String[] hScale = {"1 s", "0,5 s", "100 ms", "50 ms", "10 ms", "5 ms", "1 ms", 
                                "500 us", "100 us", "50 us", "10 us", "5 us", "1 us",
                                "500 ns", "100 ns", "50 ns", "25 ns"};
    private double[] hScaleTimeValues = {1, 0.5, 0.1, 0.05, 0.01, 0.005, 0.001,
                                        500 * 1E-6, 100 * 1E-6, 50 * 1E-6, 10 * 1E-6, 5 * 1E-6, 1 * 1E-6,
                                        500 * 1E-9, 100 * 1E-9, 50 * 1E-9, 25 * 1E-9};
    
    /** Creates new form Timing */
    public LA40Waveform() {
        wave = new LA40Wave();        
        initComponents();                
        
        jPanel8.add(wave, java.awt.BorderLayout.CENTER);
        jSpinner1.setModel(new SpinnerListModel(hScale));               
        setSize(600, 400);
    }
        
    public void setNewData(Vector d) {
        // get new data
        data = d;
        calcData();
        // create waveformd ata and show it
        wave.updateData(result);
    }
    
    public void setNewLabels(Vector l) {
        // get label names
        labels = (Vector)l.elementAt(0);
        // get label masks and convert to byte table
        masks = (Vector)l.elementAt(1);    
        // create waveformdata and show it
        calcData();
        wave.updateData(result);
    }
    
    public void updateSampleScale(double t) {
        // update sampling time scale
        wave.setSampleScale(t);
    }
    
    private void calcData() {
        int i = 0, n = 0, p = 0;
        byte maskTmp = 0;               // temporary mask byte 
        byte maskIndex = 0;             // pointer to mask table
        byte bit = 0;                   // bit pointer
        byte bitMask = 0x01;            // bit mask used with bit pointer
        byte shiftCnt = 0;              // bit shifting counter
        byte resultShiftCnt = 0;        // bit shifting counter for result byte
        byte maskedData = 0;            // data byte after masking
        int[] maskTable;                // here are mask byte for one label
        byte[] d;                       // data array from pod
        long shiftedData = 0;           // 
        long[] tmp = new long[65536];   // array containing labels bit data
        
        waitDialog wait = new waitDialog();
        int waitInc = 0;
        
        // check label and data validity
        if(labels == null || data == null || labels.size() == 0 || data.size() == 0)
            return;
        
        if(result == null)
            // create if not exists
            result = new Vector();         
        else 
            // othervise make it empty
            result.clear();
        
        // calculate increment size to progress window
        waitInc = 100 / (labels.size() * data.size());
        
        // show progress window to user
        wait.setVisible(true);
        
        // loop all labels
        for(i = 0; i < labels.size();i++) {                        
            // get mask array of this label
            maskTable = (int[])masks.elementAt(i);
            // check number of selected bits in mask word for bus type drawing
            if(countOneBits(maskTable[0]) > 1 || countOneBits(maskTable[1]) > 1 || countOneBits(maskTable[2]) > 1 || countOneBits(maskTable[3]) > 1) {
                // clear mask pointer
                maskIndex = 0;
                // loop all activated pods byte arrays
                for(n = 0; n < data.size();n++) {
                    // update prgress bar
                    wait.setProgress(waitInc);                    
                    wait.repaint();
                    // mask selection
                    switch(n) {
                        case 1:
                        case 3:
                        case 5:
                        case 7:
                            // get hi mask byte
                            maskTmp = (byte)((maskTable[maskIndex] & 0xFF00) >> 8);
                            // increment masktable pointer
                            maskIndex++;
                            break;
                        case 0:
                        case 2:
                        case 4:
                        case 6:
                            // get lo mask byte
                            maskTmp = (byte)(maskTable[maskIndex] & 0x00FF);
                            break;
                    }
                    
                    // get one byte array from pod data set
                    d = (byte[])data.elementAt(n);
                    
                    // actual data parsing loop
                    for(p = 0; p < d.length;p++) {
                        // calculate masked byte
                        maskedData = (byte)(d[p] & maskTmp);
                        // init bit mask
                        bitMask = 0x01;
                        // check every bit and calculate needed shift count to correct bit position in result
                        for(bit = 0; bit < 8;bit++) {
                            // if bit is one in mask byte 
                            if((bitMask & maskTmp) == bitMask) {
                                // no shift for first bit
                                if(bit > 0) {
                                    // add one bit to rigth position pointed by bitMask
                                    shiftedData |= (bitMask & maskedData) >> shiftCnt;
                                    // increment bit shifting counter
                                    shiftCnt++;                                    
                                } else {
                                    // add bit to zero position
                                    shiftedData |= bitMask & maskedData;
                                    // increment bit shifting counter
                                    shiftCnt++;
                                }
                            }
                            // rotate mask bit to next position
                            bitMask = (byte)(bitMask << 1);
                        }
                        // check bit position in result
                        if(resultShiftCnt > 0) {
                            // shift to left 
                            tmp[p] |= shiftedData << resultShiftCnt; 
                            // add used bits to bit counter
                            resultShiftCnt += shiftCnt;
                        } else {
                            // first bits 
                            tmp[p] = shiftedData;
                            // load initial used bit to counter
                            resultShiftCnt = shiftCnt;
                        }
                    }
                }
                // add waveform type, label and data to result vector
                result.add(0);
                result.add(labels.elementAt(i));
                result.add(tmp);
            } else {
                // single line waveform drawing
            }
        }
        
        // hide wait window
        wait.setVisible(false);
        wait.dispose();
        //return(result);
    }
    
    private int countOneBits(int m) {
        int cnt = 0;
        int i = 0;
        int mask = 0x0001;
        
        // loop all used bits
        for(i = 0; i < 16;i++) {
            //check if this bit is one
            if((m & mask) == mask)
                // increment counter
                cnt++;
            // next mask value
            mask = mask << 1;
        }
        // return result
        return(cnt);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        jPanel1 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel7 = new javax.swing.JPanel();
        jSpinner2 = new javax.swing.JSpinner();
        jPanel2 = new javax.swing.JPanel();
        jSpinner3 = new javax.swing.JSpinner();
        jComboBox1 = new javax.swing.JComboBox();
        jSpinner4 = new javax.swing.JSpinner();
        jComboBox2 = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollBar2 = new javax.swing.JScrollBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        setTitle("Timing window");
        setLocationRelativeTo(this);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setLayout(new java.awt.BorderLayout());

        jPanel1.add(jPanel4);

        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel5.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel6.setBorder(new javax.swing.border.TitledBorder(null, "Seconds/Div", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 10)));
        jSpinner1.setPreferredSize(new java.awt.Dimension(100, 24));
        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jPanel6.add(jSpinner1);

        jPanel5.add(jPanel6);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel7.setBorder(new javax.swing.border.TitledBorder(null, "Delay", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 10)));
        jSpinner2.setPreferredSize(new java.awt.Dimension(60, 24));
        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jPanel7.add(jSpinner2);

        jPanel5.add(jPanel7);

        jPanel2.setBorder(new javax.swing.border.TitledBorder(null, "Cursor", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 10)));
        jSpinner3.setPreferredSize(new java.awt.Dimension(60, 24));
        jSpinner3.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner3StateChanged(evt);
            }
        });

        jPanel2.add(jSpinner3);

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel2.add(jComboBox1);

        jSpinner4.setPreferredSize(new java.awt.Dimension(64, 24));
        jSpinner4.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner4StateChanged(evt);
            }
        });

        jPanel2.add(jSpinner4);

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jPanel2.add(jComboBox2);

        jPanel5.add(jPanel2);

        jPanel1.add(jPanel5);

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        jPanel8.setLayout(new java.awt.BorderLayout());

        jPanel8.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jPanel8ComponentResized(evt);
            }
        });

        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });

        jPanel8.add(jScrollBar1, java.awt.BorderLayout.EAST);

        jScrollBar2.setMaximum(65535);
        jScrollBar2.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar2.setValue(32768);
        jScrollBar2.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar2AdjustmentValueChanged(evt);
            }
        });

        jPanel8.add(jScrollBar2, java.awt.BorderLayout.SOUTH);

        getContentPane().add(jPanel8, java.awt.BorderLayout.CENTER);

        jMenu1.setText("File");
        jMenuItem1.setText("Load Waveform...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem1);

        jMenuItem3.setText("Save Waveform...");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem3);

        jMenu1.add(jSeparator1);

        jMenuItem2.setText("Close");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });

        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu3.setText("Edit");
        jMenuBar1.add(jMenu3);

        jMenu2.setText("Help");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }//GEN-END:initComponents

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jSpinner4StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner4StateChanged
        // Add your handling code here:
    }//GEN-LAST:event_jSpinner4StateChanged

    private void jSpinner3StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner3StateChanged
        // Add your handling code here:
    }//GEN-LAST:event_jSpinner3StateChanged

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // Add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jScrollBar2AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar2AdjustmentValueChanged
        // Add your handling code here:
        
        // move waveforms in horizontal plane
        wave.setTimeOffset(jScrollBar2.getValue());
    }//GEN-LAST:event_jScrollBar2AdjustmentValueChanged

    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar1AdjustmentValueChanged
        // Add your handling code here:
        // move waveforms in vertical plane
        wave.setLabelOffset(jScrollBar1.getValue());        
    }//GEN-LAST:event_jScrollBar1AdjustmentValueChanged

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        // Add your handling code here:
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        // Add your handling code here:
        int i = 0;
        for(i = 0; i < hScale.length; i++) {
            // find out current spinner value
            if(hScale[i].equals(jSpinner1.getValue()) == true) {
                // set new sampling time to drawing components
                wave.setTimeScale(hScaleTimeValues[i]);
            }
        }        
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jPanel8ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel8ComponentResized
        // Add your handling code here:
        wave.setResize();
    }//GEN-LAST:event_jPanel8ComponentResized

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // Add your handling code here:
        // close dialog
        setVisible(false);
        dispose();        
    }//GEN-LAST:event_jMenuItem2ActionPerformed
    
    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        System.exit(0);
    }//GEN-LAST:event_exitForm
    
    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
        //new Timing().show();
//    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollBar jScrollBar2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JSpinner jSpinner4;
    // End of variables declaration//GEN-END:variables
    
}
