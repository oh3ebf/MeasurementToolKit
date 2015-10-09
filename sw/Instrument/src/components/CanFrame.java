/***********************************************************
 * Software: instrument client
 * Module:   Can message frame class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 19.4.2013
 *
 ***********************************************************/
package components;

public class CanFrame {

    private int id;
    private int flags;
    //private int time_s;
    //private int time_us;
    private long timeVal,  timevalOld;
    private short dlc;
    private byte[] data;
    public static final int MSG_RTR = 0x01;
    public static final int MSG_EXT = 0x02;

    /** Conctructors
     * 
     */
    public CanFrame() {
        //this.time_s = 0;
        //this.time_us = 0;
        timevalOld = timeVal;
        timeVal = 0;
        this.id = 0;
        this.flags = 0;
        this.dlc = 1;
        this.data = new byte[8];
    //timestamp = " " + timeVal;
    }

    public CanFrame(int time_s, int time_us, int id, int flags, short dlc, byte[] data) {
        //this.time_s = time_s;
        //this.time_us = time_us;
        timevalOld = timeVal;
        timeVal = (time_s * 1000000 + time_us) & 0xffffffffL;
        this.id = id;
        this.flags = flags;
        this.dlc = dlc;
        this.data = data;
    //timestamp = time_s + "." + time_us;
    }

    public CanFrame(int id, int flags, short dlc, byte[] data) {
        timevalOld = timeVal;
        timeVal = 0L;
        this.id = id;
        this.flags = flags;
        this.dlc = dlc;
        this.data = data;
    }

    /** Function updates frame
     * 
     * @param time_s
     * @param time_us
     * @param data
     */
    public void update(int time_s, int time_us, byte[] data) {
        //this.time_s = time_s;
        //this.time_us = time_us;
        timevalOld = timeVal;
        timeVal = (time_s * 1000000 + time_us) & 0xffffffffL;
        this.data = data;
    //timestamp = time_s + "." + time_us;
    }

    /** Function returns can frame id
     * 
     * @return id
     * 
     */
    public int getId() {
        return id;
    }

    /** Function sets can frame id
     * 
     * 
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Function returns can frame flags
     * 
     * @return id
     * 
     */
    public int getFlags() {
        return flags;
    }

    /** Function sets can frame flags
     * 
     */
    public void setFlags(int flags) {
        this.flags = flags;
    }

    /** Function returns can frame timestamp
     * 
     * @return time stamp
     * 
     */
    public long getTimestamp() {
        return (timeVal);
    }

    /** Function returns can frame data length
     * 
     * @return data length
     * 
     */
    public short getDlc() {
        return dlc;
    }

    /** Function sets can frame data length
     * 
     */
    public void setDlc(short dlc) {
        this.dlc = dlc;
    }

    /** Function returns can frame data
     * 
     * @return data
     * 
     */
    public byte[] getData() {
        return data;
    }

    /** Function sets can frame data
     * 
     * @param data
     */
     
    public void setData(byte[] data) {
        this.data = data;
    }

     /** Function set message extended flag
      * 
      * @param state of extended flag
      */
    
    public void setExtended(boolean state) {
        if (state) {
            flags |= MSG_EXT;
        } else {
            flags &= ~MSG_EXT;
        }
    }

     /** Function return message extended flag
      * 
      * @return true if extended frame
      * 
      */
    public boolean isExtended() {
        if ((flags & MSG_EXT) == MSG_EXT) {
            return (true);
        }

        return (false);
    }

     /** Function set message RTR flag
      * 
      * @param state of RTR flag
      * 
      */
    
    public void setRtr(boolean state) {
        if (state) {
            flags |= MSG_RTR;
        } else {
            flags &= ~MSG_RTR;
        }
    }
    
    /** Function return message RTR flag
     * 
     * @return RTR state
     * 
     */
    
    public boolean isRtr() {
        if ((flags & MSG_RTR) == MSG_RTR) {
            return (true);
        }

        return (false);
    }
    
    /** Function return time difference between frames
     * 
     * @return time difference in microseconds
     * 
     */
    
    public long getDiffTime() {
        return(timeVal - timevalOld);
    }
    
}
