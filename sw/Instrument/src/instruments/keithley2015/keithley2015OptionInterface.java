/***********************************************************
 * Software: instrument client
 * Module:   Keithley 2015 multimeter options interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 17.4.2013
 *
 ***********************************************************/

package instruments.keithley2015;

public interface keithley2015OptionInterface {
    public void setLoggingState(boolean s);
    public void setLogFilename(String name);
    public void setLogInterval(int i);
}
