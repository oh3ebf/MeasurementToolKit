/***********************************************************
 * Software: instrument client
 * Module:   delay function scripting extension class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 27.03.2013
 *
 ***********************************************************/
package scriptEngine;

import java.util.ArrayList;
import murlen.util.fscript.BasicExtension;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSUnsupportedException;
import org.apache.log4j.Logger;

public class SleepExtension extends BasicExtension {

    private static Logger logger;

    public SleepExtension() {
        // get logger instance for this class
        logger = Logger.getLogger(SleepExtension.class);

    }

    /** Function implements script acees funtions
     * 
     * @param name
     * @param params index 0 is device instance name
     *               index 1 is slot number 1 - 5
     *               index 2 is relay number 0 - 15 (depends on installed card)
     *               index 3 is new state 0 / 1
     * @return
     * @throws murlen.util.fscript.FSException
     * 
     */
    @Override
    public Object callFunction(String name, ArrayList params) throws FSException {
        Integer delayTime = 0;
        
        // Funktion call for delay
        if (name.toLowerCase().equals("delay")) {
            try {
                // check for valid parameter
                if (params.get(0) instanceof Integer) {
                    delayTime = (Integer) params.get(0);
                    Thread.sleep(delayTime.intValue() * 1000);
                }
            } catch (Exception ex) {
                logger.error("failed to parse parameters: " + ex.getMessage());
            }
        } else {
            throw new FSUnsupportedException(name);
        }
        return null;
    }

    @Override
    public Object getVar(String name) {
        System.out.println("Getting  " + name);
        return new Integer(0);
    }

    @Override
    public Object getVar(String name, Object index) {
        System.out.println("Getting " + name + "[" + index + "]");
        return new Integer(0);
    }

    @Override
    public void setVar(String name, Object value) {
        System.out.println("Setting " + name + " to " + value);
    }

    @Override
    public void setVar(String name, Object index, Object value) {
        System.out.println("Setting " + name + "[" + index + "] to " + value);
    }
}
