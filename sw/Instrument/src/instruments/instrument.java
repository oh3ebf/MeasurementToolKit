/***********************************************************
 * Software: instrument client
 * Module:   UI main class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 18.11.2003
 *
 ***********************************************************/
package instruments;

import instruments.can.generator.NoCANDoGeneratorInstance;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;
import networking.netCommand;
import components.*;
import instruments.agilent4421b.E4421BInstance;
import instruments.can.browser.*;
import interfaces.*;
import instruments.hp3488.HP3480Instance;
import instruments.hp54600.HP54600Instance;
import instruments.hp8922.HP8922Instance;
import instruments.keithley2015.keithley2015Instance;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import javax.swing.UIManager.LookAndFeelInfo;
import oh3ebf.lib.ui.fileWidgets.AdvancedFileChooser;
import oh3ebf.lib.ui.panels.GradientDesktopPane;
import oh3ebf.lib.common.utilities.ConfigurationInstance;
import scriptEngine.scriptingInstance;

public class instrument extends javax.swing.JFrame implements DesktopInterface, Runnable {

    private DSO40 scope;
    private LA40 analyser;
    private boolean Logic40Active = false;
    private boolean DSO40Active = false;
    private netCommand cmd;
    private Hashtable<String, JInternalFrame> windows;
    private static Logger logger;
    private ConfigurationInstance config;
    private boolean configReady = false;
    private Hashtable<String, configObject> deviceConfigs;
    private scriptingInstance en;
    private File currentDir;
    private String settingsFile = "";
    private static final String SERVER_IP = "server.ip";
    private static final String SERVER_NAME = "server.name";
    private static final String SERVER_PORT = "server.port";
    private static final String CLIENT_NAME = "client.name";
    private static final String CLIENT_OBJECT = "client.object";
    private static final String CLIENT_PORT = "client.port";
    private static final String CLIENT_DEVICES = "client.devices";
    private static final String INSTRUMENT_SPLASH = "instrument.splash_screen";
    private static final String INSTRUMENT_LOOKANDFEEL = "instrument.look_and_feel";
    private static final String UI_INSTRUMENTS = "instrumentBox";

