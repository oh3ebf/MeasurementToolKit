/***********************************************************
 * Software: instrument client
 * Module:   HP54600 instrument instances managing class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 23.10.2012
 *
 ***********************************************************/
package scriptEngine;

import components.InstanceBase;
import interfaces.*;
import javax.swing.JDesktopPane;
import networking.netCommand;
import org.apache.log4j.Logger;
import instruments.*;
import java.util.Hashtable;
import javax.swing.ImageIcon;

public class scriptingInstance extends InstanceBase {

    private MessageInterface msg;

    /** Creates new form scriptingInstance */
    public scriptingInstance(String name, Hashtable<String, String> properties,
            DesktopInterface desktop, MessageInterface msg) {
        // get logger instance for this class
        logger = Logger.getLogger(scriptingInstance.class);

        this.desktop = desktop;
        this.msg = msg;
        this.name = name;

        initComponents();

        // set default icon
        java.net.URL imageURL = this.getClass().getResource("/resources/64x64_script_tool.png");

        // load icon image
        if (imageURL != null) {
            image = new ImageIcon(imageURL);
            this.setIcon(image);
        }

        device = new scriptingEngine();
    }

    public scriptingEngine getScriptEngine() {
        return((scriptingEngine)device);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setPreferredSize(new java.awt.Dimension(64, 80));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());
    }// </editor-fold>//GEN-END:initComponents
    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        mouseClicked(evt);
    }//GEN-LAST:event_formMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
