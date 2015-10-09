/***********************************************************
 * Software: instrument client
 * Module:   HP 8922 instrument instances managing class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 16.9.2013
 *
 ***********************************************************/
package instruments.hp8922;

import components.InstanceBase;
import interfaces.*;
import org.apache.log4j.Logger;
import instruments.*;
import java.util.Hashtable;
import javax.swing.ImageIcon;

public class HP8922Instance extends InstanceBase {

    private MessageInterface msg;

    /** Creates new form E4421Instance */
    public HP8922Instance(String name, Hashtable<String, String> properties,
            DesktopInterface desktop, MessageInterface msg) {
        // get logger instance for this class
        logger = Logger.getLogger(HP8922Instance.class);

        this.desktop = desktop;
        this.msg = msg;
        this.name = name;

        initComponents();

        // set default icon
        java.net.URL imageURL = this.getClass().getResource("/resources/64x64_mobile_networks.png");

        // load icon image
        if (imageURL != null) {
            image = new ImageIcon(imageURL);
            this.setIcon(image);
        }
        
        device = new HP8922(name, properties, msg);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setToolTipText("HP8922 GSM/PCS/PCN Testset");
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
