/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo data view data tree model class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 19.4.2013
 *
 ***********************************************************/
package instruments.can.browser;

import components.CanFrame;
import java.util.Arrays;
import java.util.Hashtable;
import javax.swing.tree.TreePath;
import lib.common.utilities.ByteConverter;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;

public class NoCANDoDataTreeModel extends DefaultTreeTableModel {

    public static final int CAN_TIMESTAMP = 0;
    public static final int CAN_DIFFTIME = 1;
    public static final int CAN_DIR = 2;
    public static final int CAN_CHAN = 3;
    public static final int CAN_NAME = 4;
    public static final int CAN_SEND_NODE = 5;
    public static final int CAN_ID = 6;
    public static final int CAN_DLC = 7;
    public static final int CAN_DATA = 8;
    public static final int CAN_DATABASE = 9;
    public static final int CAN_COUNTER = 10;
    private static Logger logger;
    private String[] titles = {
        "Timestamp", "Diff time", "Dir", "Chan", "Name", "Send node", "Id", "DLC", "Data",
        "Database", "Counter"
    };
    private ByteConverter converter;
    private Hashtable<Integer, NoCANDoMutableTreeNode> canLookUp;
    private NoCANDoMutableTreeNode rootNode;
    private static final int VISUAL_MODE_FIXED_POSITION = 0;
    private static final int VISUAL_MODE_CRONOLOGICAL = 1;
    private int visualMode;
    private boolean cyclicUpdate;
    private boolean pauseUpdate;
    private boolean numberFormat;

    public NoCANDoDataTreeModel() {
        super();
        // module init
        init();

        rootNode = new NoCANDoMutableTreeNode();
        setRoot(rootNode);
    }

    public NoCANDoDataTreeModel(NoCANDoMutableTreeNode root) {
        super();
        // module init
        init();

        // set root of model
        setRoot(root);
    }

    private void init() {
        // get logger instance for this class
        logger = Logger.getLogger(NoCANDoDataTreeModel.class);

        // set default modes
        visualMode = VISUAL_MODE_CRONOLOGICAL;
        cyclicUpdate = false;
        pauseUpdate = false;
        numberFormat = false;

        // get converter instance
        converter = new ByteConverter();
        canLookUp = new Hashtable<Integer, NoCANDoMutableTreeNode>();
    }

    /** Function returns visual mode
     * 
     * @return true if fixed position, false if cronological
     */
    public boolean getVisualMode() {
        if (visualMode == VISUAL_MODE_FIXED_POSITION) {
            return (true);
        } else {
            return (false);
        }
    }

    /** Function sets visual mode
     * 
     * @param mode fixed or cronological
     * 
     */
    public void setVisualMode(boolean mode) {
        if (mode) {
            visualMode = VISUAL_MODE_FIXED_POSITION;
        // TODO tähän uusi rootnode luonti
        } else {
            visualMode = VISUAL_MODE_CRONOLOGICAL;
        }
    }

    /** Function returns state of update mode
     * 
     * @return true if display is in cyclic update mode
     * 
     */
    public boolean isCyclicUpdate() {
        return cyclicUpdate;
    }

    /** Function sets state of update mode
     * 
     * @param cyclicUpdate enable or disable
     * 
     */
    public void setCyclicUpdate(boolean cyclicUpdate) {
        this.cyclicUpdate = cyclicUpdate;
    }

    /** Function return display update enable state
     * 
     * @return tue if stopped
     * 
     */
    public boolean isPauseUpdate() {
        return pauseUpdate;
    }

    /** Function sets display update enable state
     * 
     * @param pauseUpdate if true
     */
    public void setPauseUpdate(boolean pauseUpdate) {
        this.pauseUpdate = pauseUpdate;
    }

    /** Function returns message data format
     * 
     * @return true if hexadecimal
     */
    public boolean isNumberFormat() {
        return numberFormat;
    }

    /** Function sets message data format to decimal or hexadecimal
     * 
     * @param messageFormat true when hexadecimal
     * 
     */
    public void setNumberFormat(boolean format) {
        numberFormat = format;
    }

    /** Function return table Columns
     * 
     * @param column number to get
     * @return column name as string
     * 
     */
    @Override
    public String getColumnName(int column) {
        if (column < titles.length) {
            return (titles[column]);
        } else {
            return "";
        }
    }

    /** Function return number of columns
     * 
     * @return column number
     * 
     */
    @Override
    public int getColumnCount() {
        return titles.length;
    }

    /** Functions return type of column object
     * 
     * @param column index to get
     * @return class type
     * 
     */
    @Override
    public Class getColumnClass(int column) {
        return String.class;
    }

