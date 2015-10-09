/***********************************************************
 * Software: instrument client
 * Module:   instrument xml waveform parser class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 27.12.2013
 *
 ***********************************************************/
package components;

import lib.gui.models.DefaultWaveModel;
import org.apache.log4j.Logger;
import org.jdom.Element;

public class WaveformParserBase {

    protected static Logger logger;
    protected Element waveSet;
            
    public WaveformParserBase() {
        // get logger instance for this class".
        logger = Logger.getLogger(WaveformParserBase.class);
    }
    
    /** Function sets reference to waveform element data
     * 
     * @param e element containing waveform
     */
    
    public void setWaveData(Element e) {
        waveSet = e;
    }
    
    /** Function reads waveform from xml data
     * 
     * @param ch channel to read
     * @param wm reference to wavemodel
     */
    public void getWaveform(int ch, DefaultWaveModel wm) {        
    }
            
}
