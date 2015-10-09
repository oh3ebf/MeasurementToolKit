/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo raw frame editor class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.6.2013
 *
 ***********************************************************/
package instruments.can.generator;

import components.CanFrame;
import instruments.can.interfaces.NoCANDoFrameEditorInterface;
import java.util.Hashtable;
import javax.swing.JFormattedTextField;

public class NoCANDoGeneratorRawFrameEditor extends javax.swing.JInternalFrame {

    private NoCANDoFrameEditorInterface editorIf;
    private JFormattedTextField textFields[];
    private NoCANDoGeneratorDataObject data;
    private String[] busList;

    /** Creates new form NoCANDoGeneratorSignalEditor */
    public NoCANDoGeneratorRawFrameEditor(Hashtable<String, String> busProperties,
            NoCANDoGeneratorDataObject obj, NoCANDoFrameEditorInterface iFace) {
        // save interface
        editorIf = iFace;
        // save data object
        data = obj;
        busList = busProperties.keySet().toArray(new String[0]);

        initComponents();

        // initialize fields
        textFields = new JFormattedTextField[8];

        textFields[0] = data0;
        textFields[1] = data1;
        textFields[2] = data2;
        textFields[3] = data3;
        textFields[4] = data4;
        textFields[5] = data5;
        textFields[6] = data6;
        textFields[7] = data7;


        idTextField.setText(String.valueOf(obj.getCanData().getId()));
        dlcSpinner.getModel().setValue(obj.getCanData().getDlc());
        intervalSpinner.getModel().setValue(obj.getInterval());

        for (int i = 0; i < 8; i++) {
            textFields[i].setValue(obj.getCanData().getData()[i]);
        }
    /*
    data0.setValue(obj.getCanData().getData()[0]);
    data1.setValue(obj.getCanData().getData()[1]);
    data2.setValue(obj.getCanData().getData()[2]);
    data3.setValue(obj.getCanData().getData()[3]);
    data4.setValue(obj.getCanData().getData()[4]);
    data5.setValue(obj.getCanData().getData()[5]);
    data6.setValue(obj.getCanData().getData()[6]);
    data7.setValue(obj.getCanData().getData()[7]);*/
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBox2 = new javax.swing.JCheckBox();
        intervalSpinner = new javax.swing.JSpinner();
        SendButton = new javax.swing.JButton();
        busComboBox = new javax.swing.JComboBox(busList);
        dlcSpinner = new javax.swing.JSpinner();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        CloseButton = new javax.swing.JButton();
        ApplyButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ExtCheckBox = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        data0 = new javax.swing.JFormattedTextField();
        data1 = new javax.swing.JFormattedTextField();
        data2 = new javax.swing.JFormattedTextField();
        data3 = new javax.swing.JFormattedTextField();
        data4 = new javax.swing.JFormattedTextField();
        data5 = new javax.swing.JFormattedTextField();
        data6 = new javax.swing.JFormattedTextField();
        data7 = new javax.swing.JFormattedTextField();
        idTextField = new javax.swing.JFormattedTextField();
        RtrCheckBox = new javax.swing.JCheckBox();

        setBackground(new java.awt.Color(196, 204, 223));
        setTitle("Raw frame editor");
        setMaximumSize(new java.awt.Dimension(370, 320));
        setMinimumSize(new java.awt.Dimension(370, 320));
        setPreferredSize(new java.awt.Dimension(370, 340));
        setVisible(true);

        jCheckBox2.setBackground(new java.awt.Color(196, 204, 223));
        jCheckBox2.setText("Cyclic");
        jCheckBox2.setMaximumSize(new java.awt.Dimension(86, 24));
        jCheckBox2.setMinimumSize(new java.awt.Dimension(86, 24));
        jCheckBox2.setPreferredSize(new java.awt.Dimension(86, 24));

        intervalSpinner.setBackground(new java.awt.Color(196, 204, 223));
        intervalSpinner.setModel(new javax.swing.SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(10), null, Integer.valueOf(1)));
        intervalSpinner.setMinimumSize(new java.awt.Dimension(28, 22));
        intervalSpinner.setPreferredSize(new java.awt.Dimension(28, 22));

