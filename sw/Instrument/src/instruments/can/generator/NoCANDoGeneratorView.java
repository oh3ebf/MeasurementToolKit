/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo signal generator class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.6.2013
 *
 ***********************************************************/
package instruments.can.generator;

import instruments.can.*;
import components.DeviceBase;
import instruments.can.interfaces.NoCANDoGeneratorRowInterface;
import components.DeviceMessage;
import interfaces.MessageInterface;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import yami.ParamSet;

public class NoCANDoGeneratorView extends DeviceBase
        implements NoCANDoGeneratorRowInterface {
    
    private Vector<NoCANDoGeneratorRow> genRows = null;
    private Hashtable<String, String> busProperties;
    private static final String StreamDataOk = "DataOk";

    /** Creates new form NoCANDoGeneratorView */
    public NoCANDoGeneratorView(String name, Hashtable<String, String> properties, MessageInterface msg) {
        super(name, properties, msg);
        
        // get logger instance for this class
        logger = Logger.getLogger(NoCANDoGeneratorView.class);

        /*TODO message kuuntelijoita voi olla useita
         * lisää uusi listener
         * miten name hoidetaan jos useampia???
         * 
         */
        
        // save messaging context        
        message = new DeviceMessage(properties.get("name"), msg);
        // add message listener
        message.addMessageCallback(name, this);

        genRows = new Vector<NoCANDoGeneratorRow>();
        busProperties = properties;

        initComponents();    
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
        logger.debug("data stream parametercount: " + param.getNumberOfEntries());

        try {
            // check for valid message
            if (param.extractString().equals(StreamDataOk)) {
                for (int i = 0; i < param.getNumberOfEntries() - 1; i++) {
                // add or update data to model
                //dataTreeModel.addMessage(param.extractBinary());
                }
            } else {
                logger.warn("streaming data fail");
            }
        } catch (Exception ex) {
            logger.error("failed to extract parameter from response" + ex.getMessage());
        }

        return (true);
    }

    /** Function adds new frame to desktop
     * 
     * @param e frame to add
     * 
     */
    public void addToDesktop(NoCANDoGeneratorRawFrameEditor e) {
        // set loaction on desktop        
        Dimension desktopSize = this.getDesktopPane().getSize();
        Dimension jInternalFrameSize = e.getSize();
        e.setLocation((desktopSize.width - jInternalFrameSize.width) / 2,
                (desktopSize.height - jInternalFrameSize.height) / 2);

        // add dialog to desktop
        this.getParent().add(e);
        // bring dialog to top window
        this.moveToBack();
    }

    /** Function adds new can message to generator
     * 
     */
    public void addNewRow() {
        busProperties.keySet().toArray();

        NoCANDoGeneratorRow r = new NoCANDoGeneratorRow(this);
        genRows.add(r);
        jPanel1.add(r);
        pack();
    }

    /** Function remove can message from generator
     * 
     * @param id of measurement
     * @param r reference to row object
     */
     
    public void removeRow(String id, NoCANDoGeneratorRow r) {
        ParamSet PsetMsg = new ParamSet();
        // remove from server        
        PsetMsg.append(DeviceMessage.MessageMeasurementStop);
        PsetMsg.append((byte) DeviceMessage.MeasurementCmdGenerate);
        PsetMsg.append(id);

        message.commandMessage(r.getDataObject().getBus(), PsetMsg);

        // remove from display
        genRows.remove(r);
        jPanel1.remove(r);
        pack();
    }

    /** Function returns configured can bus interfaces and properties
     * 
     * @return can bus intefaces in hashtable
     */
    public Hashtable<String, String> getBusProperties() {
        return (busProperties);
    }

    /** Function creates or updates generated frame information
     * 
     * @param obj frame information 
     * @return unique id of generator command
     * 
     */
    public String update(String id, NoCANDoGeneratorDataObject obj) {
        ParamSet PsetMsg = new ParamSet();

        // select command
        if ((id == null) || (id.isEmpty())) {
            PsetMsg.append(DeviceMessage.MessageMeasurementStart);
        } else {
            PsetMsg.append(DeviceMessage.MessageMeasurementUpdate);
        }
                
        PsetMsg.append((byte) DeviceMessage.MeasurementCmdGenerate);                
        PsetMsg.append((byte) 0x0);                  // no channel info
        PsetMsg.append(1);                           // boolean inteval in use
        PsetMsg.append(1);                           // boolean measurement enabled
        PsetMsg.append(obj.getInterval());           // interval time
        PsetMsg.append(obj.getCanData().getId());    // can id
        PsetMsg.append(obj.getCanData().getFlags()); // can frame flags
        PsetMsg.append(obj.getCanData().getDlc());   // can frame dlc
        PsetMsg.append(obj.getCanData().getData());  // can frame data
        
        // send id only if initialized all ready
        if (id != null) {
            PsetMsg.append(id);
        }
        
        if (message.commandMessage(obj.getBus(), PsetMsg)) {
            return (message.getUniqueId());
        }

        return ("");
    }

    /** Function set acitive state of measurement
     * 
     * @param obj measurement related can row data
     * @param s new state
     * @param id of measurement
     */
    
    public void setMeasurementState(String id, NoCANDoGeneratorDataObject obj, boolean s) {
        ParamSet PsetMsg = new ParamSet();

        // select new state for measurement
        if (s) {
            // enable measurement command
            PsetMsg.append(DeviceMessage.MessageMeasurementState);
            PsetMsg.append(id);
            PsetMsg.append(1);
        // message.sendValue(, id);
        } else {
             // disable measurement command
            PsetMsg.append(DeviceMessage.MessageMeasurementState);
            PsetMsg.append(id);
            PsetMsg.append(0);
        }
        
        message.commandMessage(obj.getBus(), PsetMsg);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        AddMenuItem = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        AddMenuItem.setText("Add");
        AddMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(AddMenuItem);

        setBackground(new java.awt.Color(196, 204, 223));
        setClosable(true);
        setComponentPopupMenu(jPopupMenu1);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Genix");
        setPreferredSize(new java.awt.Dimension(640, 480));

        jScrollPane1.setBackground(new java.awt.Color(196, 204, 223));

        jPanel1.setBackground(new java.awt.Color(196, 204, 223));
        jPanel1.setComponentPopupMenu(jPopupMenu1);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(jPanel1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(196, 204, 223));

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void AddMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddMenuItemActionPerformed
        // add new message to generator
        addNewRow();        
        
    }//GEN-LAST:event_AddMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AddMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
