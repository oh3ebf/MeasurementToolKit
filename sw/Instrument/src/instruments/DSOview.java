package instruments;
/*
 * DSOview.java
 *
 * Created on November 18, 2003, 5:18 PM
 */

/**
 *
 * @author  operator
 */

import java.awt.*;
import java.lang.Math.*;

public class DSOview extends javax.swing.JPanel {
    private Color bg = new java.awt.Color(0, 102, 102);
    private Color cr = new java.awt.Color(51, 204, 0);
    private Color wave1 = Color.red;
    private Color wave2 = Color.blue;
    private Color wave3 = Color.cyan;
    private Color wave4 = Color.magenta;
    private Color gnd1 = Color.blue;
    private Color cursor = Color.orange;
    
    private Image hiddenImg;
    private Graphics hiddenG;
    
    private byte[] dataCh0; 
    private byte[] dataCh1; 
    private byte[] dataCh2; 
    private byte[] dataCh3; 
    
    private byte[] memCh1; 
    private byte[] memCh2; 
    
    private int offset = 0;
    private double timeScale = 0.0;
    //private int magnitude = 1;
    
    private int xStart = 10;
    private int yStart = 10;
    private int topMax = 0;
    private int bottomMax = 0;
    private int leftMax = 0;
    private int rigthMax = 0;
    int divX = 51;
    int divY = 31;
    int adcHalfValue = 127;  
    
    private double[] voltageSampleScale;
    private double[] voltageScale;
    private double[] gndRef;
    private double[] samplingTime;
    private boolean[] chActive;
    
    private int cursorMode = 0;
    private int cursorX1 = 100;
    private boolean cursorX1Select = false;
    private int cursorX2 = 120; 
    private boolean cursorX2Select = false;
    private int cursorY1 = 100;
    private boolean cursorY1Select = false;
    private int cursorY2 = 120;
    private boolean cursorY2Select = false;
    
    /** Creates new form DSOview */
    public DSOview() {
        int i = 0;
        initComponents();        
        
        dataCh0 = new byte[65536];
        dataCh1 = new byte[65536];
        dataCh2 = new byte[65536];
        dataCh3 = new byte[65536];
                
        memCh1 = new byte[65536];
        memCh2 = new byte[65536];
        
        // init waveforms
        for(i = 0;i < 65536;i++) {
            dataCh0[i] = (byte)adcHalfValue;
            dataCh1[i] = (byte)adcHalfValue;
            dataCh2[i] = (byte)adcHalfValue;
            dataCh3[i] = (byte)adcHalfValue;
            
            memCh1[i] = (byte)adcHalfValue;
            memCh2[i] = (byte)adcHalfValue;
        }
        
        voltageSampleScale = new double[4];
        voltageScale = new double[4];
        gndRef = new double[4];
        samplingTime = new double[4];
        chActive = new boolean[4];
        
    }   
    
    public void setData(byte[] d) {
        // update data set
        dataCh0 = d;
        clearView();
        repaint();
    }
    
    public void setOffset(int f) {
        // update memory offset
        offset = f;
        clearView();
        repaint();
    }
    
    public void setTimeScale(double t) {
        // update time scale
        timeScale = t;
        clearView();
        repaint();
    }    
    
    public void setSamplingTimeScale(double s, int ch) {
        // set sampling time for selected channel
        samplingTime[ch] = s;
        clearView();
        repaint();
    }
    
    public void setVoltageSampleScale(double v, int i) {
        // update sampling voltage scale
        voltageSampleScale[i] = v;
        clearView();
        repaint();
    }
    
    public void setVoltageScale(double v, int i) {
        // update display voltage scale
        voltageScale[i] = v;
        clearView();
        repaint();
    }
    
    public void setGndRef(double v, int i) {
        // update gdn reference point of waveform
        gndRef[i] = v;
        clearView();
        repaint();
    }
    
    public void setChActive(boolean state, int ch) {
        // set new state
        chActive[ch] = state;
        clearView();
        repaint();
    }
    
    public void setCursorMode(int mode) {
        // update cusor mode
        cursorMode = mode;
        clearView();
        drawCursor(hiddenG);
        repaint();
    }
    
    public void paint(Graphics g) {
        // drawing area border values
        topMax = 10;
        bottomMax = getHeight() - 10;
        leftMax = 20;
        rigthMax = getWidth() - 20;
        
        if(hiddenImg == null) {
            // create double buffered image
            hiddenImg = createImage(getWidth(), getHeight());
            // get its graphics
            hiddenG = hiddenImg.getGraphics();
            hiddenG.setColor(bg);
            hiddenG.fillRect(0, 0, getWidth(), getHeight());
            drawCross(hiddenG);
        } else {
            update(g);
        }
    }
    
