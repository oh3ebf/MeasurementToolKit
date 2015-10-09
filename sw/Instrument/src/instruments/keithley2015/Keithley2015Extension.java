/***********************************************************
 * Software: instrument client
 * Module:   Keithley 2015 scripting extension class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 25.04.2013
 *
 ***********************************************************/
package instruments.keithley2015;

import interfaces.commandExecutionInterface;
import java.util.ArrayList;
import murlen.util.fscript.BasicExtension;
import murlen.util.fscript.FSException;
import murlen.util.fscript.FSUnsupportedException;
import org.apache.log4j.Logger;

public class Keithley2015Extension extends BasicExtension {

    private static Logger logger;
    private commandExecutionInterface cmdIf;

    public Keithley2015Extension(commandExecutionInterface iface) {
        // get logger instance for this class
        logger = Logger.getLogger(Keithley2015Extension.class);
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
        String targetName = "";
        String cmd = "";
        String attribute = "";

        // Funktion call for Keithley
        if (name.toLowerCase().equals("keithley2015")) {
            try {
                // check for valid parameter
                if (params.get(0) instanceof String) {
                    // get target instance name
                    targetName = (String) params.get(0);
                    // get command name
                    cmd = (String) params.get(1);

                    // check if there is attributes
                    if (params.size() > 2) {
                        attribute = (String) params.get(2);
                    }

                    // command is targeted for this instance
                    if (targetName.equals(cmdIf.getName())) {
                        if (cmd.toLowerCase().equals("read")) {
                            // read measured value
                            return (cmdIf.readValueDouble());
                        } else {
                            // execute command
                            cmdIf.executeCmd(cmd.toLowerCase(), attribute.toLowerCase());
                        }
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
