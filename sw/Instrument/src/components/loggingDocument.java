/***********************************************************
 * Software: instrument client
 * Module:   logging Document model class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 11.4.2013
 *
 ***********************************************************/
package components;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import org.apache.log4j.Logger;

public class loggingDocument extends PlainDocument {

    private boolean enabled = false;
    private String fileName = "";
    private BufferedWriter logFile;
    private static Logger logger;
    private String formatterStr = "dd.MMM.yyyy HH:mm:ss";

    public loggingDocument() {
        super();

        // get logger instance for this class
        logger = Logger.getLogger(loggingDocument.class);
    }

    /** Function enables log writing
     * 
     * @param state of logging
     */
    public void enableLogging(boolean state) {
        enabled = state;
    }

    /** Function set log file name
     * 
     * @param name of file
     */
    public void setLogFileName(String name) {
        fileName = name;

    }

    /** Function opens log file for writing
     * 
     * @return file opened ok
     */
    public boolean openLog() {
        // file name set
        if (!fileName.isEmpty()) {
            try {
                // open log file
                logFile = new BufferedWriter(new FileWriter(fileName));
                return (true);
            } catch (IOException ex) {
                logger.error("failed to open data log file :" + fileName);
            }
        }
        return (false);
    }

    /** Function closes log file
     * 
     * @return true if closed ok
     * 
     */
    public boolean closeLog() {
        try {
            // check if file is opened
            if (logFile != null) {
                // close log file
                logFile.close();
                return (true);
            }
        } catch (IOException ex) {
            logger.error("failed to close data log file :" + fileName);
        }
        return (false);
    }

    /** Function inserts log line to document model and log file if enabled
     * 
     * @param offs not supported
     * @param str line to insert
     * @param a not supported
     * @throws javax.swing.text.BadLocationException
     * 
     */
    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {

        // TODO tätä pitää kehittää, aikaleima voi olla millisekunteina tai halutussa muodossa joka annetaan optio näytössä
        SimpleDateFormat sdf = new SimpleDateFormat(formatterStr);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        // collect log line 
        String line = sdf.format(timestamp) + ": " + str + "\n";
        // insert to model
        super.insertString(offs, line, a);

        // check if logging to file is enabled
        if (enabled) {
            try {
                // write to log and flush to disk
                logFile.write(line);
                logFile.flush();
            } catch (IOException ex) {
                logger.error("failed to write data to log file :" + fileName);
            }
        }
    }
}
