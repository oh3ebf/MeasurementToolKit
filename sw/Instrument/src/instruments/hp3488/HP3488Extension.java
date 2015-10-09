/***********************************************************
 * Software: instrument client
 * Module:   HP3488 scripting extension class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.03.2013
 *
 ***********************************************************/
package instruments.hp3488;

import interfaces.commandExecutionInterface;
import java.util.ArrayList;
import murlen.util.fscript.BasicExtension;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSUnsupportedException;
import org.apache.log4j.Logger;

public class HP3488Extension extends BasicExtension {
    private static Logger logger;
    private commandExecutionInterface cmdIf;

    public HP3488Extension(commandExecutionInterface iface) {
        // get logger instance for this class
        logger = Logger.getLogger(HP3488Extension.class);
        // save command interface
        cmdIf = iface;
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
        String targetName;
        Integer slot = 0;
        Integer relay = 0;
        Integer state = 0;

        // Funktion call for HP3488
        if (name.toLowerCase().equals("hp3488")) {
            try {
                // check for valid parameter
                if (params.get(0) instanceof String) {
                    // get target instance name
                    targetName = (String) params.get(0);
                    slot = (Integer) params.get(1);
                    relay = (Integer) params.get(2);
                    state = (Integer) params.get(3);

                    // command is targeted for this instance
                    if (targetName.equals(cmdIf.getName())) {

                        // execute command
                        cmdIf.executeCmd(slot, relay, state);
                    } else {
                        throw new FSUnsupportedException(targetName);
                    }
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