    /** Function returns indexed row values
     * 
     * @param arg0 table tree node
     * @param arg1 index to treenode row object
     * @return
     */
    @Override
    public Object getValueAt(Object arg0, int arg1) {
        // check type of table node parameter
        if (arg0 instanceof NoCANDoMutableTreeNode) {
            // access it as can node
            NoCANDoMutableTreeNode dataNode = (NoCANDoMutableTreeNode) arg0;

            // handling for can frame insces
            if (dataNode.getUserObject() instanceof NoCANDoTableRowData) {
                NoCANDoTableRowData data = (NoCANDoTableRowData) dataNode.getUserObject();

                if (data != null) {
                    switch (arg1) {
                        case CAN_TIMESTAMP:
                            return data.getTimestamp();
                        case CAN_DIFFTIME:

                        case CAN_DIR:

                        case CAN_CHAN:

                        case CAN_NAME:

                        case CAN_SEND_NODE:
                            break;
                        case CAN_ID:
                            return data.getId();
                        case CAN_DLC:
                            return data.getDlc();
                        case CAN_DATA:
                            if (numberFormat) {
                                return data.getDataHexString();
                            } else {
                                return data.getDataString();
                            }
                        case CAN_DATABASE:

                        case CAN_COUNTER:
                            break;
                    }
                }
            }

            // handling for signal instances
            if (dataNode.getUserObject() instanceof NoCANDoTableRowSignal) {
                NoCANDoTableRowSignal signal = (NoCANDoTableRowSignal) dataNode.getUserObject();

                if (signal != null) {
                    switch (arg1) {
                        case 0:
                            return signal.getname();
                        case 1:
                            return signal.getRawValue();
                    }
                }
            }

        }
        return null;
    }

    /** Function returns child object of given node
     * 
     * @param arg0 tree node 
     * @param arg1 location of child node
     * @return
     */
    @Override
    public Object getChild(Object arg0, int arg1) {
        if (arg0 instanceof NoCANDoMutableTreeNode) {
            return (rootNode.getChildAt(arg1));
        }
        
        return null;
    }

    /** Function return child count of given node
     * 
     * @param arg0 tree node
     * @return number of childs
     * 
     */
    @Override
    public int getChildCount(Object arg0) {
        if (arg0 instanceof NoCANDoMutableTreeNode) {
            return (rootNode.getChildCount());
        }
        
        return 0;
    }

    /** Function adds ofr uppdates can message treenode 
     * 
     * @param data can frame data in byte array
     * 
     */
    public void addMessage(byte[] data) {
        NoCANDoMutableTreeNode node = null;
        CanFrame frame = null;

        // parse nessage to variables
        int time_s = converter.getInt(0, data);
        int time_us = converter.getInt(4, data);
        int id = converter.getInt(8, data);
        int flags = converter.getInt(12, data);
        short dlc = converter.getShort(16, data);
        byte[] d = Arrays.copyOfRange(data, 18, 26);

        //System.out.println("time : " + time_s + "." + time_us);

        switch (visualMode) {
            case VISUAL_MODE_CRONOLOGICAL:
                // new can frame
                frame = new CanFrame(time_s, time_us, id, flags, dlc, d);

                // make new node and add to tree
                node = new NoCANDoMutableTreeNode(new NoCANDoTableRowData(frame, true));
                canLookUp.put(id, node);
                rootNode.add(node);

                // remove nodes if max node count reached
                if (rootNode.getChildCount() >= 200) {
                    rootNode.remove(0);
                }

                // check update rules
                if (!pauseUpdate) {
                    // update tree table      
                    modelSupport.fireNewRoot();
                }
                break;
            case VISUAL_MODE_FIXED_POSITION:
                // check if message exist in table
                if (canLookUp.containsKey(id)) {
                    // update message data
                    node = canLookUp.get(id);

                    // TODO data pitää myös päivittää...
                    ((NoCANDoTableRowData) node.getUserObject()).update(time_s, time_us, d);
                    TreeTableNode parent = node.getParent();
                    //modelSupport.fireChildChanged(new TreePath(getPathToRoot(parent)), rootNode.getIndex(node), root);

                    // check update rules
                    if (!pauseUpdate) {
                        // update tree table                        
                        modelSupport.firePathChanged(new TreePath(getPathToRoot(parent)));
                    }
                } else {
                    try {
                        // new can frame
                        frame = new CanFrame(time_s, time_us, id, flags, dlc, d);
                        // make new node and add to tree
                        node = new NoCANDoMutableTreeNode(new NoCANDoTableRowData(frame, true));
                        canLookUp.put(id, node);
                        rootNode.add(node);

                        //TreeTableNode parent = node.getParent();
                        //modelSupport.firePathChanged(new TreePath(getPathToRoot(parent)));
                        //modelSupport.fireTreeStructureChanged(new TreePath(getPathToRoot(parent)));
                        // check update rules

                        // check update rules
                        if (!pauseUpdate) {
                            // update tree table
                            modelSupport.fireNewRoot();
                        }
                    } catch (Exception ex) {
                        logger.error("failed to add new canframe in TreeTable" + ex.getMessage());
                        ex.printStackTrace();
                    }
                }
                break;
        }
    }
}
