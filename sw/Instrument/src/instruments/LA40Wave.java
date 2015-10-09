package instruments;
/*
 * LA40Wave.java
 *
 * Created on January 16, 2004, 3:07 PM
 */

/**
 *
 * @author  operator
 */

import java.awt.*;
import java.util.*;

public class LA40Wave extends javax.swing.JPanel {
    private Color bg = new java.awt.Color(0, 102, 102); // background color   
    private Color labelC = Color.cyan; // labebel writing color
    private Color waveC = new java.awt.Color(95, 232, 248); // waveform color
    private Color busC = new java.awt.Color(95, 232, 248);  // bus color
    private Color scaleC = new java.awt.Color(51, 204, 0); // scales color
    private Color valueC = new java.awt.Color(51, 204, 0); // bus value color
    
    private int labelX = 5; // label drawing x - offset
    private int labelY = 15; // label drawing y - offset
    private int waveX = 0; // 
    private int waveY = 0;
    private int scaleLen = 50; // length of scale points
    private int sampleLen = 50; //  defautl length of waveform samples
    private int busH = 15; // heigth of bus wave form
    private int bitH = 15; // heigth of single wave form
    
    private int labelOffset = 0;
    private int timeOffset = 32768; // current sample offset
    
    private double sampleScale = 1.0;
    private double timeScale = 1.0;
    
    private Image hiddenImg;
    private Graphics hiddenG;

    //private boolean[] t = {false,false,true,false,true,false,true,true,false,false,true,true,false,true};
    //private long[] s = {0x55,0x55,0x55,0xaa,0xaa,0x01,0x02,0x03,0x04,0x02,0x05,0xf0,0xe0,0x10};
    private long[] s;
    private boolean[] t;
    
    //private int xOffset = 100;
    private Vector dataV;
    
    /** Creates new form LA40Wave */
    public LA40Wave() {
        // create internal conponents
        initComponents();
        
        // initialize offset variables
        waveX += labelX + 50; // position where waveform horizontal drawing area begings
        waveY += labelY; // position where waveform vertical drawing area begings
        
        dataGenerator();
    }
    
    
    private void dataGenerator() {
        Random rand = new Random();
        int i = 0;
        t = new boolean[65536];
        s = new long[65536];
        
        for(i = 0; i < 65536;i++) {
        t[i] = rand.nextBoolean();
        s[i] = rand.nextLong();
        }
    }
    
    /* function implents horizontal scrolling
     *
     * Parameters:
     * t: new value to time offset
     *
     * Returns:
     *
     */
    
    public void setTimeOffset(int t) {
        // set new offset value
        timeOffset = t;
        // update component
        repaint();
    }
    
    public void setLabelOffset(int l) {
        // function implements vertical scrolling
        repaint();
    }
    
    public void setResize() {
        hiddenImg = null;
        hiddenG = null;
    }
    
    /* Function sets sample rate value
     *
     * Parameters:
     * s: new sampling scale value
     *
     * Returns:
     *
     */
    
    public void setSampleScale(double s) {
        // set new sample scale
        sampleScale = s;
        repaint();
    }
    
    /* Function sets visual timescale value
     *
     * Parameters:
     * t: new  timescale value
     *
     * Returns:
     *
     */
    
    public void setTimeScale(double t) {
        // set new time scale
        timeScale = t;
        repaint();
    }
    
    /*
     *
     *
     *
     */
    
    public void updateData(Vector d) {
        if(d != null) {
            // draw new waveforms
            dataV = d;
            //clearWave();
            repaint();
        }
    }
    
    public void paint(Graphics g) {
        if(hiddenImg == null) {
            // create double buffered image
            hiddenImg = createImage(getWidth(), getHeight());
            // get its graphics
            hiddenG = hiddenImg.getGraphics();
            // set new background color
            hiddenG.setColor(bg);     
        } else {
        }
        update(g);
    }
    
    /* Function is called every time when this component is updated
     *
     * Parametrs:
     * g: graphic object to draw
     *
     * Returns:
     *
     */
    
    public void update(Graphics g) {
        // draw wave form if data and hidden image are present
        //        if(dataV != null && hiddenImg != null)
        clearWave(); 
        // draw image to the panel
        g.drawImage(hiddenImg,0,0,this);
    }
    
