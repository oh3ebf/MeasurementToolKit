/***********************************************************
 * Software: instrument client
 * Module:   Scope demo instances managing class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.9.2012
 *
 ***********************************************************/

package instruments.scopedemo;

import oh3ebf.lib.gui.ScopePlot;
import javax.swing.*;
import oh3ebf.lib.gui.interfaces.PlotCursorInterface;
import org.apache.log4j.Logger;
import components.ScaleSpinnerBox;
import interfaces.ScaleSpinnerBoxInterface;
import java.text.DecimalFormat;
import oh3ebf.lib.ui.comboboxes.uiHelpers;


public class DSODemo extends javax.swing.JInternalFrame implements ScaleSpinnerBoxInterface, PlotCursorInterface {
    private ScopePlot scope;
    private static Logger logger;
    private String[] vertScale = {"100 mV/div","200 mV/div","500 mV/div","1 V/div","2 V/div","5 V/div"};
    private double[] scaleVoltage = {0.1, 0.2, 0.5, 1.0, 2.0, 5.0};
    
    private String[] hScale = {"500 ms/div","200 ms/div","100 ms/div","50 ms/div","20 ms/div","10 ms/div","5 ms/div","2 ms/div","1 ms/div",
    "0.5 ms/div","0.2 ms/div","0.1 ms/div","50 us/div","20 us/div","10 us/div","5 us/div","2 us/div","1 us/div","0.5 us/div","0.2 us/div",
    "0.1 us/div","50 ns/div","20 ns/div","10 ns/div"};

    private double[] hScaleTime = {0.5, 0.2, 0.1, 0.05, 0.02, 0.01, 0.005, 0.002, 0.001, 500 * 1E-6,
    200 * 1E-6, 100 * 1E-6, 50 * 1E-6, 20 * 1E-6, 10 * 1E-6, 5 * 1E-6, 2 * 1E-6, 1E-6, 500 * 1E-9, 
    200 * 1E-9, 100 * 1E-9, 50 * 1E-9, 20 * 1E-9, 10 * 1E-9};
          
    private double[] hScalerTable = {1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1000.0, 1 * 1E6,
    1 * 1E6, 1 * 1E6, 1 * 1E6, 1 * 1E6, 1 * 1E6, 1 * 1E6, 1 * 1E6, 1E6, 1 * 1E9, 
    1 * 1E9, 1 * 1E9, 1 * 1E9, 1 * 1E9, 1 * 1E9};
    
    private double hScaler;
    
    /** Creates new form DSODemo */
    public DSODemo() {
        // get logger instance for this class".
        logger = Logger.getLogger(DSODemo.class);
        
        initComponents();
        setSize(800,600);
             
        hScaler = hScalerTable[0];
        // initialize scope component
        scope = new ScopePlot(2, ((PlotCursorInterface)this));
        scope.setSize(jPanel23.getSize());
        
        scope.setTimeScale(hScaleTime[0]);        
        scope.setVoltageScale(0, scaleVoltage[0]);
        scope.setVoltageScale(1, scaleVoltage[0]);
        
        scope.setGndRef(0, 0.0D);
        scope.setGndRef(1, 0.0D);
        jPanel23.add(scope, java.awt.BorderLayout.CENTER);
        
        jPanel7.add(new ScaleSpinnerBox(this, 0, "Ch1", true, new SpinnerListModel(vertScale), new SpinnerNumberModel(0.0,-20.0,20.0,0.01)));
        jPanel7.add(new ScaleSpinnerBox(this, 1, "Ch2", true, new SpinnerListModel(vertScale), new SpinnerNumberModel(0.0,-20.0,20.0,0.01)));

        uiHelpers ui = new uiHelpers();
        
        jComboBox1.addItem(ui.makeObj("Ch1"));
        jComboBox1.addItem(ui.makeObj("Ch2"));
        jComboBox2.addItem(ui.makeObj("Ch1"));
        jComboBox2.addItem(ui.makeObj("Ch2"));
        //scope.setCursorTracking(cursor
    }
    
    /** Function handles waveform visual state callbacks
     *
     * @param boxNumber identity of spinner box
     * @param state selection state
     *
     */
    
