/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo frame editor interface class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 25.6.2013
 *
 ***********************************************************/

package instruments.can.interfaces;

import instruments.can.generator.NoCANDoGeneratorDataObject;

public interface NoCANDoFrameEditorInterface {
    public void updateValues(NoCANDoGeneratorDataObject obj);
}
