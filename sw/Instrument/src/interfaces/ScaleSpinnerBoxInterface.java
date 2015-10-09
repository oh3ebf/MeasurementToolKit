/***********************************************************
 * Software: instrument client
 * Module:   Spinner box interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 14.9.2012
 *
 ***********************************************************/
package interfaces;

public interface ScaleSpinnerBoxInterface {
    public void SpinnerBoxisEnabled(int boxNumber, boolean state);
    public void SpinnerBox1ValueChanged(int boxNumber, Object value);
    public void SpinnerBox2ValueChanged(int boxNumber, Object value);
}
