/***********************************************************
 * Software: instrument client
 * Module:   HP3488 option card interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 4.12.2012
 *
 ***********************************************************/

package instruments.hp3488;

public interface HP3488CardInterface {
    public void setLineState(boolean operation, int slot, int ch);
}
