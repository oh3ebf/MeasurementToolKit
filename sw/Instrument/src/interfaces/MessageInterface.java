/***********************************************************
 * Software: instrument client
 * Module:   message interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 26.10.2012
 *
 ***********************************************************/

package interfaces;

import lib.common.exceptions.genericException;
import yami.ParamSet;

public interface MessageInterface {
    public boolean addMessageCallback(String name, MessageCallbackInterface msgIf);
    public ParamSet MessageSend(String message, ParamSet param) throws genericException;
}