    /* Function handles waveform drawign to panel
     *
     * Parameters:
     *
     * Returns:
     *
     */
    
    private void clearWave() {
        int i = 0;
        int yOffset = 15;
        // clear screen
        hiddenG.setColor(bg);
        hiddenG.fillRect(0, 0, getWidth(), getHeight());
        drawScales(hiddenG);
        
        sampleLen = (int)((sampleScale / timeScale) * (double)scaleLen);
        if(sampleLen < 1)
            sampleLen = 1;
        
        //System.out.println("sampleScale:"+ sampleScale + " timeScale:"+timeScale+" multiplier: " + sampleScale / timeScale);
        //System.out.println(sampleLen);
        // testing
        drawWave(hiddenG, 100, t, "Label");
        drawBus(hiddenG, 120, s, "Label");
        
        
        // t�h�n datavektorin k�sittely ja piirto
        if(dataV != null)
        //for(i = 0;i < dataV.size(); i++) {
            //if(dataV. elementAt(i))
            if((Integer)dataV.elementAt(i) == 0) {
                // draw one bus label
                drawBus(hiddenG, yOffset, (long[])dataV.elementAt(i + 2), (String)dataV.elementAt(i + 1));
            } else {
            
            }
            // increment index ond drawing offset
            //i += 2;
            //yOffset += 20;
        //}
    }
    
    /* Function draws waveform area borders and timing scales
     *
     * Parameters:
     * g: virtual image buffer to draw
     *
     * Returns:
     *
     */
    
    private void drawScales(Graphics g) {
        int i = 0;
        int scaleStartY = 10;
        int scaleXinc = scaleLen;
        int w = getWidth() - 5;
        int h = getHeight() - 5;
        int scaleStartX = ((w - waveX - 5) / 2) + waveX;
        
        // color selection
        g.setColor(scaleC);
        // draw vertival separator line between labels and waveforms
        g.drawLine(waveX, scaleStartY, waveX, h);
        // draw top line
        g.drawLine(waveX, scaleStartY, w, scaleStartY);
        // draw rigth line
        g.drawLine(w, scaleStartY, w, h);
        // draw bottom line
        g.drawLine(waveX, h, w, h);
        
        // draw scale points beginning from middle
        for(i = 0;i <= (scaleStartX / scaleLen);i++) {
            // to the left on top
            g.drawLine(scaleStartX - scaleXinc, scaleStartY,scaleStartX - scaleXinc, scaleStartY + 3);
            // to the rigth on top
            g.drawLine(scaleStartX + scaleXinc, scaleStartY,scaleStartX + scaleXinc, scaleStartY + 3);
            // to the left on bottom
            g.drawLine(scaleStartX - scaleXinc, h,scaleStartX - scaleXinc, h - 3);
            // to the rigth on bottom
            g.drawLine(scaleStartX + scaleXinc, h,scaleStartX + scaleXinc, h - 3);
            scaleXinc -= scaleLen;
        }
    }
    
    /* Function draws single waform on virtual image buffer
     *
     * Parameters:
     * g: virtual image buffer to draw
     * yStart: vertical offset
     * data[]: boolean array containing data to draw
     * s: waveform label
     *
     * Returns:
     *
     */
    
