package instruments;
/*
 * LA40.java
 *
 * Created on November 18, 2003, 5:14 PM
 */

/**
 *
 * @author  operator
 */

import java.lang.Thread;
import java.util.*;
import javax.swing.*;
import networking.netConnect;
import networking.netCommand;

public class LA40 extends javax.swing.JPanel implements Runnable{
    
    private String[] sampleTime = {"40 MS/s","20 MS/s","10 MS/s","5 MS/s","2.5 MS/s","1.25 MS/s","625 kS/s","312.5 kS/s"};
    private double[] sampleScale = {25 * 1E-9, 50 * 1E-9, 100 * 1E-9, 200 * 1E-9, 400 * 1E-9, 800 * 1E-9, 1.6 * 1E-6, 3.2 * 1E-6};
    
    //private int clkSel = 0;
    private boolean pollingState = false;
    private int memSize = 0;
    private boolean[] activePods;
    
    private netConnect con;
    private netCommand cmd;
    private Thread checkStatus;

    private Vector dataV;
    
    private LA40Waveform wave;
    private LA40Trigger trig;
    private LA40Listing list;
    private LA40LabelEditor edit;
    
    /** Creates new form LA40 */
    public LA40(netConnect c, netCommand k) {
        con = c;
        cmd = k;
        dataV = new Vector();
        activePods = new boolean[4];
        activePods[0] = false;
        activePods[1] = false;
        activePods[2] = false;
        activePods[3] = false;
        
        initComponents();
        // set visible information
        //jTextField1.setText(sampleTime[clkSel]);
        //jTextField2.setText(Integer.toString(trigWin));
        jSpinner1.setModel(new SpinnerListModel(sampleTime));
        jSpinner2.setModel(new SpinnerNumberModel(0, 0, 255, 1));
        
        start();
    }
    
    /*
     *
     *
     *
     */
    
    public void SetPollingState(boolean state) {
        pollingState = state;
    }
    
    /*
     *
     *
     *
     */
    
    public void start() {
        // start thread
        if(checkStatus == null)
            checkStatus = new Thread(this);
        
            checkStatus.start();
    }
    
    /*
     *
     *
     *
     *
     */
    
    public void stop() {
        // stop thread
        if(checkStatus != null) {
            checkStatus.stop();
            
            checkStatus = null;
        }
    }
    
    /*
     *
     *
     *
     *
     *
     */
    
