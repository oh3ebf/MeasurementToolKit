/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo data view class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 16.4.2013
 *
 ***********************************************************/
package instruments.can.browser;

import components.*;
import interfaces.MessageInterface;
import java.util.Hashtable;
import org.apache.log4j.Logger;
import yami.ParamSet;

public class NoCANDoDataView extends DeviceBase {
    //private MessageInterface msgIf;
   
    private int interval = 100;
    private NoCANDoDataTreeModel dataTreeModel;   
    private static final String ColTimestampVisible = "nocando.timestamp_visible";
    private static final String ColDiffTimeVisible = "nocando.difftime_visible";
    private static final String ColDirectionVisible = "nocando.direction_visible";
    private static final String ColChannelVisible = "nocando.channel_visible";
    private static final String ColNameVisible = "nocando.name_visible";
    private static final String ColSendNodeVisible = "nocando.sendnode_visible";
    private static final String ColIdVisible = "nocando.id_visible";
    private static final String ColDlcVisible = "nocando.dlc_visible";
    private static final String ColDataVisible = "nocando.data_visible";
    private static final String ColDatabaseVisible = "nocando.database_visible";
    private static final String ColCounterVisible = "nocando.counter_visible";

    public static final int MeasurementCmdCapture = 1;
    public static final int MeasurementCmdGenerate = 2;    
    public static final String MessageMeasurementState = "MeasurementState";        
    
    /** Creates new form CAN bus data view */
    public NoCANDoDataView(String name, Hashtable<String, String> properties, MessageInterface msg) {
        super(name, properties, msg);
        // get logger instance for this class
        logger = Logger.getLogger(NoCANDoDataView.class);
        
        /*TODO message kuuntelijoita voi olla useita
         * lisää uusi listener
         * miten name hoidetaan jos useamapia???
         * 
         */
        
        
        // save messaging context        
        message = new DeviceMessage(properties.get("name"), msg);
        // add message listener
        message.addMessageCallback(name, this);
        
        initComponents();

        // new data model
        dataTreeModel = new NoCANDoDataTreeModel();

        // load settings
        update();

        jXTreeTable1.setTreeTableModel(dataTreeModel);
        jXTreeTable1.setAutoCreateColumnsFromModel(false);
        jXTreeTable1.getColumnModel().getColumn(NoCANDoDataTreeModel.CAN_TIMESTAMP).setPreferredWidth(175);
        jXTreeTable1.getColumnModel().getColumn(NoCANDoDataTreeModel.CAN_ID).setPreferredWidth(25);
        jXTreeTable1.getColumnModel().getColumn(NoCANDoDataTreeModel.CAN_DLC).setPreferredWidth(40);
        jXTreeTable1.getColumnModel().getColumn(NoCANDoDataTreeModel.CAN_DATA).setPreferredWidth(250);

        //
        jXTreeTable1.validate();
        capture(true);
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
            if (param.extractString().equals(DeviceMessage.StreamDataOk)) {
                for (int i = 0; i < param.getNumberOfEntries() - 1; i++) {
                    // add or update data to model
                    dataTreeModel.addMessage(param.extractBinary());
                }
            } else {
                logger.warn("streaming data fail");
            }
        } catch (Exception ex) {
            logger.error("failed to extract parameter from response" + ex.getMessage());
        }

