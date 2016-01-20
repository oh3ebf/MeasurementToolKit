/***********************************************************
 * Software: instrument client
 * Module:   HP3488 instrument class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 23.10.2012
 *
 ***********************************************************/
package instruments.hp3488;

import components.DeviceMessage;
import components.XmlReader;
import components.XmlWriter;
import oh3ebf.lib.ui.buttons.*;
import networking.*;
import interfaces.*;
import java.util.Hashtable;
import yami.ParamSet;
import org.apache.log4j.Logger;

public class HP3480 extends javax.swing.JInternalFrame
        implements MessageCallbackInterface, HP3488CardInterface, commandExecutionInterface {

    private static Logger logger;
    private DeviceMessage message;
    private String name;
    private String MessageReset = "Reset";
    private String MessageTest = "Test";
    private String MessageClose = "Close";
    private String MessageOpen = "Open";

    // TODO kortit taulukkoon talteen
    private HP3488Card[] cards;

    /** Creates new form HP3480 */
    public HP3480(String name, Hashtable<String, String> properties, MessageInterface msg) {
        cards = new HP3488Card[5];

        // get logger instance for this class
        logger = Logger.getLogger(HP3480.class);
        // save local instance name
        this.name = name;
        // save messaging context        
        message = new DeviceMessage(name, msg);
        // add message listener
        message.addMessageCallback(name, this);

        initComponents();

        // set button parameters
        resetButton.setColorModel(ModelButton.ModelOrange);
        testButton.setColorModel(ModelButton.ModelNormal);

        // add installed cards
        jPanel3.add(cards[0] = new HP3488Card(0, properties.get("slot1"), this));
        jPanel2.add(cards[1] = new HP3488Card(1, properties.get("slot2"), this));
        jPanel2.add(cards[2] = new HP3488Card(2, properties.get("slot3"), this));
        jPanel2.add(cards[3] = new HP3488Card(3, properties.get("slot4"), this));
        jPanel2.add(cards[4] = new HP3488Card(4, properties.get("slot5"), this));
    }

    /** Function sends action command to device
     * 
     * @param operation if true close operation, otherwise open
     * @param slot to perform command
     * @param ch channel to perform operation
     * 
     */
    public void setLineState(boolean operation, int slot, int ch) {
        if (operation) {
            // close operation
            message.sendValue(MessageClose, String.valueOf(slot + 1) + String.format("%02d", ch));
        } else {
            // open operation
            message.sendValue(MessageOpen, String.valueOf(slot + 1) + String.format("%02d", ch));
        }
    }

    /** Function sends message
     *
     * @param param message parameters
     *
     * @return true/false
     *
     */
    @Override
    public boolean DataStreamCallback(ParamSet param) {
        return (true);
    }

    /** Function return device instance name
     * 
     * @return name in string
     */
    @Override
    public String getName() {
        return (name);
    }

    /** Function handles command request from scripts
     * 
     * @param slot target card for command
     * @param relay number
     * @param state to set on relay
     * 
     * @return true if all ok
     */
    @Override
    public boolean executeCmd(int slot, int relay, int state) {
        // bounds check
        if (slot <= cards.length) {
            // set new state
            boolean s = state == 0 ? false : true;
            cards[slot - 1].setState(relay, s);

            return (true);
        } else {
            // index out of bounds
            logger.warn("too big slot number in script :" + slot);
            return (false);
        }
    }

    /** Function handles command request from scripts
     * 
     * @param cmd
     * @return
     */
    @Override
    public boolean executeCmd(String cmd, String attr) {
        throw new UnsupportedOperationException("Not supported in HP3488");
    }

    /** Function implements callback from script engine value reading 
     * 
     * @return double value
     */
    @Override
    public double readValueDouble() {
        return (0);
    }

    /** Function restores setting from file
     * 
     * @param r reference to xml file reader
     */
    public void loadSettings(XmlReader r) {
        r.parse(name);

        for (int i = 0; i < cards.length; i++) {
            // check if slot contains same card type as stored in file
            if (cards[i].getType() == r.getSlotType(i)) {
                boolean[] rl = r.getSlotRelays(i);
                for (int j = 0; j < rl.length; j++) {
                    // restore relay state
                    cards[i].setState(j, rl[j]);
                }
            }
        }
    }

    /** Function stores scope setting to file
     * 
     * @param file to save
     */
    public void saveSettings(XmlWriter w) {

        for (int i = 0; i < cards.length; i++) {
            // add slot configuration
            w.addSlotSection(name, Integer.toString(cards[i].getSlot()),
                    Integer.toString(cards[i].getType()));

            int count = cards[i].getRelayInUseCount();
            boolean[] states = cards[i].getStates();

            for (int j = 0; j < count; j++) {
                w.addRelaySection(j, states[j]);
            }
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
        jTextField1 = new javax.swing.JTextField();
        resetButton = new oh3ebf.lib.ui.buttons.ModelButton();
        testButton = new oh3ebf.lib.ui.buttons.ModelButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        setBackground(new java.awt.Color(196, 204, 223));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("HP3488a");
        setMaximumSize(new java.awt.Dimension(690, 365));
        setMinimumSize(new java.awt.Dimension(690, 365));
        setPreferredSize(new java.awt.Dimension(690, 365));
        setVisible(true);
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(194, 225, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(250, 140, 19)));
        jPanel1.setPreferredSize(new java.awt.Dimension(280, 0));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextField1.setBackground(new java.awt.Color(51, 102, 0));
        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Dialog", 1, 18));
        jTextField1.setForeground(new java.awt.Color(0, 255, 0));
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jPanel1.add(jTextField1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 305, 40));

        resetButton.setBackground(new java.awt.Color(194, 225, 255));
        resetButton.setText("Reset");
        resetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetButtonActionPerformed(evt);
            }
        });
        jPanel1.add(resetButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 60, 70, 30));

        testButton.setBackground(new java.awt.Color(194, 225, 255));
        testButton.setText("Test");
        testButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testButtonActionPerformed(evt);
            }
        });
        jPanel1.add(testButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, 70, 30));

        jPanel3.setBackground(new java.awt.Color(194, 225, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(250, 140, 19)));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));
        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 331, 80));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(5, 5, 331, 320));

        jPanel2.setBackground(new java.awt.Color(194, 225, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(250, 140, 19)));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));
        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(345, 5, 331, 320));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
    // TODO add your handling code here:
        
    }//GEN-LAST:event_formInternalFrameClosed

    private void resetButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetButtonActionPerformed
        // reset device
        message.sendString(MessageReset);
    // TODO pitäisi nollata ledit
}//GEN-LAST:event_resetButtonActionPerformed

    private void testButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testButtonActionPerformed
        // run self test
        message.sendString(MessageTest);
    }//GEN-LAST:event_testButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private oh3ebf.lib.ui.buttons.ModelButton resetButton;
    private oh3ebf.lib.ui.buttons.ModelButton testButton;
    // End of variables declaration//GEN-END:variables
}
