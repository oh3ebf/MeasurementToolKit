/***********************************************************
 * Software: instrument client
 * Module:   Splash screen class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 25.6.2013
 *
 ***********************************************************/

package components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import org.apache.log4j.Logger;

public class InstrumentSplashScreen extends JWindow implements Runnable {

    private static final long serialVersionUID = 935801891530361293L;
    private static Logger logger;
    private static final Color COLOR_PROGRESS_BACKGROUND = new Color(160, 182, 192);
    private static final Color COLOR_PROGRESS_FORGROUND = new Color(215, 224, 227);
    private BufferedImage splashScreenImage = null;
    private JProgressBar progressbar;
    private volatile int progressValue = 0;
    private boolean exitFlag = false;
    private static InstrumentSplashScreen splash;
    
    private InstrumentSplashScreen(final InputStream theResourceAsStream, int min, int max) {
        super();
        // get new logger instance
        logger = Logger.getLogger(InstrumentSplashScreen.class);
        
        try {
            splashScreenImage = ImageIO.read(theResourceAsStream);
        } catch (final IOException e) {
            logger.error("Can't load splashscreen: " + e.getMessage());
        }
        
        final JPanel contentPanel = new JPanel(new BorderLayout());
        
        contentPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        
        if (min != -1 && max != -1) {
            progressbar = new JProgressBar(SwingConstants.HORIZONTAL, min, max);
        } else {
            progressbar = new JProgressBar(SwingConstants.HORIZONTAL);
            progressbar.setIndeterminate(true);
        }
        
        progressbar.setBackground(COLOR_PROGRESS_BACKGROUND);
        progressbar.setForeground(COLOR_PROGRESS_FORGROUND);
        progressbar.setStringPainted(true);
        
        final JLabel label = new JLabel(new ImageIcon(splashScreenImage));
        
        contentPanel.add(label, BorderLayout.CENTER);
        contentPanel.add(progressbar, BorderLayout.SOUTH);
        add(contentPanel);
        pack();
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }

    /** Function returns splash sreen instance
     * 
     * @return splash screen instance
     * 
     */
    
    public static InstrumentSplashScreen getSplashScreen() {
        return(splash);
    }
    
    /** Function returns splash sreen instance with specified image
     * 
     * @param imageStream to show on screen
     * @param min progress bar value
     * @param max progress bar value
     * @return splash screen instance
     * 
     */
    public static InstrumentSplashScreen getSplashScreen(final InputStream imageStream, int min, int max) {
        if(splash == null) {
            splash = new InstrumentSplashScreen(imageStream, min , max);
        }
        
        return(splash);
    }
    
    /** Function close splash screen
     * 
     */
    
    public void close() {
        setVisible(false);        
        dispose();
    }

    /** Function set progress bar value
     * 
     * @param value to set
     * 
     */
    
    public void setProgress(int value) {
        progressValue = value;       
    }

    /** Function implements thread for progress bar value handler
     * 
     */
    
    public void run() {
        while (exitFlag == false) {
            progressbar.setValue(progressValue);
            
            if (progressValue >= 100) {
                exitFlag = true;
                setVisible(false);
            }
            
            try {
                Thread.sleep(1000);
            } catch (Exception ex) {
            }
        }
    }
}