        SendButton.setText("Send");
        SendButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SendButtonActionPerformed(evt);
            }
        });

        dlcSpinner.setBackground(new java.awt.Color(196, 204, 223));
        dlcSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 8, 1));
        dlcSpinner.setMaximumSize(new java.awt.Dimension(90, 24));
        dlcSpinner.setMinimumSize(new java.awt.Dimension(90, 24));
        dlcSpinner.setPreferredSize(new java.awt.Dimension(90, 24));
        dlcSpinner.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                dlcSpinnerStateChanged(evt);
            }
        });

        jLabel2.setText("Can Id:");
        jLabel2.setMaximumSize(new java.awt.Dimension(75, 22));
        jLabel2.setMinimumSize(new java.awt.Dimension(75, 22));
        jLabel2.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel3.setText("Send mode:");
        jLabel3.setMaximumSize(new java.awt.Dimension(69, 22));
        jLabel3.setMinimumSize(new java.awt.Dimension(69, 22));
        jLabel3.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel4.setText("Interval:");
        jLabel4.setMaximumSize(new java.awt.Dimension(46, 22));
        jLabel4.setMinimumSize(new java.awt.Dimension(46, 22));
        jLabel4.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel5.setText("Can bus:");
        jLabel5.setMaximumSize(new java.awt.Dimension(51, 22));
        jLabel5.setMinimumSize(new java.awt.Dimension(51, 22));
        jLabel5.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel6.setText("Data length:");
        jLabel6.setMaximumSize(new java.awt.Dimension(69, 22));
        jLabel6.setMinimumSize(new java.awt.Dimension(69, 22));
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 22));

        jLabel7.setText("Data bytes:");
        jLabel7.setMaximumSize(new java.awt.Dimension(65, 22));
        jLabel7.setMinimumSize(new java.awt.Dimension(65, 22));
        jLabel7.setPreferredSize(new java.awt.Dimension(90, 22));

        CloseButton.setText("Close");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        ApplyButton.setText("Apply");
        ApplyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ApplyButtonActionPerformed(evt);
            }
        });

        jLabel1.setText("Frametype:");
        jLabel1.setMaximumSize(new java.awt.Dimension(64, 22));
        jLabel1.setMinimumSize(new java.awt.Dimension(64, 22));
        jLabel1.setPreferredSize(new java.awt.Dimension(90, 22));

        ExtCheckBox.setBackground(new java.awt.Color(196, 204, 223));
        ExtCheckBox.setText("Extended");

        jLabel8.setText("Flags:");
        jLabel8.setMaximumSize(new java.awt.Dimension(40, 22));
        jLabel8.setMinimumSize(new java.awt.Dimension(40, 22));
        jLabel8.setPreferredSize(new java.awt.Dimension(90, 22));

        data0.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data0.setEnabled(false);
        data0.setPreferredSize(new java.awt.Dimension(24, 24));

        data1.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data1.setEnabled(false);
        data1.setPreferredSize(new java.awt.Dimension(24, 24));

        data2.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data2.setEnabled(false);
        data2.setPreferredSize(new java.awt.Dimension(24, 24));

        data3.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data3.setEnabled(false);
        data3.setPreferredSize(new java.awt.Dimension(24, 24));

        data4.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data4.setEnabled(false);
        data4.setPreferredSize(new java.awt.Dimension(24, 24));

        data5.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data5.setEnabled(false);
        data5.setPreferredSize(new java.awt.Dimension(24, 24));

        data6.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data6.setEnabled(false);
        data6.setPreferredSize(new java.awt.Dimension(24, 24));

        data7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##"))));
        data7.setEnabled(false);
        data7.setPreferredSize(new java.awt.Dimension(24, 24));

        idTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###"))));
        idTextField.setMinimumSize(new java.awt.Dimension(20, 24));
        idTextField.setPreferredSize(new java.awt.Dimension(70, 24));

        RtrCheckBox.setBackground(new java.awt.Color(196, 204, 223));
        RtrCheckBox.setText("RTR");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ExtCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(RtrCheckBox))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(busComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dlcSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(SendButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(ApplyButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(CloseButton))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(data0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(data7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(intervalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(idTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheckBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(intervalSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ExtCheckBox)
                    .addComponent(RtrCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(busComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(dlcSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(data0, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(data1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(data2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(data3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(data4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(data5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(data6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(data7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CloseButton)
                    .addComponent(ApplyButton)
                    .addComponent(SendButton))
                .addGap(20, 20, 20))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void SendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SendButtonActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_SendButtonActionPerformed

    private void ApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ApplyButtonActionPerformed
        byte[] canData = new byte[8];
        CanFrame frame = new CanFrame();

        // update data values
        data.setBus((String) busComboBox.getSelectedItem());
        data.setInterval(((Number) intervalSpinner.getModel().getValue()).intValue());

        frame.setId(((Number) idTextField.getValue()).intValue());
        frame.setDlc(((Number) dlcSpinner.getModel().getValue()).shortValue());

        for (int i = 0; i < 8; i++) {
            canData[i] = ((Number) textFields[i].getValue()).byteValue();
        }

        /*
        canData[0] = ((Number) data0.getValue()).byteValue();
        canData[1] = ((Number) data1.getValue()).byteValue();
        canData[2] = ((Number) data2.getValue()).byteValue();
        canData[3] = ((Number) data3.getValue()).byteValue();
        canData[4] = ((Number) data4.getValue()).byteValue();
        canData[5] = ((Number) data5.getValue()).byteValue();
        canData[6] = ((Number) data6.getValue()).byteValue();
        canData[7] = ((Number) data7.getValue()).byteValue();
         */
        frame.setData(canData);
        frame.setFlags(0);

        data.setCanData(frame);
        editorIf.updateValues(data);
        // close window        
        this.dispose();

}//GEN-LAST:event_ApplyButtonActionPerformed

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        // close window
        this.dispose();
}//GEN-LAST:event_CloseButtonActionPerformed

    private void dlcSpinnerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_dlcSpinnerStateChanged
        // disable all
        for (int i = 0; i < 8; i++) {
            textFields[i].setEnabled(false);
        }

        // enable only those fields in dlc 
        for (short i = 0; i < ((Number) dlcSpinner.getModel().getValue()).shortValue(); i++) {
            textFields[i].setEnabled(true);
        }
        
}//GEN-LAST:event_dlcSpinnerStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ApplyButton;
    private javax.swing.JButton CloseButton;
    private javax.swing.JCheckBox ExtCheckBox;
    private javax.swing.JCheckBox RtrCheckBox;
    private javax.swing.JButton SendButton;
    private javax.swing.JComboBox busComboBox;
    private javax.swing.JFormattedTextField data0;
    private javax.swing.JFormattedTextField data1;
    private javax.swing.JFormattedTextField data2;
    private javax.swing.JFormattedTextField data3;
    private javax.swing.JFormattedTextField data4;
    private javax.swing.JFormattedTextField data5;
    private javax.swing.JFormattedTextField data6;
    private javax.swing.JFormattedTextField data7;
    private javax.swing.JSpinner dlcSpinner;
    private javax.swing.JFormattedTextField idTextField;
    private javax.swing.JSpinner intervalSpinner;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    // End of variables declaration//GEN-END:variables
}
