/***********************************************************
 * Software: instrument client
 * Module:   Scripting engine class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 13.03.2013
 *
 ***********************************************************/
package scriptEngine;

import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.Highlighter;
import murlen.util.fscript.FSExtension;
import murlen.util.fscript.FSReflectionExtension;
import murlen.util.fscript.FScript;
import org.apache.log4j.Logger;

public class scriptingEngine extends javax.swing.JInternalFrame implements Runnable {

    private static Logger logger;
    private FScript fs;
    private String fileName;
    private String[] lines;
    private boolean exitThread;
    private Thread worker;
    private File currentDir;
    private volatile int state;
    private final static int IDLE = 0;
    private final static int RUN_SCRIPT = 1;
    private final static int STOP_SCRIPT = 2;

    /** Creates new form scriptingEngine */
    public scriptingEngine() {
        currentDir = null;

        // get logger instance for this class
        logger = Logger.getLogger(scriptingEngine.class);

        fs = new FScript();
        fs.registerExtension(new FSReflectionExtension());        
        fs.registerExtension(new SleepExtension());
        
        state = IDLE;
        exitThread = false;
        worker = new Thread(this);
        worker.start();

        initComponents();
    }

    /** Function adds new command handler
     * 
     * @param ex reference to handler
     * 
     */
    public void addExtension(FSExtension ex) {
        // chack for valid command interpreter
        if (fs != null) {
            // add new extension module
            fs.registerExtension(ex);
        }
    }

    public void run() {
        Highlighter.HighlightPainter painter;



        // tässä pitäisi lisätä rivit moottorille...

        // yksittäisen rivin ajaminen tapahtuu eventillä....
        /*
        try {
        int startIndex = jTextArea1.getLineStartOffset(0);
        int endIndex = jTextArea1.getLineEndOffset(0);
        //String colour = (String) cbox.getSelectedItem();
        painter = new DefaultHighlighter.DefaultHighlightPainter(Color.RED);
        jTextArea1.getHighlighter().addHighlight(startIndex, endIndex, painter);
        } catch (BadLocationException ex) {
        }
         */

        while (!exitThread) {
            try {
                // execution state machine
                switch (state) {
                    case IDLE:
                        // wait here
                        break;
                    case RUN_SCRIPT:
                        // execute script
                        try {
                            fs.run();
                        } catch (Exception ex) {
                            logger.fatal("script execution failed: " + fs.getError() + " " + ex.getMessage());
                        }
                        state = IDLE;
                        break;
                    case STOP_SCRIPT:
                        // stop execution
                        fs.reset();
                        state = IDLE;
                        break;
                }
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                logger.fatal("InterruptedException in scrip excecution thread");
            }
        }
        logger.info("script execution finished");
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jToolBar1 = new javax.swing.JToolBar();
        RunButton = new javax.swing.JButton();
        StopButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        openFileMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();

        setBackground(new java.awt.Color(196, 204, 223));
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(640, 480));

        jToolBar1.setBackground(new java.awt.Color(196, 204, 223));
        jToolBar1.setRollover(true);

        RunButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/control_play.png"))); // NOI18N
        RunButton.setToolTipText("Execute script");
        RunButton.setEnabled(false);
        RunButton.setFocusable(false);
        RunButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        RunButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        RunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RunButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(RunButton);

        StopButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/control_stop.png"))); // NOI18N
        StopButton.setToolTipText("Stop script");
        StopButton.setFocusable(false);
        StopButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        StopButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        StopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StopButtonActionPerformed(evt);
            }
        });
        jToolBar1.add(StopButton);

        getContentPane().add(jToolBar1, java.awt.BorderLayout.SOUTH);

        jPanel1.setBackground(new java.awt.Color(196, 204, 223));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jTextArea1.setColumns(20);
        jTextArea1.setEditable(false);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 0.8;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jScrollPane1, gridBagConstraints);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        jMenuBar1.setBackground(new java.awt.Color(196, 204, 223));

        jMenu1.setText("File");

        openFileMenuItem.setBackground(new java.awt.Color(196, 204, 223));
        openFileMenuItem.setText("Open");
        openFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openFileMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(openFileMenuItem);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void openFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openFileMenuItemActionPerformed
        // create and show file chooser
        JFileChooser jfc = new JFileChooser();
        if (currentDir != null) {
            jfc.setCurrentDirectory(currentDir);
        }

        // set file filter for script files
        jfc.addChoosableFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                // show directories
                if (f.isDirectory()) {
                    return (true);
                }

                // show script files               
                if (f.getName().endsWith(".fs")) {
                    return (true);
                }

                return (false);
            }

            @Override
            public String getDescription() {
                return "FSCRIPT files";
            }
        });

        int result = jfc.showOpenDialog(this);
                       
        // check return value
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }

        // get user selected file
        fileName = jfc.getSelectedFile().toString();

        try {
            // get current path
            currentDir = jfc.getSelectedFile().getCanonicalFile();

            // read file to text area
            FileReader reader = new FileReader(fileName);
            jTextArea1.read(reader, fileName);

            // get lines from text area
            lines = jTextArea1.getText().split("\\n");
        } catch (Exception ex) {
            logger.error("failed to read script file" + fileName);
        }

        // enable script execution
        RunButton.setEnabled(true);
}//GEN-LAST:event_openFileMenuItemActionPerformed

    private void RunButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RunButtonActionPerformed
        // reset the Interpreter (only done when it was started before)
        fs.reset();

        // load hole script for execution
        for (int i = 0; i < lines.length; i++) {
            if (!lines[i].equals("")) {
                fs.loadLine(lines[i]);
            }
        }

        // start scripting
        state = RUN_SCRIPT;
}//GEN-LAST:event_RunButtonActionPerformed

    private void StopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StopButtonActionPerformed
        // stop scripting
        state = STOP_SCRIPT;
}//GEN-LAST:event_StopButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton RunButton;
    private javax.swing.JButton StopButton;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem openFileMenuItem;
    // End of variables declaration//GEN-END:variables
}
