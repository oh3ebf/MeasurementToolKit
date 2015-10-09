/***********************************************************
 * Software: instrument client
 * Module:   data stream callback interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 26.10.2012
 *
 ***********************************************************/

package interfaces;

import yami.ParamSet;


public interface MessageCallbackInterface {
    public boolean DataStreamCallback(ParamSet param);
}
