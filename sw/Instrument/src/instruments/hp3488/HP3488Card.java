/***********************************************************
 * Software: instrument client
 * Module:   HP3488 option card class
 * Version:  0.1
 * Licence:  GPL2
 *
 * Owner: Kim Kristo
 * Date creation : 29.10.2012
 *
 ***********************************************************/
package instruments.hp3488;

import eu.hansolo.steelseries.tools.LedColor;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;
import org.apache.log4j.Logger;

public class HP3488Card extends javax.swing.JPanel {

    private static Logger logger;
    private static final String cardType[] = {
        "HP44470", "HP44471", "HP44472", "HP44473",
        "HP44474", "HP44475", "HP44476", "HP44477",
        "HP44478", "empty"
    };
    private JLabel[] labels;
    private eu.hansolo.steelseries.extras.Led[] leds;
    private boolean[] ledStates;
    private int slot;
    private int type;
    private HP3488CardInterface cardIf;
    private int cnt = 0,  blocks = 0;
    public final static int HP44470 = 0;
    public final static int HP44471 = 1;
    public final static int HP44472 = 2;
    public final static int HP44473 = 3;
    public final static int HP44474 = 4;
    public final static int HP44475 = 5;
    public final static int HP44476 = 6;
    public final static int HP44477 = 7;
    public final static int HP44478 = 8;
    public final static int HP_EMPTY_SLOT = 9;
    private final static int MAX_CNT = 16;
            
    /** Creates new form HP3488Card */
    public HP3488Card(int slot, int type, HP3488CardInterface iface) {
        this.slot = slot;
        this.type = type;
        this.cardIf = iface;

        labels = new JLabel[MAX_CNT];
        leds = new eu.hansolo.steelseries.extras.Led[MAX_CNT];
        ledStates = new boolean[MAX_CNT];

        // get logger instance for this class
        logger = Logger.getLogger(HP3488Card.class);

        initComponents();
        init(type);
        
        ((TitledBorder) this.getBorder()).setTitle("Slot " + (slot + 1) + ": " + cardType[type]);
        repaint();
    }

    /** Creates new form HP3488Card */
    public HP3488Card(int slot, String card, HP3488CardInterface iface) {
        int i = 0;
        this.slot = slot;
        this.cardIf = iface;

        labels = new JLabel[MAX_CNT];
        leds = new eu.hansolo.steelseries.extras.Led[MAX_CNT];
        ledStates = new boolean[MAX_CNT];

        // get logger instance for this class
        logger = Logger.getLogger(HP3488Card.class);

        initComponents();

        // scan card list and set card type
        for (i = 0; i < cardType.length; i++) {
            if ((card.toUpperCase()).equals(cardType[i])) {
                this.type = i;
                ((TitledBorder) this.getBorder()).setTitle("Slot " + (slot + 1) + ": " + cardType[i]);
                break;
            }
        }

        // initialize visual buttons
        init(type);
    }

