/***********************************************************
 * Software: instrument client
 * Module:   UI main class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 23.10.2012
 *
 ***********************************************************/

package interfaces;

import java.awt.Dimension;
import javax.swing.JInternalFrame;


public interface DesktopInterface {
    public boolean addToDesktop(JInternalFrame fr);
    public boolean removeFromDesktop(JInternalFrame fr);
    public Dimension getSize();
}