    public void run() {
        // worker thread for trigger detection and data reading
        Thread myThread = Thread.currentThread();
        String statusReference = new String();
        int i = 0;
        byte lo[] = new byte[65536];
        byte hi[] = new byte[65536];
        
        while(checkStatus == myThread) {
            if(pollingState == true) {
                // send status request and wait for triggering
                if(cmd.LA40StatusRequest() == LA40StateEnum.Triggered) {
                    // cleanup all previous data
                    dataV.clear();
                    // check active pods and read measured data
                    if(activePods[0] == true) {
                        cmd.LA40ReadData(0, hi,lo);
                        // save data
                        dataV.add(lo);
                        dataV.add(hi);
                    }
                    
                    if(activePods[1] == true) {
                        cmd.LA40ReadData(1, hi,lo);
                        // save data
                        dataV.add(lo);
                        dataV.add(hi);
                    }
                    
                    if(activePods[2] == true) {
                        cmd.LA40ReadData(2, hi,lo);
                        // save data
                        dataV.add(lo);
                        dataV.add(hi);
                    }
                    
                    if(activePods[3] == true) {
                        cmd.LA40ReadData(3, hi,lo);
                        // save data
                        dataV.add(lo);
                        dataV.add(hi);
                    }
                    
                    // update data to display
                    if(wave != null) {
                        for(i = 0; i < sampleTime.length; i++) {
                            // find out current spinner value
                            if(sampleTime[i].equals(jSpinner1.getValue()) == true) {
                                // set new sampling time to drawing components
                                wave.updateSampleScale(sampleScale[i]);
                            }
                        }
                        // draw new wave forms
                        wave.setNewData(dataV);
                    }
                    
                    // enable run button
                    //jButton4.setEnabled(true);
                    // stop measurement operation
                    if(cmd.LA40SetStopState() == true) {
                        // enable start button
                        jButton4.setEnabled(true);
                        
                        pollingState = false;
                    } else {
                        System.out.println("No network connection");
                    }
                    
                    // stop thread
                    //checkStatus.stop();
                    
                    
                }
            }
            
            try {
                checkStatus.sleep(500);
            } catch(InterruptedException e) {
            }
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();
        jSpinner2 = new javax.swing.JSpinner();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout());

        setBorder(new javax.swing.border.TitledBorder("40MHz Logic Analyser"));
        setMaximumSize(new java.awt.Dimension(400, 210));
        setMinimumSize(new java.awt.Dimension(400, 210));
        setPreferredSize(new java.awt.Dimension(400, 210));
        jPanel4.setLayout(null);

        jPanel4.setBorder(new javax.swing.border.TitledBorder("Sample/Memory"));
        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setText("EXT");
        jToggleButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jPanel4.add(jToggleButton2);
        jToggleButton2.setBounds(115, 95, 33, 25);

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setSelected(true);
        jToggleButton3.setText("INT");
        jToggleButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jPanel4.add(jToggleButton3);
        jToggleButton3.setBounds(155, 95, 31, 25);

        buttonGroup2.add(jToggleButton4);
        jToggleButton4.setText("32k");
        jToggleButton4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jPanel4.add(jToggleButton4);
        jToggleButton4.setBounds(115, 45, 34, 25);

        buttonGroup2.add(jToggleButton5);
        jToggleButton5.setSelected(true);
        jToggleButton5.setText("64k");
        jToggleButton5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        jPanel4.add(jToggleButton5);
        jToggleButton5.setBounds(155, 45, 35, 25);

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel1.setText("Sample time");
        jPanel4.add(jLabel1);
        jLabel1.setBounds(10, 25, 85, 13);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel2.setText("Trigger window");
        jPanel4.add(jLabel2);
        jLabel2.setBounds(10, 75, 105, 13);

        jSpinner1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner1StateChanged(evt);
            }
        });

        jPanel4.add(jSpinner1);
        jSpinner1.setBounds(10, 45, 90, 20);

        jSpinner2.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner2StateChanged(evt);
            }
        });

        jPanel4.add(jSpinner2);
        jSpinner2.setBounds(10, 95, 90, 20);

        jCheckBox1.setText("Pod1");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jPanel4.add(jCheckBox1);
        jCheckBox1.setBounds(230, 35, 56, 23);

        jCheckBox2.setText("Pod2");
        jCheckBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });

        jPanel4.add(jCheckBox2);
        jCheckBox2.setBounds(230, 55, 56, 23);

        jCheckBox3.setText("Pod3");
        jCheckBox3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });

        jPanel4.add(jCheckBox3);
        jCheckBox3.setBounds(230, 75, 56, 23);

        jCheckBox4.setText("Pod4");
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        jPanel4.add(jCheckBox4);
        jCheckBox4.setBounds(230, 95, 56, 23);

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel3.setText("Memory");
        jPanel4.add(jLabel3);
        jLabel3.setBounds(125, 25, 55, 13);

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel4.setText("Trigger");
        jPanel4.add(jLabel4);
        jLabel4.setBounds(130, 75, 45, 13);

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 10));
        jLabel5.setText("Active pods");
        jPanel4.add(jLabel5);
        jLabel5.setBounds(225, 20, 65, 13);

        add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

        jPanel1.setBorder(new javax.swing.border.TitledBorder("Tools"));
        jButton1.setText("Trigger");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton1);

        jButton2.setText("Wave");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton2);

        jButton3.setText("Listing");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton3);

        jButton6.setText("Label Editor");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jPanel1.add(jButton6);

        add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(new javax.swing.border.TitledBorder("Status"));
        jButton4.setText("START");
        jButton4.setMaximumSize(new java.awt.Dimension(81, 25));
        jButton4.setMinimumSize(new java.awt.Dimension(81, 25));
        jButton4.setPreferredSize(new java.awt.Dimension(81, 25));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton4);

        jButton5.setText("STOP");
        jButton5.setMaximumSize(new java.awt.Dimension(81, 25));
        jButton5.setMinimumSize(new java.awt.Dimension(81, 25));
        jButton5.setPreferredSize(new java.awt.Dimension(81, 25));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton5);

        jButton10.setText("Testing");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jPanel2.add(jButton10);

        add(jPanel2, java.awt.BorderLayout.EAST);

    }//GEN-END:initComponents

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // Add your handling code here:
        
        // Label editor button
        
        Vector labelInfo;
        // show label editor
        if(edit == null)
            edit = new LA40LabelEditor(new javax.swing.JFrame(), true);            
        edit.setActive(activePods);        
        edit.setVisible(true);
        
        // update label information
        labelInfo = edit.getLabelData();
        if(labelInfo != null && wave != null)
            wave.setNewLabels(labelInfo);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
        // Add your handling code here:
        
        // Measurement POD4 activation check box
        
        if(jCheckBox4.isSelected() == true)
            // make pod active
            activePods[3] = true;
        else
            // make pod passive
            activePods[3] = false; 
        
        // update pod status
        if(trig != null)
            trig.setActive(activePods);
        //if(wave != null)
        //    wave.setActive(activePods);
        if(edit != null)
            edit.setActive(activePods);
    }//GEN-LAST:event_jCheckBox4ActionPerformed

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox3ActionPerformed
        // Add your handling code here:
        
        // Measurement POD3 activation check box
        
        if(jCheckBox3.isSelected() == true)
            // make pod active
            activePods[2] = true;
        else
            // make pod passive
            activePods[2] = false; 
        
        // update pod status
        if(trig != null)
            trig.setActive(activePods);
        //if(wave != null)
        //    wave.setActive(activePods);
        if(edit != null)
            edit.setActive(activePods);        
    }//GEN-LAST:event_jCheckBox3ActionPerformed

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox2ActionPerformed
        // Add your handling code here:
        
        // Measurement POD2 activation check box
        
        if(jCheckBox2.isSelected() == true)
            // make pod active
            activePods[1] = true;
        else
            // make pod passive
            activePods[1] = false; 

        // update pod status
        if(trig != null)
            trig.setActive(activePods);
        //if(wave != null)
        //    wave.setActive(activePods);
        if(edit != null)
            edit.setActive(activePods);        
    }//GEN-LAST:event_jCheckBox2ActionPerformed

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        // Add your handling code here:        
        
        // Measurement POD1 activation check box
        
        if(jCheckBox1.isSelected() == true)
            // make pod active
            activePods[0] = true;
        else
            // make pod passive
            activePods[0] = false;           
        
        // update pod status
        if(trig != null)
            trig.setActive(activePods);
        //if(wave != null)
        //    wave.setActive(activePods);
        if(edit != null)
            edit.setActive(activePods);        
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void jSpinner2StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner2StateChanged
        // Add your handling code here:
        // add selected value to dataset
        //LA40Clk[2] = (byte)trigWin;
        // send status request
        //con.netWrite(LA40Clk);
        // read server response
        //String response = con.netRead();
        // triggering has happened
        //if(response.indexOf("OK") != -1) {            
        //}    
    }//GEN-LAST:event_jSpinner2StateChanged

    private void jSpinner1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner1StateChanged
        // Add your handling code here:
        int i = 0;
        for(i = 0; i < sampleTime.length; i++) {
            // find out current spinner value
            if((sampleTime[i].equals(jSpinner1.getValue()) == true)) {
                // set new value
                cmd.LA40SetSampleRate((byte)(i));
                
                // waveform viever is already created
                if(wave != null)
                    // set new sampling time to drawing components
                    wave.updateSampleScale(sampleScale[i]);
            } else {
                System.out.println("No network connection");
            }
            
        }
    }//GEN-LAST:event_jSpinner1StateChanged

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // Add your handling code here:
        
        // Hardware testing dialog
        
        LA40Testing test = new LA40Testing(new javax.swing.JFrame(), true, cmd);
        test.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Add your handling code here:
        
        // STOP measurement button
        
        // stop thread
        //stop();
        pollingState = false;
        
        // stop measurement operation
        if(cmd.LA40SetStopState() == true) {
            // enable start button
            jButton4.setEnabled(true);            
        } else {
            System.out.println("No network connection");
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Add your handling code here:
        
        // Measurement START button
        
        // validate connetion status
        //if(con.netStatus() == true) {
            // is any pods active for measurement
            if(activePods[0] == true || activePods[1] == true || activePods[2] == true || activePods[3] == true) {
                // get device ready
                if(cmd.LA40SetArmedState() == true) {
                    jButton4.setEnabled(false);
                    pollingState = true;
                    // kill previous thread if exist
                    //if(checkStatus != null)
                    //    checkStatus = null;
                    // start new thread
                    //start();
                }
            }
        //} else {
            //System.out.println("No network connection");
        //}
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // Add your handling code here:
        
        // State analysis window
        
        // show listing window
        if(list == null)
            list = new LA40Listing();
        list.setVisible(true);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // Add your handling code here:
        
        // waveform window
        
        int i = 0;
        Vector labelInfo;
        // show waveform window
        if(wave == null) {
            wave = new LA40Waveform();
            
            for(i = 0; i < sampleTime.length; i++) {
                // find out current spinner value
                if(sampleTime[i].equals(jSpinner1.getValue()) == true) {
                    // set new sampling time to drawing components
                    wave.updateSampleScale(sampleScale[i]);
                }
            }
            
            // check if label editor is alive
            if(edit != null) {
                // update label information
                labelInfo = edit.getLabelData();
                if(labelInfo != null)
                    wave.setNewLabels(labelInfo);
            }
        }
        // update pod status
        //wave.setActive(activePods);
        wave.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // Add your handling code here:
        
        // Trigger window
        
        // show trigger window
        if(trig == null)
            trig = new LA40Trigger(con);
        // update pod status
        trig.setActive(activePods);
        trig.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        // Add your handling code here:
        
        // Memory 64K selection 
        
        // set memory size to 64k
        memSize = 1;
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // Add your handling code here:
        
        // Memory 32K selection
        
        // set memory size to 32k
        memSize = 0;        
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // Add your handling code here:
        
        // Trigger selection internal
        if(cmd.LA40SetTriggerSource(true) == false) {
            System.out.println("No network connection");                    
            
        } //else {
            //System.out.println("No network connection");
        //}
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // Add your handling code here:
        
        // Trigger selection external
        if(cmd.LA40SetTriggerSource(false) == false) {
            System.out.println("No network connection");
 
        } //else {
            //System.out.println("No network connection");
        //}
    }//GEN-LAST:event_jToggleButton2ActionPerformed
        
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton5;
    // End of variables declaration//GEN-END:variables
    
}
