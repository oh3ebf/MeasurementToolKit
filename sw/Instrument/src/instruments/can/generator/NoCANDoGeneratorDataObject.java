/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo generator data object class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.7.2013
 *
 ***********************************************************/

package instruments.can.generator;

import components.CanFrame;


public class NoCANDoGeneratorDataObject {

    private CanFrame canData;
    private boolean cyclic;
    private int interval;
    private String bus;

    public NoCANDoGeneratorDataObject() {
        this.canData = new CanFrame();
        this.cyclic = false;
        this.interval = 10;
        this.bus = "none";
    }
    
    public NoCANDoGeneratorDataObject(CanFrame data, boolean cyclic, int interval, String bus) {
        this.canData = data;
        this.cyclic = cyclic;
        this.interval = interval;
        this.bus = bus;
    }
    
    public CanFrame getCanData() {
        return canData;
    }

    public void setCanData(CanFrame canData) {
        this.canData = canData;
    }

    public boolean isCyclic() {
        return cyclic;
    }

    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getBus() {
        return bus;
    }

    public void setBus(String bus) {
        this.bus = bus;
    }
}