    /** Creates new form instrument */
    public instrument() {
        // strorage for subwindows
        windows = new Hashtable<String, JInternalFrame>();

        // get new logger instance
        logger = Logger.getLogger(instrument.class);
        // Set up a simple configuration that logs on the console.
        BasicConfigurator.configure();

        // TODO add command line parameter reading
        config = ConfigurationInstance.getConfiguration("instrument.cfg");


        try {
            // set look and feel of application
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (config.getString(INSTRUMENT_LOOKANDFEEL).equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            logger.warn("look and feel not found " + ex);
        }

        // create main userinterface
        initComponents();

        // start component initializer
        new Thread(this).start();
    }

    /** Function implements background initializer
     * 
     */
    public void run() {
        InstrumentSplashScreen s = InstrumentSplashScreen.getSplashScreen();
        // set visibility from configuration
        s.setVisible(config.getBoolean(INSTRUMENT_SPLASH));

        s.setProgress(0);

        s.setProgress(10);

        try {
            // connect to server if exist
            cmd = new netCommand(config.getString(SERVER_NAME), config.getString(SERVER_IP),
                    config.getInt(SERVER_PORT), config.getString(CLIENT_NAME), config.getString(CLIENT_OBJECT),
                    config.getInt(CLIENT_PORT));

            // open connection
            if (cmd.Open()) {
                // make connection by sw click
                ConnectButton.doClick();
            }
        } catch (Exception ex) {
            logger.error("exception when connecting to server : " + ex.getMessage());
        }

        s.setProgress(25);

        desktop.setUI(new GradientDesktopPane());
        GradientDesktopPane.setBackground(desktop, new Color(164, 188, 234), new Color(96, 126, 198));

        windows.put(UI_INSTRUMENTS, new InstrumentBox("Instruments"));
        windows.get(UI_INSTRUMENTS).setVisible(false);
        desktop.add(windows.get(UI_INSTRUMENTS));
        GpibToggleButton.setSelected(false);

        windows.put("canBox", new InstrumentBox("Can tools"));
        windows.get("canBox").setVisible(false);
        desktop.add(windows.get("canBox"));
        CanToggleButton.setSelected(false);

        windows.put("toolsBox", new InstrumentBox("General tools"));
        windows.get("toolsBox").setVisible(false);
        desktop.add(windows.get("toolsBox"));
        ToolsToggleButton.setSelected(false);

        try {
            // read server configuration
            deviceConfigs = cmd.getServerConfig();
        } catch (Exception e) {
            logger.error("failed to read server configuration:" + e.getMessage());
        }

        // configure tools visual components
        setToolsConfig();
        s.setProgress(50);

        // configure gpib visual components
        setDeviceConfig(config.getList("client.devices"));

        s.setProgress(75);

        // configure CAN bus visual components
        setNoCANDoConfig();

        // resize window
        //setSize(1024, 768);
        //setLocationRelativeTo(null);
        s.setProgress(100);
    }

    /** Function fill GPIB window
     * 
     * @param support list of supported devices
     * 
     */
    private void setDeviceConfig(List<Object> support) {
        //Hashtable<String, configObject> deviceConfigs;
        InstanceBase instance = null;

        // tässä pitäisi hakea skripti kone ref

        /* TODO: 
         * luetaan kerran yhteyden avaamisen yhteydessä jos ei ole aiemmin tehty.
         * tallenetaan levylle.
         * avataan viimeisin käynnistyksen yhteydessä
         * verrataan serverin conffiin jos erilainen kysy päivitetäänkö uuteen
         * 
         */

        if (support == null) {
            logger.warn("list of supported devices missing");
            return;
        }

        if (configReady == false) {
            JInternalFrame IBox = windows.get(UI_INSTRUMENTS);
            try {
                Enumeration e = deviceConfigs.keys();

                while (e.hasMoreElements()) {
                    // get one device configuration
                    configObject device = deviceConfigs.get(e.nextElement());
                    String type = device.getType();

                    // loop configured devices and create instances
                    if (support.contains(type)) {
                        // check if device type is supported
                        if (type.equals("HP3488")) {
                            instance = new HP3480Instance(device.getName(), device.getProperties(), this, cmd);
                            device.setInstance(instance);
                            en.getScriptEngine().addExtension(instance.getScriptExtension());
                            IBox.add(instance);
                        }

                        // check if device type is supported
                        if (type.equals("HP54600")) {
                            instance = new HP54600Instance(device.getName(), device.getProperties(), this, cmd);
                            device.setInstance(instance);
                            IBox.add(instance);
                        }

                        // check if device type is supported
                        if (type.equals("HP8922")) {
                            instance = new HP8922Instance(device.getName(), device.getProperties(), this, cmd);
                            device.setInstance(instance);
                            IBox.add(instance);
                        }

                        // check if device type is supported
                        if (type.equals("E4421B")) {
                            instance = new E4421BInstance(device.getName(), device.getProperties(), this, cmd);
                            device.setInstance(instance);
                            IBox.add(instance);
                        }

                        // check if device type is supported
                        if (type.equals("keithley2015")) {
                            instance = new keithley2015Instance(device.getName(), device.getProperties(), this, cmd);
                            device.setInstance(instance);
                            en.getScriptEngine().addExtension(instance.getScriptExtension());
                            IBox.add(instance);
                        }

                        // config done
                        configReady = true;
                    } else {
                        logger.warn("device type " + type + " is not supported yet!");
                    }
                }

                pack();
            } catch (Exception e) {
                logger.error("failed to initialize component:" + e.getMessage());
            }
        }
    }

    /** Function fills tool box window
     * 
     */
    private void setToolsConfig() {
        Hashtable<String, String> a = new Hashtable<String, String>();

        // miten tool palikat tallennetaan????

        JInternalFrame toolBox = windows.get("toolsBox");
        en = new scriptingInstance("Script", a, this, cmd);
        toolBox.add(en);

    }

    /** Function fills CAN box window
     * 
     */
    private void setNoCANDoConfig() {
        Hashtable<String, String> busProperties = new Hashtable<String, String>();

        try {
            // get server configuration
            Enumeration e = deviceConfigs.keys();
            while (e.hasMoreElements()) {
                // get one device configuration
                configObject device = deviceConfigs.get(e.nextElement());

                if (device.getType().equals("lincan")) {
                    busProperties.put("type", device.getType());
                    busProperties.put("name", device.getName());
                    
                }
            }
        } catch (Exception e) {
            logger.error("failed to initialize component:" + e.getMessage());
        }

        JInternalFrame toolBox = windows.get("canBox");

        toolBox.add(new NoCANDoDataViewInstance("Browser", busProperties, this, cmd));
        toolBox.add(new NoCANDoGeneratorInstance("Genix", busProperties, this, cmd));
    }

    /** Function add new component to desktop
     * 
     * @param fr reference to new object
     * 
     * @return true if ok, otherwise false
     * 
     */
    public boolean addToDesktop(JInternalFrame fr) {
        desktop.add(fr);
        return (true);
    }

    /** Function removes object from desktop
     * 
     * @param fr reference to removed object
     * 
     * @return true if ok, otherwise false
     * 
     */
    public boolean removeFromDesktop(JInternalFrame fr) {
        desktop.remove(fr);
        return (true);
    }

    /** Function returns current desktop size
     * 
     * @return desktop size
     */
    @Override
    public Dimension getSize() {
        return (desktop.getSize());
    }

    /* Function closes application
     *
     * Parameters:
     *
     * Returns:
     *
     */
    private void exitApplication() {
        try {
            // disconnec from instrument server
            cmd.Disconnect();
        } catch (Exception e) {
            logger.warn("failed to disconnect from server");
        }

        // close window and exit
        dispose();
        System.exit(0);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        ConnectButton = new javax.swing.JButton();
        GpibToggleButton = new oh3ebf.lib.ui.buttons.CheckToggleButton();
        CanToggleButton = new oh3ebf.lib.ui.buttons.CheckToggleButton();
        ToolsToggleButton = new oh3ebf.lib.ui.buttons.CheckToggleButton();
        desktop = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        FileMenu = new javax.swing.JMenu();
        OpenMenuItem = new javax.swing.JMenuItem();
        SaveMenuItem = new javax.swing.JMenuItem();
        SaveAsMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        PrintMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        ExitMenuItem = new javax.swing.JMenuItem();
        ToolsMenu = new javax.swing.JMenu();
        OptionsMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        WindowMenu = new javax.swing.JMenu();
        HelpMenu = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        AboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Measurement Toolkit");
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(1024, 768));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(196, 204, 223));
        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        ConnectButton.setText("Connect");
        ConnectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConnectButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ConnectButton);

        GpibToggleButton.setText("GPIB");
        GpibToggleButton.setMaximumSize(new java.awt.Dimension(70, 24));
        GpibToggleButton.setMinimumSize(new java.awt.Dimension(70, 24));
        GpibToggleButton.setPreferredSize(new java.awt.Dimension(70, 24));
        GpibToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GpibToggleButtonActionPerformed(evt);
            }
        });
        jPanel3.add(GpibToggleButton);

        CanToggleButton.setText("CAN");
        CanToggleButton.setMaximumSize(new java.awt.Dimension(70, 24));
        CanToggleButton.setMinimumSize(new java.awt.Dimension(70, 24));
        CanToggleButton.setPreferredSize(new java.awt.Dimension(70, 24));
        CanToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CanToggleButtonActionPerformed(evt);
            }
        });
        jPanel3.add(CanToggleButton);

        ToolsToggleButton.setText("Tools");
        ToolsToggleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ToolsToggleButtonActionPerformed(evt);
            }
        });
        jPanel3.add(ToolsToggleButton);

        getContentPane().add(jPanel3, java.awt.BorderLayout.NORTH);
        getContentPane().add(desktop, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(196, 204, 223));

        FileMenu.setBackground(new java.awt.Color(196, 204, 223));
        FileMenu.setText("File");

        OpenMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        OpenMenuItem.setText("Open");
        OpenMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OpenMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(OpenMenuItem);

        SaveMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        SaveMenuItem.setText("Save");
        SaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(SaveMenuItem);

        SaveAsMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        SaveAsMenuItem.setText("Save As ...");
        SaveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveAsMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(SaveAsMenuItem);
        FileMenu.add(jSeparator1);

        PrintMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        PrintMenuItem.setText("Print...");
        PrintMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(PrintMenuItem);
        FileMenu.add(jSeparator3);

        ExitMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        ExitMenuItem.setText("Exit");
        ExitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ExitMenuItemActionPerformed(evt);
            }
        });
        FileMenu.add(ExitMenuItem);

        jMenuBar1.add(FileMenu);

        ToolsMenu.setBackground(new java.awt.Color(196, 204, 223));
        ToolsMenu.setText("Tools");

        OptionsMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        OptionsMenuItem.setText("Options");
        OptionsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OptionsMenuItemActionPerformed(evt);
            }
        });
        ToolsMenu.add(OptionsMenuItem);

        jMenuItem1.setBackground(new java.awt.Color(196, 204, 223));
        jMenuItem1.setText("Jython");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        ToolsMenu.add(jMenuItem1);

        jMenuBar1.add(ToolsMenu);

        WindowMenu.setText("Window");
        jMenuBar1.add(WindowMenu);

        HelpMenu.setBackground(new java.awt.Color(196, 204, 223));
        HelpMenu.setText("Help");

        jMenuItem7.setBackground(new java.awt.Color(196, 204, 223));
        jMenuItem7.setText("Contents");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        HelpMenu.add(jMenuItem7);
        HelpMenu.add(jSeparator2);

        AboutMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        AboutMenuItem.setText("About");
        AboutMenuItem.setToolTipText("Information about this software");
        AboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AboutMenuItemActionPerformed(evt);
            }
        });
        HelpMenu.add(AboutMenuItem);

        jMenuBar1.add(HelpMenu);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void ConnectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConnectButtonActionPerformed
        // connects to instrument server
        if (cmd != null) {
            if (cmd.isConnected()) {
                // disconnect from server
                if (cmd.Disconnect()) {
                    ConnectButton.setText("Connect");
                }
            } else {
                // make connection
                if (cmd.Connect()) {
                    ConnectButton.setText("Disconnect");

                    // configure visual components
                    setDeviceConfig(config.getList(CLIENT_DEVICES));
                    // resize window
                    setSize(1024, 768);
                }
            }
        }
}//GEN-LAST:event_ConnectButtonActionPerformed

    private void OptionsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OptionsMenuItemActionPerformed
        // create setup window
        options s = new options(this, true, config);        
}//GEN-LAST:event_OptionsMenuItemActionPerformed

    private void ExitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ExitMenuItemActionPerformed
        // File menu: Exit 
        exitApplication();
}//GEN-LAST:event_ExitMenuItemActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        exitApplication();
    }//GEN-LAST:event_exitForm

    private void GpibToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GpibToggleButtonActionPerformed
        // Set visibility of GPIB instruments
        JInternalFrame IBox = windows.get(UI_INSTRUMENTS);
        if (GpibToggleButton.isSelected()) {
            IBox.setVisible(true);
        } else {
            IBox.setVisible(false);
        }
            
}//GEN-LAST:event_GpibToggleButtonActionPerformed

    private void CanToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CanToggleButtonActionPerformed
        // Set visibility of CAN instruments
        JInternalFrame IBox = windows.get("canBox");
        if (CanToggleButton.isSelected()) {
            IBox.setVisible(true);
        } else {
            IBox.setVisible(false);
        }
}//GEN-LAST:event_CanToggleButtonActionPerformed

    private void OpenMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OpenMenuItemActionPerformed
        // open settings file
        AdvancedFileChooser jfc = new AdvancedFileChooser();

        if (currentDir != null) {
            jfc.setCurrentDirectory(currentDir);
        }

        int result = jfc.showOpenDialog(this);

        // check return value
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        // get user selected file        
        XmlReader r = new XmlReader(jfc.getSelectedFile());

        // get supported devices
        Enumeration e = deviceConfigs.keys();

        // loop all
        while (e.hasMoreElements()) {
            configObject c = deviceConfigs.get(e.nextElement());
            // find device from saved configuration xml
            if (r.contains(c.getName())) {
                // run device specific xml handler
                if (c.validInstance()) {
                    c.getInstance().loadConfig(r);
                }
            }
        }    
}//GEN-LAST:event_OpenMenuItemActionPerformed

    private void SaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuItemActionPerformed
        // save settings to existing file
        if (!settingsFile.isEmpty()) {
            // get document to write
            XmlWriter w = new XmlWriter();

            // get supported devices
            Enumeration e = deviceConfigs.keys();

            // loop all
            while (e.hasMoreElements()) {
                configObject c = deviceConfigs.get(e.nextElement());

                // run device specific xml handler
                if (c.validInstance()) {
                    w.addDeviceSection(c.getName(), c.getType(), c.getProperty("ch_count"));
                    // stode device configuration
                    c.getInstance().saveConfig(w);
                }
            }

            // store file
            w.XmlSave(settingsFile);
            return;
        }

        SaveAsMenuItemActionPerformed(evt);
        
}//GEN-LAST:event_SaveMenuItemActionPerformed

    private void SaveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveAsMenuItemActionPerformed
        // save setting to named file
        AdvancedFileChooser jfc = new AdvancedFileChooser();

        if (currentDir != null) {
            jfc.setCurrentDirectory(currentDir);
        }

        int result = jfc.showSaveDialog(this);

        // check return value
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        try {
            // get current path
            currentDir = jfc.getSelectedFile().getCanonicalFile();
        } catch (IOException ex) {
            logger.error("failed to parse current directory " + ex.getMessage());
        }

        // get user selected file
        settingsFile = jfc.getSelectedFile().toString();

        // get document to write
        XmlWriter w = new XmlWriter();

        // get supported devices
        Enumeration e = deviceConfigs.keys();

        // loop all
        while (e.hasMoreElements()) {
            configObject c = deviceConfigs.get(e.nextElement());

            // run device specific xml handler
            if (c.validInstance()) {
                w.addDeviceSection(c.getName(), c.getType(), c.getProperty("ch_count"));
                // stode device configuration
                c.getInstance().saveConfig(w);
            }
        }

        // store file
        w.XmlSave(settingsFile);
}//GEN-LAST:event_SaveAsMenuItemActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
    // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void AboutMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AboutMenuItemActionPerformed
        new AboutDialog(this, true);
}//GEN-LAST:event_AboutMenuItemActionPerformed

    private void PrintMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintMenuItemActionPerformed
    // TODO add your handling code here:
}//GEN-LAST:event_PrintMenuItemActionPerformed

    private void ToolsToggleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ToolsToggleButtonActionPerformed
        // Set visibility of tools
        JInternalFrame IBox = windows.get("toolsBox");
        if (ToolsToggleButton.isSelected()) {
            IBox.setVisible(true);
        } else {
            IBox.setVisible(false);
        }
    }//GEN-LAST:event_ToolsToggleButtonActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    // make a new   
    //scriptingEngine engine = new scriptingEngine();
    //desktop.add(engine);
    //engine.setVisible(true);
        
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final InputStream imageStream = instrument.class.getResourceAsStream("/resources/splash.png");

        Thread t = new Thread(InstrumentSplashScreen.getSplashScreen(imageStream, 0, 100));
        t.start();

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new instrument().setVisible(true);
            }
        });

        try {
            t.join();
        } catch (InterruptedException ex) {

        }

    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AboutMenuItem;
    private oh3ebf.lib.ui.buttons.CheckToggleButton CanToggleButton;
    private javax.swing.JButton ConnectButton;
    private javax.swing.JMenuItem ExitMenuItem;
    private javax.swing.JMenu FileMenu;
    private oh3ebf.lib.ui.buttons.CheckToggleButton GpibToggleButton;
    private javax.swing.JMenu HelpMenu;
    private javax.swing.JMenuItem OpenMenuItem;
    private javax.swing.JMenuItem OptionsMenuItem;
    private javax.swing.JMenuItem PrintMenuItem;
    private javax.swing.JMenuItem SaveAsMenuItem;
    private javax.swing.JMenuItem SaveMenuItem;
    private javax.swing.JMenu ToolsMenu;
    private oh3ebf.lib.ui.buttons.CheckToggleButton ToolsToggleButton;
    private javax.swing.JMenu WindowMenu;
    private javax.swing.JDesktopPane desktop;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    // End of variables declaration//GEN-END:variables
    }
