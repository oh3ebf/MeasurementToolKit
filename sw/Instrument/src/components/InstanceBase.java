/***********************************************************
 * Software: instrument client
 * Module:   Instance managing base class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 22.11.2012
 *
 ***********************************************************/
package components;

import interfaces.DesktopInterface;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import murlen.util.fscript.FSExtension;
import org.apache.log4j.Logger;

public class InstanceBase extends javax.swing.JPanel {

    protected String name;
    protected ImageIcon image;
    protected JInternalFrame device;
    protected DesktopInterface desktop;
    protected static Logger logger;
    protected FSExtension scriptExtension;

    /** Creates new form InstanceBase */
    public InstanceBase() {
        // set deafult name
        name = "Dummy";
        device = null;
        desktop = null;
        logger = null;

        // set default icon
        java.net.URL imageURL = this.getClass().getResource("/resources/64x64_unknown.png");

        // load icon image
        if (imageURL != null) {
            image = new ImageIcon(imageURL);
        }

        initComponents();
    }

    /** Function sets icon image
     * 
     * @param img to use as aicon
     * 
     */
    public void setIcon(ImageIcon img) {
        image = img;
    }

    /** Funtion returns name
     * 
     * @return instance name
     * 
     */
    public String getInstanceName() {
        return (name);
    }

    /** Function implements prototype of custom xml handler
     * 
     * @param r instance of XmlReader
     */
    public void loadConfig(XmlReader r) {

    }

    /** Function implements prototype of custom xml writer
     * 
     * @param w instance of XmlWriter
     */
    public void saveConfig(XmlWriter w) {

    }

    /** Function returns scripting interface to component
     * 
     * @return command interface 
     */
    public FSExtension getScriptExtension() {
        return (scriptExtension);
    }

    /** Function draws icons and text
     * 
     * @param g graphics to use drawing
     * 
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            image.paintIcon(this, g, 0, 0);
        }

        g.drawString(name, 2, 75);
    }

    protected void mouseClicked(java.awt.event.MouseEvent evt) {
        // detect double clik
        if (evt.getClickCount() == 2 && !evt.isConsumed()) {
            evt.consume();

            // is device instantiated
            if (device != null) {
                // if device is all ready on desktop, show it
                if (!device.isVisible()) {
                    device.show();
                    return;
                }

                // add new instance to desktop and show it
                desktop.addToDesktop(device);

                Dimension desktopSize = desktop.getSize();
                Dimension deviceSize = device.getSize();
                device.setLocation((desktopSize.width - deviceSize.width) / 2,
                        (desktopSize.height - deviceSize.height) / 2);

                try {
                    // get focus
                    device.setSelected(true);
                } catch (Exception ex) {
                    logger.warn("Cannot get focus: " + name);
                }
            }

            device.show();
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        setMaximumSize(new java.awt.Dimension(64, 80));
        setMinimumSize(new java.awt.Dimension(64, 80));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 82, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
