/***********************************************************
 * Software: instrument client
 * Module:   numeric input control with units class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 10.10.2013
 *
 ***********************************************************/
package components;

import interfaces.NumericControlInterface;
import javax.swing.JRadioButtonMenuItem;

public class NumericInputControl extends javax.swing.JPanel {
    // min max value 
    private double value = 0.0D;
    private double minValue = 0.0D;
    private double maxValue = 0.0D;
    private String format;
    private String units[];
    private JRadioButtonMenuItem menu[];
    private unitScalerBase scaler;
    private NumericControlInterface callback;
    private int id;

    /** Creates new form NumericInputContol */
    public NumericInputControl(String label, int id, String units[], String format,
            double min, double max, unitScalerBase s, NumericControlInterface iFace) {
        this.id = id;
        this.units = units;
        this.format = format;
        minValue = min;
        maxValue = max;
        scaler = s;
        
        // TODO pitäisikö tarkistaa onko null
        callback = iFace;

        menu = new JRadioButtonMenuItem[units.length];

        initComponents();

        // set component title
        setBorder(javax.swing.BorderFactory.createTitledBorder(label));
        // set formatter
        numericTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.NumberFormatter(
                new java.text.DecimalFormat(format + units[0]))));

        //load popup menu and add listeners
        for (int i = 0; i < units.length; i++) {
            // add new radio mennu item
            menu[i] = new javax.swing.JRadioButtonMenuItem();
            // add unit text to button
            menu[i].setText(units[i]);

            // add menuitems
            if (i == 0) {
                // initially firs item is selected
                menu[i].setSelected(true);
            } else {
                menu[i].setSelected(false);
            }

            // set tooltip
            menu[i].setToolTipText("Value in " + units[i]);
            // add to button group
            UnitbuttonGroup.add(menu[i]);
            UnitPopupMenu.add(menu[i]);
            // add listener for events
            menu[i].addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    MenuItemActionPerformed(evt);
                }
            });
        }
    }

    /** Function sets new value
     * 
     * @param v value to set
     */
    public void setValue(double v) {
        value = v;
        numericTextField.setValue(scaler.getScaledValue(value));
    }

    /** Function implements menu listener
     * 
     * @param evt event from menu
     */
    private void MenuItemActionPerformed(java.awt.event.ActionEvent evt) {
        scaler.setScalingFactor(evt.getActionCommand());

        // set formatter
        numericTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.NumberFormatter(
                new java.text.DecimalFormat(format + evt.getActionCommand()))));

        // update visible value to new scaling
        numericTextField.setValue(scaler.getScaledValue(value));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        UnitPopupMenu = new javax.swing.JPopupMenu();
        UnitbuttonGroup = new javax.swing.ButtonGroup();
        numericTextField = new javax.swing.JFormattedTextField();

        setBackground(new java.awt.Color(196, 204, 223));
        setBorder(javax.swing.BorderFactory.createTitledBorder("Numeric input"));

        numericTextField.setValue(value);
        numericTextField.setBackground(new java.awt.Color(51, 102, 0));
        numericTextField.setComponentPopupMenu(UnitPopupMenu);
        numericTextField.setForeground(new java.awt.Color(0, 255, 0));
        numericTextField.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        numericTextField.setCaretColor(new java.awt.Color(0, 255, 51));
        numericTextField.setFocusLostBehavior(javax.swing.JFormattedTextField.COMMIT);
        numericTextField.setFont(new java.awt.Font("Dialog", 1, 18));
        numericTextField.setMaximumSize(new java.awt.Dimension(300, 40));
        numericTextField.setMinimumSize(new java.awt.Dimension(300, 40));
        numericTextField.setPreferredSize(new java.awt.Dimension(300, 40));
        numericTextField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                numericTextFieldPropertyChange(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 228, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(numericTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 52, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(numericTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void numericTextFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_numericTextFieldPropertyChange
        // check event and set new value
        if (evt.getPropertyName().equals("value")) {
            try {
                // read current value from control
                double tmp = Double.parseDouble(
                        numericTextField.getValue().toString()) * scaler.getScalingFactorInUse();

                // check upper limit
                if (tmp > maxValue) {
                    value = maxValue;
                    numericTextField.setValue(scaler.getScaledValue(value));
                } else {

                    // check lower limit
                    if (tmp < minValue) {
                        value = minValue;
                        numericTextField.setValue(scaler.getScaledValue(value));
                    } else {
                        // otherwise set new value
                        value = tmp;
                    }
                }

                if (callback != null) {
                    callback.UpdateValue(id, value);
                }
            } catch (NumberFormatException e) {
            // tässä pitää heittää poikkeus eteenpäin...
            }
        }
}//GEN-LAST:event_numericTextFieldPropertyChange
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu UnitPopupMenu;
    private javax.swing.ButtonGroup UnitbuttonGroup;
    private javax.swing.JFormattedTextField numericTextField;
    // End of variables declaration//GEN-END:variables
}