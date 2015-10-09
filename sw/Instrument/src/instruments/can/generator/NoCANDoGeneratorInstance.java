/***********************************************************
 * Software: instrument client
 * Module:   NoCANDo generator instance managing class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 3.6.2013
 *
 ***********************************************************/

package instruments.can.generator;

import instruments.hp3488.*;
import components.InstanceBase;
import interfaces.*;
import org.apache.log4j.Logger;
import instruments.*;
import java.util.Hashtable;
import javax.swing.ImageIcon;
import murlen.util.fscript.FSExtension;

public class NoCANDoGeneratorInstance extends InstanceBase {      
    private MessageInterface msg;
    
            
    /** Creates new form HP3480Instance */
    public NoCANDoGeneratorInstance(String name, Hashtable<String, String>properties, 
            DesktopInterface desktop, MessageInterface msg) {
        // get logger instance for this class
        logger = Logger.getLogger(NoCANDoGeneratorInstance.class);
        
        this.desktop = desktop;
        this.msg = msg;
        this.name = name;
                
        initComponents();
        
        // set default icon
        java.net.URL imageURL = this.getClass().getResource("/resources/64x64_switch.png");

        // load icon image
        if (imageURL != null) {
            image = new ImageIcon(imageURL);
            this.setIcon(image);
        }
        
        // create measurement device ui
        device = new NoCANDoGeneratorView(name, properties, msg);
        // create command interface for scripting
        //scriptExtension = new HP3488Extension((commandExecutionInterface)device);
    }
           
    @Override
    public FSExtension getScriptExtension() {
        return(scriptExtension);
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
