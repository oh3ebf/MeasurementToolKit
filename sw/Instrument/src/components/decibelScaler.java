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

public class decibelScaler extends unitScalerBase {
    
    
    public decibelScaler() {
        super();
    }
   
    /** Function returns given scaling factor
     * 
     * @param scale index to scales
     * @return scale multiplier at index
     */
    
    @Override    
    public double getScalingFactor(int scale) {
        return(1.0D);
    }
    
    /** Function converts given scale name to value
     * 
     * @param scale name
     * @return scale multiplier
     */    
    
    @Override  
    public double getScalingFactor(String scale) {
        return(1.0D);
    }
    
    /** Function returns current scaling factor in use
     * 
     * @return scaling factor
     */  
    
    @Override  
    public double getScalingFactorInUse() {     
        return (1.0D);
    }
    
    /** Function sets new scaling factor by name
     * 
     * @param scale name of scaling unit
     */ 
    
    @Override  
    public void setScalingFactor(String scale) {
        
    }   
    
    /** Function returns scaled value, value in basic units
     * 
     * @param value to scale
     * @return scaled value
     */     
    @Override  
     public double getScaledValue(double value) {
        return (value);
    }
}