    @Override
    public void SpinnerBoxisEnabled(int boxNumber, boolean state) {

        // select correct action
        switch(boxNumber) {
            case 0:
            case 1:
                // enable or disable wave form
                scope.setChActive(boxNumber, state);
                break;
        }
    }
    
    /** Function handles spinner 1 modification callbacks
     *
     * @param boxNumber identity of spinner box
     * @param value spinner value as object
     *
     */
    
    @Override
    public void SpinnerBox1ValueChanged(int boxNumber, Object value) {
         // select correct action
        switch(boxNumber) {
            case 0:
            case 1:
                try {
                    // evaluate voltage scale value
                    for(int i = 0;i < vertScale.length;i++) {
                        if(vertScale[i].equals(value)) {
                            // set voltage scale to ch0
                            scope.setVoltageScale(boxNumber, scaleVoltage[i]);
                        }
                    }
                } catch(NumberFormatException e) {
                    logger.error("Failed to convert String to Double: " + e.getMessage());
                }
                break;
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
        // select correct action
        switch(boxNumber) {
            case 0:
            case 1:
                try {
                    // set gnd reference value to chX
                    scope.setGndRef(boxNumber, Double.parseDouble((value.toString())));
                } catch(NumberFormatException e) {
                    logger.error("Failed to convert String to Double: " + e.getMessage());
                }
        }
    }
    
    /**
     *
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
    
    /**
     *
     *
     */
    
    @Override
            public void cursorPositionScaledValues(double x1, double x2, double y1, double y2) {
        DecimalFormat f = new DecimalFormat("0.00");
        cursorValueX1.setText(f.format(x1 * hScaler));
        cursorValueX2.setText(f.format(x2 * hScaler));
        cursorValueY1.setText(f.format(y1));
        cursorValueY2.setText(f.format(y2));
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        cursorButtonGroup = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollBar1 = new javax.swing.JScrollBar();
        jPanel14 = new javax.swing.JPanel();
        jSpinner5 = new javax.swing.JSpinner();
        jSpinner1 = new javax.swing.JSpinner();
        jPanel23 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel7 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        cursorValueX1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cursorValueX2 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cursorValueY1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cursorValueY2 = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jRadioButton19 = new javax.swing.JRadioButton();
        jRadioButton20 = new javax.swing.JRadioButton();
        jRadioButton21 = new javax.swing.JRadioButton();
        jRadioButton22 = new javax.swing.JRadioButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("DSODemo");
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        jScrollBar1.setMaximum(65000);
        jScrollBar1.setOrientation(javax.swing.JScrollBar.HORIZONTAL);
        jScrollBar1.addAdjustmentListener(new java.awt.event.AdjustmentListener() {
            public void adjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {
                jScrollBar1AdjustmentValueChanged(evt);
            }
        });

        jPanel8.add(jScrollBar1, java.awt.BorderLayout.CENTER);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jPanel14.setBorder(new javax.swing.border.EtchedBorder());
        jSpinner5.setModel(new SpinnerListModel(hScale));
        jSpinner5.setToolTipText("Time scale");
        jSpinner5.setPreferredSize(new java.awt.Dimension(100, 24));
        jSpinner5.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSpinner5StateChanged(evt);
            }
        });

        jPanel14.add(jSpinner5);

        jSpinner1.setModel(new SpinnerNumberModel(0.0,-20.0,20.0,0.01));
        jSpinner1.setToolTipText("Trigger position");
        jSpinner1.setPreferredSize(new java.awt.Dimension(100, 24));
        jPanel14.add(jSpinner1);

        jPanel8.add(jPanel14, java.awt.BorderLayout.SOUTH);

        jPanel2.add(jPanel8, java.awt.BorderLayout.SOUTH);

        jPanel23.setLayout(new java.awt.BorderLayout());

        jPanel2.add(jPanel23, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

        jPanel9.setLayout(new java.awt.GridBagLayout());

        jPanel9.setBorder(new javax.swing.border.EtchedBorder());
        jPanel7.add(jPanel9);

        jTabbedPane2.addTab("Channel", jPanel7);

        jTabbedPane2.addTab("Values", jPanel18);

        jPanel1.add(jTabbedPane2, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBorder(new javax.swing.border.TitledBorder("Values"));
        cursorValueX1.setEditable(false);
        cursorValueX1.setAutoscrolls(false);
        cursorValueX1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueX1.setFocusable(false);
        cursorValueX1.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueX1.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueX1.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueX1.setRequestFocusEnabled(false);
        cursorValueX1.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueX1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, -1, -1));

        jLabel1.setText("X1:");
        jLabel1.setEnabled(false);
        jLabel1.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel1.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel1.setPreferredSize(new java.awt.Dimension(30, 25));
        jLabel1.setRequestFocusEnabled(false);
        jLabel1.setVerifyInputWhenFocusTarget(false);
        jLabel1.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        jLabel2.setText("X2:");
        jLabel2.setEnabled(false);
        jLabel2.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel2.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel2.setPreferredSize(new java.awt.Dimension(30, 25));
        jLabel2.setRequestFocusEnabled(false);
        jLabel2.setVerifyInputWhenFocusTarget(false);
        jLabel2.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, -1, -1));

        cursorValueX2.setEditable(false);
        cursorValueX2.setAutoscrolls(false);
        cursorValueX2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueX2.setFocusable(false);
        cursorValueX2.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueX2.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueX2.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueX2.setRequestFocusEnabled(false);
        cursorValueX2.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueX2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, -1, -1));

        jLabel3.setText("Y1:");
        jLabel3.setEnabled(false);
        jLabel3.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel3.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel3.setPreferredSize(new java.awt.Dimension(30, 25));
        jLabel3.setRequestFocusEnabled(false);
        jLabel3.setVerifyInputWhenFocusTarget(false);
        jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, -1, -1));

        cursorValueY1.setEditable(false);
        cursorValueY1.setAutoscrolls(false);
        cursorValueY1.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueY1.setFocusable(false);
        cursorValueY1.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueY1.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueY1.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueY1.setRequestFocusEnabled(false);
        cursorValueY1.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueY1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, -1, -1));

        jLabel4.setText("Y2:");
        jLabel4.setEnabled(false);
        jLabel4.setMaximumSize(new java.awt.Dimension(30, 25));
        jLabel4.setMinimumSize(new java.awt.Dimension(30, 25));
        jLabel4.setPreferredSize(new java.awt.Dimension(30, 25));
        jLabel4.setRequestFocusEnabled(false);
        jLabel4.setVerifyInputWhenFocusTarget(false);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        jPanel3.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, -1, -1));

        cursorValueY2.setEditable(false);
        cursorValueY2.setAutoscrolls(false);
        cursorValueY2.setBorder(new javax.swing.border.BevelBorder(javax.swing.border.BevelBorder.LOWERED));
        cursorValueY2.setFocusable(false);
        cursorValueY2.setMaximumSize(new java.awt.Dimension(80, 24));
        cursorValueY2.setMinimumSize(new java.awt.Dimension(80, 24));
        cursorValueY2.setPreferredSize(new java.awt.Dimension(80, 24));
        cursorValueY2.setRequestFocusEnabled(false);
        cursorValueY2.setVerifyInputWhenFocusTarget(false);
        jPanel3.add(cursorValueY2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 110, -1, -1));

        jPanel4.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 160, 134, 150));

        jPanel20.setLayout(new java.awt.GridLayout(2, 2));

        jPanel20.setBorder(new javax.swing.border.TitledBorder(null, "Type", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 10)));
        cursorButtonGroup.add(jRadioButton19);
        jRadioButton19.setSelected(true);
        jRadioButton19.setText("None");
        jRadioButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton19ActionPerformed(evt);
            }
        });

        jPanel20.add(jRadioButton19);

        cursorButtonGroup.add(jRadioButton20);
        jRadioButton20.setText("X");
        jRadioButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton20ActionPerformed(evt);
            }
        });

        jPanel20.add(jRadioButton20);

        cursorButtonGroup.add(jRadioButton21);
        jRadioButton21.setText("Y");
        jRadioButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton21ActionPerformed(evt);
            }
        });

        jPanel20.add(jRadioButton21);

        cursorButtonGroup.add(jRadioButton22);
        jRadioButton22.setText("XY");
        jRadioButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton22ActionPerformed(evt);
            }
        });

        jPanel20.add(jRadioButton22);

        jPanel4.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 134, -1));

        jPanel5.setLayout(new java.awt.GridLayout(2, 2, 0, 5));

        jPanel5.setBorder(new javax.swing.border.TitledBorder("Tracking"));
        jLabel5.setText("X1");
        jPanel5.add(jLabel5);

        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jPanel5.add(jComboBox1);

        jLabel6.setText("X2");
        jPanel5.add(jLabel6);

        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jPanel5.add(jComboBox2);

        jPanel4.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 134, 90));

        jTabbedPane1.addTab("Cursors", jPanel4);

        getContentPane().add(jTabbedPane1, java.awt.BorderLayout.EAST);

        jMenu1.setText("File");
        jMenuItem1.setText("Close");
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }//GEN-END:initComponents

    private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jComboBox2ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jRadioButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton22ActionPerformed
        // XY cursors
        scope.setCursorMode(oh3ebf.lib.gui.primitives.PlotCursor.CURSOR_XY);
        jLabel1.setEnabled(true);
        jLabel2.setEnabled(true);        
        jLabel3.setEnabled(true);
        jLabel4.setEnabled(true);                
    }//GEN-LAST:event_jRadioButton22ActionPerformed

    private void jRadioButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton21ActionPerformed
        // Y cursors
        scope.setCursorMode(oh3ebf.lib.gui.primitives.PlotCursor.CURSOR_Y);
        jLabel1.setEnabled(false);
        jLabel2.setEnabled(false);
        jLabel3.setEnabled(true);
        jLabel4.setEnabled(true);        
    }//GEN-LAST:event_jRadioButton21ActionPerformed

    private void jRadioButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton20ActionPerformed
        // X cursors
        scope.setCursorMode(oh3ebf.lib.gui.primitives.PlotCursor.CURSOR_X);
        jLabel1.setEnabled(true);
        jLabel2.setEnabled(true);
        jLabel3.setEnabled(false);
        jLabel4.setEnabled(false);        
    }//GEN-LAST:event_jRadioButton20ActionPerformed

    private void jRadioButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton19ActionPerformed
        // no cursors
        scope.setCursorMode(oh3ebf.lib.gui.primitives.PlotCursor.CURSOR_NONE);
        jLabel1.setEnabled(false);
        jLabel2.setEnabled(false);
        jLabel3.setEnabled(false);
        jLabel4.setEnabled(false);
    }//GEN-LAST:event_jRadioButton19ActionPerformed

    private void jScrollBar1AdjustmentValueChanged(java.awt.event.AdjustmentEvent evt) {//GEN-FIRST:event_jScrollBar1AdjustmentValueChanged
        // set memory offset
        scope.setMemoryOffset(jScrollBar1.getValue());
    }//GEN-LAST:event_jScrollBar1AdjustmentValueChanged

    private void jSpinner5StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSpinner5StateChanged
        // find out current time/div value
        for(int i = 0; i < hScale.length; i++) {
            if(hScale[i].equals(jSpinner5.getValue()) == true) {
                // set new one
                scope.setTimeScale(hScaleTime[i]);
                hScaler = hScalerTable[i];
            }
        }        
    }//GEN-LAST:event_jSpinner5StateChanged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup cursorButtonGroup;
    private javax.swing.JTextField cursorValueX1;
    private javax.swing.JTextField cursorValueX2;
    private javax.swing.JTextField cursorValueY1;
    private javax.swing.JTextField cursorValueY2;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JRadioButton jRadioButton19;
    private javax.swing.JRadioButton jRadioButton20;
    private javax.swing.JRadioButton jRadioButton21;
    private javax.swing.JRadioButton jRadioButton22;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JSpinner jSpinner5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration//GEN-END:variables
    
}