    /** Function  initializes led components to screen
     * 
     * @param cardType to initialize user interface
     * 
     */
    private void init(int cardType) {
        int x = 10, y = 20;
        int xInc = 0;
        int blockIndex = 0, blockSpacing = 0;
        
        // TODO label pitää vaihtaa kortin mukaan
        String labelText = "RL";

        switch (cardType) {
            case HP44470:
            case HP44471:
                // 10 relay / switch lines
                cnt = 10;
                xInc = 30;
                break;
            case HP44472:
                // 2 x 4 relay / switch lines
                cnt = 8;
                xInc = 30;
                blocks = 2;
                blockIndex = 4;
                blockSpacing = 50;
                break;
            case HP44473:
            case HP44474:
                // 16 digital I/O lines
                cnt = 10;
                xInc = 30;
                break;
            case HP44475:
                // testi levy 8 in 8 out
                break;
            case HP44476:
                // 3 kpl 2 x vaihto
                break;
            case HP44477:
                // 7 relay / switch lines
                cnt = 7;
                xInc = 30;
                break;
            case HP44478:
                break;
        }

        for (int i = 0; i < cnt; i++) {
            if (blocks > 0) {
                // if items are grouped, add extra space between groups
                if (i == blockIndex) {
                    blockIndex += cnt / blocks;
                    x += blockSpacing;
                }
            }

            labels[i] = new JLabel();
            //labels[i].setFont(new java.awt.Font("Dialog", 0, 10));
            labels[i].setFont(new java.awt.Font("SansSerif", 0, 10));
            labels[i].setHorizontalAlignment(javax.swing.SwingConstants.CENTER);            
            labels[i].setText(labelText + (i + 1));
            add(labels[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y + 30, 30, 20));

            leds[i] = new eu.hansolo.steelseries.extras.Led();
            leds[i].setName(String.valueOf(i));
            leds[i].setLedColor(LedColor.GREEN_LED);
            leds[i].addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    ledMouseClicked(evt);
                }
            });

            // set layout
            org.jdesktop.layout.GroupLayout ledLayout = new org.jdesktop.layout.GroupLayout(leds[i]);
            leds[i].setLayout(ledLayout);
            ledLayout.setHorizontalGroup(
                    ledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 30, Short.MAX_VALUE));
            ledLayout.setVerticalGroup(
                    ledLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 30, Short.MAX_VALUE));

            // add to panel
            add(leds[i], new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, -1, 30));
            ledStates[i] = false;
            leds[i].setLedOn(false);

            x += xInc;
        }
    }

    /** Function implements mouse listener and updates led visual states
     * 
     * @evt mouse event
     * 
     */
    private void ledMouseClicked(java.awt.event.MouseEvent evt) {

        try {
            int n = 0;
            // get event source object
            eu.hansolo.steelseries.extras.Led s = (eu.hansolo.steelseries.extras.Led) evt.getSource();
            n = Integer.parseInt(s.getName());

            // change led state
            if (ledStates[n]) {
                ledStates[n] = false;
            } else {
                ledStates[n] = true;
            }

            setLedState(n);
        
        } catch (Exception ex) {
            logger.warn("failed to parse led number: " + ex.getMessage());
        }
    }

    /** Function sets led and I/O state on device
     * 
     * @param ledIndex to to set
     * @return
     */
    
    private boolean setLedState(int ledIndex) {
        int blockIndex = 0, index = 0, selection = 0;

        // check led index bounds
        if (ledIndex > ledStates.length) {
            logger.error("trying to access led array out of bounds.");
            return (false);
        }

        // set visual state
        leds[ledIndex].setLedOn(ledStates[ledIndex]);

        // send command to target
        switch (type) {
            case HP44470:
            case HP44471:
                cardIf.setLineState(ledStates[ledIndex], slot, ledIndex);
                break;
            case HP44472:
                // get block number
                blockIndex = ledIndex / (cnt / blocks);
                // handle leds in group
                for (int i = (blockIndex * (cnt / blocks));
                        i < (blockIndex * (cnt / blocks)) + (cnt / blocks); i++) {
                    // clear all other than selected
                    if (i != ledIndex) {
                        ledStates[i] = false;
                        leds[i].setLedOn(ledStates[i]);
                    } else {
                        // save selected led 
                        selection = index;
                    }
                    // calculate led position in group
                    index++;
                }

                // write to device, valid values are s10 s11 s12 s13 s20 s21 s22 s23 s24
                cardIf.setLineState(ledStates[ledIndex], slot, (blockIndex * 10) + selection);
                break;
            }

        return (true);
    }

    /** Function sets all leds to off state
     * 
     */
    public void resetState() {
        for (int i = 0; i < leds.length; i++) {
            // change led state             
            ledStates[i] = false;

            // set visual state
            leds[i].setLedOn(ledStates[i]);
        }
    }

    /** Function sets indexed led in given state
     * 
     * @param index to led
     * @param state to set
     * @return
     */
    
    public boolean setState(int index, boolean state) {
        ledStates[index] = state;
        
        return(setLedState(index));        
    }
    
    /** Function return card slot number
     * 
     * @return slot number
     */
    public int getSlot() {
        return(slot);
    }
    
    /** Function return card type
     * 
     * @return type number
     */
    public int getType() {
        return(type);
    }
    
    /** Function return current realy states
     * 
     * @return states in boolean array
     */
    
    public boolean[] getStates() {
        return(ledStates);
    }
    
    /** Function return number of actively used relays in boolean state storage
     * 
     * @return count
     */
    
    public int getRelayInUseCount() {
        return(cnt);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(194, 225, 255));
        setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "HP44470"));
        setMaximumSize(new java.awt.Dimension(330, 80));
        setMinimumSize(new java.awt.Dimension(330, 80));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(330, 80));
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
