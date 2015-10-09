/***********************************************************
 * Software: instrument client
 * Module:   frequency units scaling class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 11.10.2013
 *
 ***********************************************************/
package components;

public class frequencyScaler extends unitScalerBase {

    public static final int SCALE_HZ = 0;
    public static final int SCALE_kHZ = 1;
    public static final int SCALE_MHZ = 2;
    public static final int SCALE_GHZ = 3;
    private double scales[] = {1.0D, 1000.0D, 1000000.D, 1000000000.0D};

    public frequencyScaler() {
        super();
        scaleInUse = SCALE_HZ;
    }

    /** Function returns given scaling factor
     * 
     * @param scale index to scales
     * @return scale multiplier at index
     */
    @Override
    public double getScalingFactor(int scale) {

        return (scales[scale]);
    }

    /** Function converts given scale name to value
     * 
     * @param scale name
     * @return scale multiplier
     */
    @Override
    public double getScalingFactor(String scale) {

        if (scale.equals("Hz")) {
            return (scales[0]);
        }
        if (scale.equals("kHz")) {
            return (scales[1]);
        }
        if (scale.equals("MHz")) {
            return (scales[2]);
        }
        if (scale.equals("GHz")) {
            return (scales[3]);
        }

        return (1.0D);
    }

    /** Function returns current scaling factor in use
     * 
     * @return scaling factor
     */
    @Override
    public double getScalingFactorInUse() {
        return (scales[scaleInUse]);
    }

    /** Function sets new scaling factor by name
     * 
     * @param scale name of scaling unit
     */
    
    @Override
    public void setScalingFactor(String scale) {
        if (scale.equals("Hz")) {
            scaleInUse = SCALE_HZ;
            return;
        }
        if (scale.equals("kHz")) {
            scaleInUse = SCALE_kHZ;
            return;
        }
        if (scale.equals("MHz")) {
            scaleInUse = SCALE_MHZ;
            return;
        }
        if (scale.equals("GHz")) {
            scaleInUse = SCALE_GHZ;
            return;
        }
    }

    /** Function returns scaled value, value in basic units
     * 
     * @param value to scale
     * @return scaled value
     */
    
    @Override
    public double getScaledValue(double value) {
        return (value / scales[scaleInUse]);
    }
}