        return (true);
    }

    /** Function reads setting from file
     * 
     */
    @Override
    public void update() {
        try {
            // new data model
            dataTreeModel = new NoCANDoDataTreeModel();
            jXTreeTable1.setTreeTableModel(dataTreeModel);

            // set stored variables
            dataTreeModel.setVisualMode(config.getBoolean(NoCanDoDataViewOptions.PARAM_DISPLAY_MODE));
            dataTreeModel.setCyclicUpdate(config.getBoolean(NoCanDoDataViewOptions.PARAM_DISPLAY_UPDATE));
            config.getBoolean(NoCanDoDataViewOptions.PARAM_TIME_FORMAT);
            config.getBoolean(NoCanDoDataViewOptions.PARAM_MESSAGE_FORMAT);
            dataTreeModel.setNumberFormat(config.getBoolean(NoCanDoDataViewOptions.PARAM_NUMBER_FORMAT));

            // set visible columns
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_TIMESTAMP).setVisible(config.getBoolean(ColTimestampVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DIFFTIME).setVisible(config.getBoolean(ColDiffTimeVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DIR).setVisible(config.getBoolean(ColDirectionVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_CHAN).setVisible(config.getBoolean(ColChannelVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_NAME).setVisible(config.getBoolean(ColNameVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_SEND_NODE).setVisible(config.getBoolean(ColSendNodeVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_ID).setVisible(config.getBoolean(ColIdVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DLC).setVisible(config.getBoolean(ColDlcVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DATA).setVisible(config.getBoolean(ColDataVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DATABASE).setVisible(config.getBoolean(ColDatabaseVisible));
            jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_COUNTER).setVisible(config.getBoolean(ColCounterVisible));

        } catch (Exception ex) {
            logger.warn("parameter not found from config: " + ex.getMessage());
        }
    }

    /** Function saves current state of visible columns
     * 
     */
    private void storeVisibility() {
        // set visible columns
        config.setProperty(ColTimestampVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_TIMESTAMP).isVisible());
        config.setProperty(ColDiffTimeVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DIFFTIME).isVisible());
        config.setProperty(ColDirectionVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DIR).isVisible());
        config.setProperty(ColChannelVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_CHAN).isVisible());
        config.setProperty(ColNameVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_NAME).isVisible());
        config.setProperty(ColSendNodeVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_SEND_NODE).isVisible());
        config.setProperty(ColIdVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_ID).isVisible());
        config.setProperty(ColDlcVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DLC).isVisible());
        config.setProperty(ColDataVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DATA).isVisible());
        config.setProperty(ColDatabaseVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_DATABASE).isVisible());
        config.setProperty(ColCounterVisible, jXTreeTable1.getColumnExt(NoCANDoDataTreeModel.CAN_COUNTER).isVisible());

    }

       
    /** Function handles capture request to device
     * 
     * @param state of measuring action
     */
    private void capture(boolean state) {
        if (state) {
            // disable option editing during capture
            //optionsMenuItem.setEnabled(false);
            // start log writing
            //((loggingDocument) doc).openLog();
            ParamSet PsetMsg = new ParamSet();

            // command
            PsetMsg.append(DeviceMessage.MessageMeasurementStart);
            PsetMsg.append((byte) MeasurementCmdCapture);
            PsetMsg.append((byte) 0x0);                  // no channel info
            PsetMsg.append(1);                           // boolean inteval in use
            PsetMsg.append(1);                           // boolean measurement enabled
            PsetMsg.append(interval);                    // interval time
            message.commandMessage(PsetMsg);
        } else {
            // enable option editing
            //optionsMenuItem.setEnabled(true);
            // stop measurement
            message.sendValue(DeviceMessage.MessageMeasurementStop, (byte)MeasurementCmdCapture);
        // stop log writing
        //((loggingDocument) doc).closeLog();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        ConfigMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        PauseCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jXTreeTable1 = new org.jdesktop.swingx.JXTreeTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        ConfigMenuItem.setText("Config");
        ConfigMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConfigMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ConfigMenuItem);
        jPopupMenu1.add(jSeparator1);

        PauseCheckBoxMenuItem.setText("Pause");
        PauseCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PauseCheckBoxMenuItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(PauseCheckBoxMenuItem);

        setBackground(new java.awt.Color(196, 204, 223));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Browser");
        setPreferredSize(new java.awt.Dimension(640, 480));
        setVisible(true);

        jScrollPane1.setBackground(new java.awt.Color(196, 204, 223));

        jXTreeTable1.setComponentPopupMenu(jPopupMenu1);
        jXTreeTable1.setAutoStartEditOnKeyStroke(false);
        jXTreeTable1.setColumnControlVisible(true);
        jXTreeTable1.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        jXTreeTable1.setDoubleBuffered(true);
        jXTreeTable1.setEditable(false);
        jXTreeTable1.setHorizontalScrollEnabled(true);
        jXTreeTable1.setLargeModel(true);
        jXTreeTable1.setLeafIcon(null);
        jXTreeTable1.setRequestFocusEnabled(false);
        jXTreeTable1.setRolloverEnabled(false);
        jXTreeTable1.setRowSelectionAllowed(false);
        jXTreeTable1.setShowGrid(false);
        jXTreeTable1.setSortsOnUpdates(false);
        jXTreeTable1.setUpdateSelectionOnSort(false);
        jXTreeTable1.setVerifyInputWhenFocusTarget(false);
        jXTreeTable1.setVisibleRowCount(200);
        jScrollPane1.setViewportView(jXTreeTable1);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(196, 204, 223));

        jMenu1.setText("File");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void ConfigMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConfigMenuItemActionPerformed
        // show options
        addToDesktop(new NoCanDoDataViewOptions(this));                   
    }//GEN-LAST:event_ConfigMenuItemActionPerformed

    private void PauseCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PauseCheckBoxMenuItemActionPerformed
        if (PauseCheckBoxMenuItem.isSelected()) {
            // pause display
            dataTreeModel.setPauseUpdate(true);
        } else {
            // continue update frames
            dataTreeModel.setPauseUpdate(false);
        }
    }//GEN-LAST:event_PauseCheckBoxMenuItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ConfigMenuItem;
    private javax.swing.JCheckBoxMenuItem PauseCheckBoxMenuItem;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private org.jdesktop.swingx.JXTreeTable jXTreeTable1;
    // End of variables declaration//GEN-END:variables
}
