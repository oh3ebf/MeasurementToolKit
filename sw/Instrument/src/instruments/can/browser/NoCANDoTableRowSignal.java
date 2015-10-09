/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo data view table row data class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 18.4.2013
 *
 ***********************************************************/
package instruments.can.browser;

public class NoCANDoTableRowSignal {

    private boolean isRoot;
    private String name;
    private int rawValue;

    public NoCANDoTableRowSignal(String name, int value, boolean isLeaf) {

        this.isRoot = isLeaf;
        this.name = name;
        this.rawValue = value;
    }

    /**
     * @return the frame id
     */
    public String getname() {
        return (name);
    }

    /**
     * @return the dlc
     */
    public int getRawValue() {
        return (rawValue);
    }

    /**
     * @return the isRoot
     */
    public boolean isRoot() {
        return (isRoot);
    }

    /**
     * @param isRoot the isRoot to set
     */
    public void setRoot(boolean isLeaf) {
        this.isRoot = isLeaf;
    }
}