    private void drawWave(Graphics g, int yStart, boolean[] data, String s) {
        int i = 0;
        int w = getWidth() - 5;
        int y = waveY + yStart;
        int x1 = 0, x2 = 0;
        int waveStartX = ((w - waveX - 5) / 2) + waveX; // calculate middle point of drawing area
        int waveXinc = sampleLen; // data sample drawing increment
        
        x1 = x2 = waveStartX; // initialize starting point
        
        // draw label on left
        g.setColor(labelC);
        g.drawString(s, labelX, labelY + yStart);
        
        // set waveform drawing color
        g.setColor(waveC);
        
        // check array limits
        if((timeOffset - 1) >= 0 && timeOffset <= 65535) {
            // transition detection for starting point
            if((data[timeOffset] == false && data[timeOffset - 1] == true) ||
                    (data[timeOffset] == true && data[timeOffset - 1] == false))
                drawTransition(g, y, x1, bitH);
        }
        
        // loop datasample and draw from middle to drawing area rigth edge
        for(i = 0; i < ((w - waveStartX) / waveXinc);i++) {
            // check array limits
            if((timeOffset + i + 1) <= 65535) {
                // draw samples to rigth
                if(data[timeOffset + i] == false) {
                    // draw zero bit
                    drawState(g, y, x1, waveXinc);
                    // next sample drawing location to rigth
                    x1 = x1 + waveXinc;
                    // check state transition
                    if(data[i + 1 + timeOffset] == true) {
                        drawTransition(g, y, x1, bitH);
                    }
                } else {
                    // draw one bit
                    drawState(g, y - bitH, x1, waveXinc);
                    x1 = x1 + waveXinc;
                    // check state transition
                    if(data[i + 1 + timeOffset] == false) {
                        drawTransition(g, y, x1, bitH);
                    }
                }
            }
        }
        
        // loop datasample and draw from middle to drawing area left edge
        for(i = 1; i < ((waveStartX - waveX) / waveXinc);i++) {
            // check array limits
            if((timeOffset - i - 1) >= 0) {
                // draw sample to left
                if(data[timeOffset - i] == false) {
                    // draw zero bit
                    drawState(g, y, x2, -waveXinc);
                    // next sample drawing location to left
                    x2 = x2 - waveXinc;
                    // check state transition
                    if(data[timeOffset - i - 1] == true) {
                        drawTransition(g, y, x2, bitH);
                    }
                } else {
                    // draw one bit
                    drawState(g, y - bitH, x2, -waveXinc);
                    // next sample drawing location to left
                    x2 = x2 - waveXinc;
                    // check state transition
                    if(data[timeOffset - i - 1] == false) {
                        drawTransition(g, y, x2, bitH);
                    }
                }
            }
        }
    }
    
    /* Function draws state in single waveform drawing
     *
     * Parameters:
     * g: virtual image buffer to draw
     * yPosition: vertical location of waveform
     * xPosition: horizontal starting point of drawing
     * xLen: drawing line lengt
     */
    
    private void drawState(Graphics g, int yPosition, int xPosition, int xLen) {
        g.drawLine(xPosition, yPosition, xPosition + xLen, yPosition);
    }
    
    
    /* Function draws state transition in single waveform drawing
     *
     * Parameters:
     * g: virtual image buffer to draw
     * yPosition: vertical location of transition
     * xPosition: horizontal starting point of drawing
     * yBitH: '1' state drawing offset from zore level
     *
     */
    
    private void drawTransition(Graphics g, int yPosition, int xPosition, int yBitH) {
        g.drawLine(xPosition, yPosition, xPosition, yPosition - yBitH);
    }
    
    /* Function draws bus waveform to virtual image buffer
     *
     * Parameters:
     * g: virtual image buffer to draw
     * yStart: vertical offset
     * data[]: boolean array containing data to draw
     * s: waveform label
     *
     * Returns:
     *
     */
    
