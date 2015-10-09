/***********************************************************
 * Software: instrument client
 * Module:   Scripting command interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.03.2013
 *
 ***********************************************************/

package interfaces;


public interface commandExecutionInterface {
    public String getName();
    public boolean executeCmd(int slot, int relay, int state);
    public boolean executeCmd(String cmd, String attr);
    public double readValueDouble();
}
