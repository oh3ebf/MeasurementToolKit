/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo generator signal row class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 5.6.2013
 *
 ***********************************************************/

package instruments.can.interfaces;

import instruments.can.generator.NoCANDoGeneratorRawFrameEditor;
import instruments.can.generator.NoCANDoGeneratorRow;
import instruments.can.generator.NoCANDoGeneratorDataObject;
import instruments.can.*;
import java.util.Hashtable;

public interface NoCANDoGeneratorRowInterface {
    public void addToDesktop(NoCANDoGeneratorRawFrameEditor e);
    public void addNewRow();
    public void removeRow(String id, NoCANDoGeneratorRow r);
    public Hashtable<String, String>getBusProperties();
    public String update(String id, NoCANDoGeneratorDataObject obj);
    public void setMeasurementState(String id, NoCANDoGeneratorDataObject obj, boolean s);
}
