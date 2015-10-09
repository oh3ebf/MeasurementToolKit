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

import components.CanFrame;
import java.text.DecimalFormat;

public class NoCANDoTableRowData {

    private boolean isRoot;
    private CanFrame canF;
    private DecimalFormat timeStampFormatter;
    private String timeFormetter = "####.########";

    public NoCANDoTableRowData(int time_s, int time_us, int id, int flags, short dlc, byte[] data, boolean isLeaf) {
        canF = new CanFrame(time_s, time_us, id, flags, dlc, data);
        this.isRoot = isLeaf;
        timeStampFormatter = new DecimalFormat(timeFormetter);
    }

    public NoCANDoTableRowData(CanFrame f, boolean isLeaf) {
        canF = f;
        this.isRoot = isLeaf;
        timeStampFormatter = new DecimalFormat(timeFormetter);
    }

    /** Function return formatted timestamp
     * 
     * @return time stamp in string
     * 
     */
    public String getTimestamp() {
        return (timeStampFormatter.format(canF.getTimestamp()));
    }

    /** Function returns can frame id
     * 
     * @return the frame id
     * 
     */
    public int getId() {
        return (canF.getId());
    }

    /** Function returns can frame data length
     * 
     * @return the dlc
     * 
     */
    public short getDlc() {
        return (canF.getDlc());
    }

    /** Function returns can frame data bytes
     * 
     * @return the data bytes
     * 
     */
    public byte[] getData() {
        return (canF.getData());
    }

    /** Function returns can frame data bytes as string
     * 
     * @return the data bytes as string
     * 
     */
    public String getDataString() {        
        byte[] data = canF.getData();
        StringBuilder sb = new StringBuilder();

        for (byte b : data) {
            sb.append(String.format("%02d ", b));
        }

        return (sb.toString());
    }

    /** Function returns can frame data bytes as hex formatted string
     * 
     * @return the data bytes as hex string
     * 
     */
    public String getDataHexString() {
        byte[] data = canF.getData();

        StringBuilder sb = new StringBuilder();

        for (byte b : data) {
            sb.append(String.format("%02X ", b));
        }

        return (sb.toString());
    }

    /** Function updates can frame data content
     * 
     * @param time_s time stamp seconds part
     * @param time_us time stamp microseconds part
     * @param data can frame data
     * 
     */
    public void update(int time_s, int time_us, byte[] data) {
        canF.update(time_s, time_us, data);
    }

    /** Function returns row data leaf status
     * 
     * @return the isRoot
     * 
     */
    public boolean isRoot() {
        return (isRoot);
    }

    /** Function sets row data leaf status
     * 
     * @param isRoot the isRoot to set
     * 
     */
    public void setRoot(boolean isLeaf) {
        this.isRoot = isLeaf;
    }
}
