/***********************************************************
 * Software: instrument client
 * Module:   unit scaling base class class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 11.10.2013
 *
 ***********************************************************/
package components;

public class unitScalerBase {
    protected int scaleInUse = 0;    
    
    public unitScalerBase() {
    }
    
    
    
    public double getScalingFactor(int scale) {
        return(0.0D);
    }

    public double getScalingFactor(String scale) {
        return(0.0D);
    }
    
    public double getScalingFactorInUse() {     
        return (1.0D);
    }
    
    public void setScalingFactor(String scale) {
        
    }
    
     public void setScalingFactor(int scale) {
        scaleInUse = scale;
    }
     
     public double getScaledValue(double value) {
        return (value);
    }
}