    private void drawBus(Graphics g, int yStart, long[] data, String s) {
        int i = 0;
        int w = getWidth() - 5;
        int x1 = 0,x2 = 0;
        int y = waveY + yStart;
        int busStartX = ((w - waveX - 5) / 2) + waveX; // calculate middle point of drawing area
        int busXinc = sampleLen; // data sample drawing increment
        int previousCrossingP = 0, previousCrossingN = 0;
        boolean stateChange = false; // indicates bus value change
        int DataValueWidth = 0; // used to save physical string length of bus value
        
        // find default font parameters
        FontMetrics fm = this.getFontMetrics(this.getFont());
        
        
        // waform drawing start loaction
        x1 = x2 = busStartX;
        // state change crossing previous location
        previousCrossingP = previousCrossingN = busStartX;
        
        // draw label
        g.setColor(labelC);
        g.drawString(s, labelX, labelY + yStart);
        
        // set bus drawing color
        g.setColor(busC);
        
        // check array limits
        if((timeOffset - 1) >= 0 && timeOffset <= 65535) {
            // check starting state change condition
            if(data[timeOffset] != data[timeOffset - 1]) {
                // draw state change
                drawBusCrossing(g, y, x1, busH, busXinc);
                // save position
                previousCrossingP = x1;
                previousCrossingN = x1;
                // value changed
                stateChange = true;
            }
        }
        
        // draw waveform to rigth
        for(i = 0; i < (((w - busStartX) / busXinc));i++) {
            // check array limits
            if((timeOffset + i - 1) <= 65535 && (timeOffset + i - 1) >= 0) {
                // check starting state change condition
                if(data[timeOffset + 1] != data[timeOffset + i - 1]) {
                    // draw state change
                    drawBusCrossing(g, y, x1, busH, busXinc);
                    // save position
                    previousCrossingP = x1;
                    // set change flag
                    stateChange = true;
                }
                // draw bus line
                drawBusLines(g, y, x1, busH, busXinc);
                // next drawing location
                x1 = x1 + busXinc;
                
                // bus value is only drawn if there is two changes
                if(stateChange == true) {
                    // draw bus value
                    g.setColor(valueC);
                    // get physical length of string to print
                    DataValueWidth = fm.stringWidth(Long.toHexString(data[i]));
                    // check if available space if enough
                    if(DataValueWidth <= (x1 - previousCrossingP))
                        // print bus value
                        g.drawString(Long.toHexString(data[timeOffset + i]),
                                x1 - ((x1 - previousCrossingP) / 2) - (DataValueWidth / 2) , y - 3);
                    g.setColor(busC);
                    // clean change indicator
                    stateChange = false;
                }
            }
        }
        
        // draw waveform to left
        for(i = 0; i < ((busStartX - waveX) / busXinc);i++) {
            // check array limits
            if((timeOffset - i - 1) >= 0) {
                System.out.println(timeOffset);
                // check starting state change condition
                if(data[timeOffset - i] != data[timeOffset - i - 1]) {
                    // draw state change
                    drawBusCrossing(g, y, x2, busH, busXinc);
                    // save position
                    previousCrossingN = x2;
                    // set change flag
                    stateChange = true;
                }
                // draw bus line
                drawBusLines(g, y, x2, busH, -busXinc);
                // next drawing location
                x2 = x2 - busXinc;
                
                // bus value is only drawn if there is two changes
                if(stateChange == true) {
                    // draw bus value
                    g.setColor(valueC);
                    // get physical length of string to print
                    DataValueWidth = fm.stringWidth(Long.toHexString(data[i]));
                    // check if available space if enough
                    if(DataValueWidth <= (x1 - previousCrossingP))
                        // print bus value
                        g.drawString(Long.toHexString(data[timeOffset - i]),
                                x2 + ((previousCrossingN - x2) / 2) - (DataValueWidth / 2), y - 3);
                    g.setColor(busC);
                    // clear change flag
                    stateChange = false;
                }
            }
        }
    }
    
    /* Function draws bus waveform  lines
     *
     * Parameters:
     * g: imagebuffer to draw
     * yPosition: vertical stating point
     * xPosition: horizontal starting position
     * yBusH: y - offset of bus top line
     * xLen: drawing length of sample
     *
     * Returns:
     *
     */
    
    private void drawBusLines(Graphics g, int yPosition, int xPosition, int yBusH, int xLen) {
        // draw bus top line
        g.drawLine(xPosition, yPosition - yBusH, xPosition + xLen, yPosition - yBusH);
        // draw bus bottom line
        g.drawLine(xPosition, yPosition, xPosition + xLen, yPosition);
    }
    
    /* Function draws bus waveform state change lines
     *
     * Parameters:
     * g: imagebuffer to draw
     * yPosition: vertical stating point
     * xPosition: horizontal starting position
     * yBusH: y - offset of bus top line
     * xLen: drawing length of sample
     *
     * Returns:
     *
     */
    
    private void drawBusCrossing(Graphics g, int yPosition, int xPosition, int yBusH, int xLen) {
        g.drawLine(xPosition -5 , yPosition , xPosition + 5, yPosition - yBusH);
        g.drawLine(xPosition -5 , yPosition - yBusH, xPosition + 5, yPosition);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        setLayout(null);

        setBackground(new java.awt.Color(255, 255, 255));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                formMouseMoved(evt);
            }
        });

    }//GEN-END:initComponents

    private void formMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseMoved
        // Add your handling code here:
    }//GEN-LAST:event_formMouseMoved

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        // Add your handling code here:
    }//GEN-LAST:event_formMouseReleased

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // Add your handling code here:
    }//GEN-LAST:event_formMousePressed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