    public void update(Graphics g) {        
        // draw new image
        g.drawImage(hiddenImg,0,0,this);
    }
    
    private void clearView() {
        if(hiddenG != null) {
            // set background color
            hiddenG.setColor(bg);
            // draw solid box
            hiddenG.fillRect(0, 0, getWidth(), getHeight());
            // draw haircross
            drawCross(hiddenG);
            // draw ch0 waveform
            if(dataCh0 != null && chActive[0] == true)
                drawWave(hiddenG, dataCh0, 0, wave1);
            // draw ch1 waveform
            if(dataCh1 != null && chActive[1] == true)
                drawWave(hiddenG, dataCh1, 1, wave2);
            // draw ch2 waveform
            if(dataCh2 != null && chActive[2] == true)
                drawWave(hiddenG, dataCh2, 2, wave3);
            // draw ch3 waveform
            if(dataCh3 != null && chActive[3] == true)
                drawWave(hiddenG, dataCh3, 3, wave4);
            if(cursorMode != 0)
                drawCursor(hiddenG);
        }
    }
    
    private void drawCross(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        int x = leftMax, y = topMax;
        int i = 0, n = 0, q = 0, p = 1;
        
        g.setColor(cr);
        
        // draw horizontal lines
        for(i = 0; i < divY;i++) {
            if(n == 0) {
                // draw solid line
                g.drawLine(leftMax, y, rigthMax,y);
            } else {
                // draw grid line
                g.drawLine(leftMax, y, leftMax + 3,y);
                g.drawLine(rigthMax - 3, y, rigthMax,y);
            }
            // netx value
            y += java.lang.Math.round((float)(bottomMax - topMax) / (float)(divY - 1));
            
            // offset counter
            if(n == 4)
                n = 0;
            else
                n++;
        }
        
        // clear offset counter
        n = 0;
        
        //draw vertical lines
        for(i = 0; i < divX;i++) {
            if(n == 0) {
                // draw solid line
                g.drawLine(x, topMax, x, bottomMax);
            } else {
                // draw grid line
                g.drawLine(x, topMax, x, topMax + 3);
                g.drawLine(x, bottomMax, x, bottomMax - 3);
            }
            // next value
            x += java.lang.Math.round((float)(rigthMax - leftMax) / (float)(divX - 1));           
            // offset counter
            if(n == 4)
                n = 0;
            else
                n++;
        }
        
        repaint();
        //x += (w - 20) / divX;
        //y += (h - 20) / divY;
        // top and down borders
        //g.drawLine((w - 20) / divX + 10, 10, ((w - 20) / divX) * divX + 10, 10);
        //g.drawLine((w - 20) / divX + 10, h - 10,((w - 20) / divX) * divX + 10, h - 10);
        
        // vertical
        //for(i = 0;i < divX;i++) {
        //    if(n == 1) {
        // draw vertical line
        //        g.drawLine(x, 10, x, h - 10);
        //    } else {
        // draw horizontal grid
        //        g.drawLine(x, 10, x, 13);
        //        g.drawLine(x, h - 10, x, h - 13);
        //        g.drawLine(x, (h - 20) / 31 * 16 - 2 + yStart, x, (h - 20) / 31 * 16 + 2 + yStart);
        //    }
        // draw middle line and horizontal bars
        //    if(i == 25) {
        //        for(q = 0; q < divY; q++) {
        // every 4 is solid line
        //            if(p == 4) {
        //                p = 1;
        // draw solid line
        //                g.drawLine((w - 20) / divX + 10, y, ((w - 20) / divX) * divX + 10, y);
        //            }
        //            else {
        // draw grid lines middle bar
        //                g.drawLine(x - 2, y, x + 2, y);
        // left border
        //                g.drawLine((w - 20) / divX + 10, y, (w - 20) / divX + 13, y);
        // rigth border
        //                g.drawLine(((w - 20) / divX) * divX + 7, y, ((w - 20) / divX) * divX + 10, y);
        //                p++;
        //            }
        
        //            y += (h - 20) / divY;
        //        }
        //    }
        //    if(n == 5)
        //        n = 1;
        //    else
        //        n++;
        // next x value
        //    x += (w - 20) / divX;
        //}
    }
    
