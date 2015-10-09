/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo data view data tree node class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 13.5.2013
 *
 ***********************************************************/
package instruments.can.browser;

import components.CanFrame;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;

public class NoCANDoMutableTreeNode extends DefaultMutableTreeTableNode {
    /* TODO tämä tietää oman id:sä
     */

    public NoCANDoMutableTreeNode() {
        super();
    }

    public NoCANDoMutableTreeNode(Object obj) {
        super(obj);

    // testataan onko data vai signaali
    // interface päivittämiseen???

    }
    /*
    @Override
    public void add(MutableTreeNode newChild) {
    }
    @Override
    public void insert(MutableTreeNode newChild, int childIndex) {
    }
     */

    public CanFrame getCanFrame() {
        // return stored can frame
        if (getUserObject() instanceof CanFrame) {
            return ((CanFrame) getUserObject());
        } else {
            return (null);
        }
    }
}