    private void drawWave(Graphics g, byte[] data, int ch, Color c) {
        int w = getWidth();
        int h = getHeight();
        int i = 0;
        int y1 = 0, y2 = 0;
        int x1 = 0, x2 = 0;
        int x = xStart;
        int y = yStart;
        int loopEnd = ((w - 20) / divX) * divX + 10;
        //int xOffset = 1;
        
        g.setColor(c);
        x += (w - 20) / divX;
        // calculate middle of screen
        y += (h - 20) / 31 * 16;
        
        // draw sample data
        //for(i = 0; i < loopEnd; i++) {
        while((x2 < loopEnd) && ((i + offset) < 65535)) {
            // calculate starting point
            x1 = (int)((samplingTime[ch] * i) / (10 * timeScale / w));
            // this point value
            y1 = 0x000000ff & data[i + offset];
            // data have zero point between 0x7f and 0x80
            if(y1 > adcHalfValue)
                // for negative values we go down
                y1 = adcHalfValue - y1;
            else
                // for positive values we go up
                y1 = y1 - adcHalfValue;
            // convert to voltage and divide by voltage per pixel value
            y1 = (int)((((voltageSampleScale[ch] / 256) * y1) - gndRef[ch]) / (voltageScale[ch] / (h - 20)));
            
            // calculate ending point
            x2 = (int)((samplingTime[ch] * (i + 1)) / (10 * timeScale / w));
            // next point value
            y2 = 0x000000ff & data[i + offset + 1];
            // data have zero point between values 0x7f and 0x80
            if(y2 > adcHalfValue)
                // for negative values we go down
                y2 = adcHalfValue - y2;
            else
                // for positive values we go up
                y2 = y2 - adcHalfValue;
            // convert to voltage and divide by voltage per pixel value
            y2 = (int)((((voltageSampleScale[ch] / 256) * y2) - gndRef[ch]) / (voltageScale[ch] / (h - 20)));
            if(i > 0)
                // draw line connected measurement values
                g.drawLine( x1 + x, y + y1, x2 + x, y + y2);
            else
                // first line does not have point to draw line
                g.drawLine( x1 + x, y + y1, x2 + x, y + y1);
            //x += xOffset;
            i++;
        }
    }
    
    private void drawGdn(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        int x = 10, y = 10;
        g.setColor(gnd1);
        x += (w - 20) / divX;
        y += (h - 20) / divY;
    }
    
    private void drawCursor(Graphics g) {
        g.setColor(cursor);
        
        switch(cursorMode) {
            case 1:
                // X cursors
                g.drawLine(cursorX1, topMax,cursorX1, bottomMax);
                g.drawLine(cursorX2, topMax,cursorX2, bottomMax);
                break;
            case 2:
                // Y cursors
                g.drawLine(leftMax, cursorY1, rigthMax,cursorY1);
                g.drawLine(leftMax, cursorY2, rigthMax,cursorY2);
                break;
            case 3:
                // both X and Y cursors
                g.drawLine(cursorX1, topMax,cursorX1, bottomMax);
                g.drawLine(cursorX2, topMax,cursorX2, bottomMax);
                g.drawLine(leftMax, cursorY1, rigthMax,cursorY1);
                g.drawLine(leftMax, cursorY2, rigthMax,cursorY2);
                break;
        }
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents

        setLayout(null);

        setMaximumSize(new java.awt.Dimension(400, 400));
        setMinimumSize(new java.awt.Dimension(400, 400));
        setPreferredSize(new java.awt.Dimension(400, 400));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                formMouseReleased(evt);
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                formMouseDragged(evt);
            }
        });

    }//GEN-END:initComponents

    private void formMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseReleased
        // Add your handling code here:
        // unselct all cursors
        cursorX1Select = false;
        cursorX2Select = false;
        cursorY1Select = false;
        cursorY2Select = false;
    }//GEN-LAST:event_formMouseReleased

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
        // Add your handling code here:
        // get current mouse point
        Point p = evt.getPoint();
        // is it on X1
        if(p.x == cursorX1 && p.x >= leftMax && p.x <= rigthMax)
            cursorX1Select = true;
        // is it on X2
        if(p.x == cursorX2 && p.x >= leftMax && p.x <= rigthMax)
            cursorX2Select = true;
        // is it on Y1
        if(p.y == cursorY1 && p.y >= topMax && p.y <= bottomMax)
            cursorY1Select = true;
        // is it on Y2
        if(p.y == cursorY2 && p.y >= topMax && p.y <= bottomMax)
            cursorY2Select = true;
    }//GEN-LAST:event_formMousePressed

    private void formMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseDragged
        // Add your handling code here:
        // get mouse position
        Point p = evt.getPoint();
        
        // check position validity and make move
        if(cursorX1Select == true && p.x >= leftMax && p.x <= rigthMax)
            cursorX1 = evt.getPoint().x;
        if(cursorX2Select == true && p.x >= leftMax && p.x <= rigthMax)
            cursorX2 = evt.getPoint().x;
        if(cursorY1Select == true && p.y >= topMax && p.y <= bottomMax)
            cursorY1 = evt.getPoint().y;
        if(cursorY2Select == true && p.y >= topMax && p.y <= bottomMax)
            cursorY2 = evt.getPoint().y;
        
        clearView();
        drawCursor(hiddenG);
        repaint();
    }//GEN-LAST:event_formMouseDragged
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    
}
