package instruments;
/*
 * LA40TriggerSelector.java
 *
 * Created on January 31, 2004, 4:59 PM
 */

/**
 *
 * @author  operator
 */
public class LA40TriggerSelector extends javax.swing.JPanel {
    private byte[] pod = {'x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','x','\0'};
    
    /** Creates new form LA40TriggerSelector */
    public LA40TriggerSelector() {
        initComponents();        
    }
    
    public byte[] getTrigPattern() {
        // return current pattern 
        return(pod);
    }
    
    public void setTrigPattern(byte[] p) {
        // set new pattern
        pod = p;
        
        // bit 15
        switch(pod[0]){
            case 'x':
                jToggleButton1.setSelected(true);
                jToggleButton17.setSelected(false);
                jToggleButton33.setSelected(false);
                break;
            case '1':
                jToggleButton1.setSelected(false);
                jToggleButton17.setSelected(true);
                jToggleButton33.setSelected(false);
                break;
            case '0':
                jToggleButton1.setSelected(false);
                jToggleButton17.setSelected(false);
                jToggleButton33.setSelected(true);
                break;                
        }
        
        // bit 14
        switch(pod[1]){
            case 'x':
                jToggleButton2.setSelected(true);
                jToggleButton18.setSelected(false);
                jToggleButton34.setSelected(false);
                break;
            case '1':
                jToggleButton2.setSelected(false);
                jToggleButton18.setSelected(true);
                jToggleButton34.setSelected(false);
                break;
            case '0':
                jToggleButton2.setSelected(false);
                jToggleButton18.setSelected(false);
                jToggleButton34.setSelected(true);
                break;        
        }
        
        // bit 13
        switch(pod[2]){
            case 'x':
                jToggleButton3.setSelected(true);
                jToggleButton19.setSelected(false);
                jToggleButton35.setSelected(false);
                break;
            case '1':
                jToggleButton3.setSelected(false);
                jToggleButton19.setSelected(true);
                jToggleButton35.setSelected(false);
                break;
            case '0':
                jToggleButton3.setSelected(false);
                jToggleButton19.setSelected(false);
                jToggleButton35.setSelected(true);
                break;        
        }
        
        // bit 12
        switch(pod[3]){
            case 'x':
                jToggleButton4.setSelected(true);
                jToggleButton20.setSelected(false);
                jToggleButton36.setSelected(false);
                break;
            case '1':
                jToggleButton4.setSelected(false);
                jToggleButton20.setSelected(true);
                jToggleButton36.setSelected(false);
                break;
            case '0':
                jToggleButton4.setSelected(false);
                jToggleButton20.setSelected(false);
                jToggleButton36.setSelected(true);
                break;        
        }
        
        // bit 11        
        switch(pod[4]){
            case 'x':
                jToggleButton5.setSelected(true);
                jToggleButton21.setSelected(false);
                jToggleButton37.setSelected(false);
                break;
            case '1':
                jToggleButton5.setSelected(false);
                jToggleButton21.setSelected(true);
                jToggleButton37.setSelected(false);
                break;
            case '0':
                jToggleButton5.setSelected(false);
                jToggleButton21.setSelected(false);
                jToggleButton37.setSelected(true);
                break;        
        }        
        
        // bit 10
        switch(pod[5]){
            case 'x':
                jToggleButton6.setSelected(true);
                jToggleButton22.setSelected(false);
                jToggleButton38.setSelected(false);
                break;
            case '1':
                jToggleButton6.setSelected(false);
                jToggleButton22.setSelected(true);
                jToggleButton38.setSelected(false);
                break;
            case '0':
                jToggleButton6.setSelected(false);
                jToggleButton22.setSelected(false);
                jToggleButton38.setSelected(true);
                break;        
        }
        
        // bit 9
        switch(pod[6]){
            case 'x':
                jToggleButton7.setSelected(true);
                jToggleButton23.setSelected(false);
                jToggleButton39.setSelected(false);
                break;
            case '1':
                jToggleButton7.setSelected(false);
                jToggleButton23.setSelected(true);
                jToggleButton39.setSelected(false);
                break;
            case '0':
                jToggleButton7.setSelected(false);
                jToggleButton23.setSelected(false);
                jToggleButton39.setSelected(true);
                break;        
        }
        
        // bit 8
        switch(pod[7]){
            case 'x':
                jToggleButton8.setSelected(true);
                jToggleButton24.setSelected(false);
                jToggleButton40.setSelected(false);
                break;
            case '1':
                jToggleButton8.setSelected(false);
                jToggleButton24.setSelected(true);
                jToggleButton40.setSelected(false);
                break;
            case '0':
                jToggleButton8.setSelected(false);
                jToggleButton24.setSelected(false);
                jToggleButton40.setSelected(true);
                break;        
        }
        
        // bit 7
        switch(pod[8]){
            case 'x':
                jToggleButton9.setSelected(true);
                jToggleButton25.setSelected(false);
                jToggleButton41.setSelected(false);
                break;
            case '1':
                jToggleButton9.setSelected(false);
                jToggleButton25.setSelected(true);
                jToggleButton41.setSelected(false);
                break;
            case '0':
                jToggleButton9.setSelected(false);
                jToggleButton25.setSelected(false);
                jToggleButton41.setSelected(true);
                break;        
        }
        
        // bit 6
        switch(pod[9]){
            case 'x':
                jToggleButton10.setSelected(true);
                jToggleButton26.setSelected(false);
                jToggleButton42.setSelected(false);
                break;
            case '1':
                jToggleButton10.setSelected(false);
                jToggleButton26.setSelected(true);
                jToggleButton42.setSelected(false);
                break;
            case '0':
                jToggleButton10.setSelected(false);
                jToggleButton26.setSelected(false);
                jToggleButton42.setSelected(true);
                break;        
        }
        
        // bit 5
        switch(pod[10]){
            case 'x':
                jToggleButton11.setSelected(true);
                jToggleButton27.setSelected(false);
                jToggleButton43.setSelected(false);
                break;
            case '1':
                jToggleButton11.setSelected(false);
                jToggleButton27.setSelected(true);
                jToggleButton43.setSelected(false);
                break;
            case '0':
                jToggleButton11.setSelected(false);
                jToggleButton27.setSelected(false);
                jToggleButton43.setSelected(true);
                break;        
        }
        
        // bit 4
        switch(pod[11]){
            case 'x':
                jToggleButton12.setSelected(true);
                jToggleButton28.setSelected(false);
                jToggleButton44.setSelected(false);
                break;
            case '1':
                jToggleButton12.setSelected(false);
                jToggleButton28.setSelected(true);
                jToggleButton44.setSelected(false);
                break;
            case '0':
                jToggleButton12.setSelected(false);
                jToggleButton28.setSelected(false);
                jToggleButton44.setSelected(true);
                break;        
        }
        
        // bit 3
        switch(pod[12]){
            case 'x':
                jToggleButton13.setSelected(true);
                jToggleButton29.setSelected(false);
                jToggleButton45.setSelected(false);
                break;
            case '1':
                jToggleButton13.setSelected(false);
                jToggleButton29.setSelected(true);
                jToggleButton45.setSelected(false);
                break;
            case '0':
                jToggleButton13.setSelected(false);
                jToggleButton29.setSelected(false);
                jToggleButton45.setSelected(true);
                break;        
        }
        
        // bit 2
        switch(pod[13]){
            case 'x':
                jToggleButton14.setSelected(true);
                jToggleButton30.setSelected(false);
                jToggleButton46.setSelected(false);
                break;
            case '1':
                jToggleButton14.setSelected(false);
                jToggleButton30.setSelected(true);
                jToggleButton46.setSelected(false);
                break;
            case '0':
                jToggleButton14.setSelected(false);
                jToggleButton30.setSelected(false);
                jToggleButton46.setSelected(true);
                break;        
        }
        
        // bit 1
        switch(pod[14]){
            case 'x':
                jToggleButton15.setSelected(true);
                jToggleButton31.setSelected(false);
                jToggleButton47.setSelected(false);
                break;
            case '1':
                jToggleButton15.setSelected(false);
                jToggleButton31.setSelected(true);
                jToggleButton47.setSelected(false);
                break;
            case '0':
                jToggleButton15.setSelected(false);
                jToggleButton31.setSelected(false);
                jToggleButton47.setSelected(true);
                break;        
        }
        
        // bit 0
        switch(pod[15]){
            case 'x':
                jToggleButton16.setSelected(true);
                jToggleButton32.setSelected(false);
                jToggleButton48.setSelected(false);
                break;
            case '1':
                jToggleButton16.setSelected(false);
                jToggleButton32.setSelected(true);
                jToggleButton48.setSelected(false);
                break;
            case '0':
                jToggleButton16.setSelected(false);
                jToggleButton32.setSelected(false);
                jToggleButton48.setSelected(true);
                break;        
        }                
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        buttonGroup6 = new javax.swing.ButtonGroup();
        buttonGroup7 = new javax.swing.ButtonGroup();
        buttonGroup8 = new javax.swing.ButtonGroup();
        buttonGroup9 = new javax.swing.ButtonGroup();
        buttonGroup10 = new javax.swing.ButtonGroup();
        buttonGroup11 = new javax.swing.ButtonGroup();
        buttonGroup12 = new javax.swing.ButtonGroup();
        buttonGroup13 = new javax.swing.ButtonGroup();
        buttonGroup14 = new javax.swing.ButtonGroup();
        buttonGroup15 = new javax.swing.ButtonGroup();
        buttonGroup16 = new javax.swing.ButtonGroup();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jToggleButton5 = new javax.swing.JToggleButton();
        jToggleButton6 = new javax.swing.JToggleButton();
        jToggleButton7 = new javax.swing.JToggleButton();
        jToggleButton8 = new javax.swing.JToggleButton();
        jToggleButton9 = new javax.swing.JToggleButton();
        jToggleButton10 = new javax.swing.JToggleButton();
        jToggleButton11 = new javax.swing.JToggleButton();
        jToggleButton12 = new javax.swing.JToggleButton();
        jToggleButton13 = new javax.swing.JToggleButton();
        jToggleButton14 = new javax.swing.JToggleButton();
        jToggleButton15 = new javax.swing.JToggleButton();
        jToggleButton16 = new javax.swing.JToggleButton();
        jPanel2 = new javax.swing.JPanel();
        jToggleButton17 = new javax.swing.JToggleButton();
        jToggleButton18 = new javax.swing.JToggleButton();
        jToggleButton19 = new javax.swing.JToggleButton();
        jToggleButton20 = new javax.swing.JToggleButton();
        jToggleButton21 = new javax.swing.JToggleButton();
        jToggleButton22 = new javax.swing.JToggleButton();
        jToggleButton23 = new javax.swing.JToggleButton();
        jToggleButton24 = new javax.swing.JToggleButton();
        jToggleButton25 = new javax.swing.JToggleButton();
        jToggleButton26 = new javax.swing.JToggleButton();
        jToggleButton27 = new javax.swing.JToggleButton();
        jToggleButton28 = new javax.swing.JToggleButton();
        jToggleButton29 = new javax.swing.JToggleButton();
        jToggleButton30 = new javax.swing.JToggleButton();
        jToggleButton31 = new javax.swing.JToggleButton();
        jToggleButton32 = new javax.swing.JToggleButton();
        jPanel3 = new javax.swing.JPanel();
        jToggleButton33 = new javax.swing.JToggleButton();
        jToggleButton34 = new javax.swing.JToggleButton();
        jToggleButton35 = new javax.swing.JToggleButton();
        jToggleButton36 = new javax.swing.JToggleButton();
        jToggleButton37 = new javax.swing.JToggleButton();
        jToggleButton38 = new javax.swing.JToggleButton();
        jToggleButton39 = new javax.swing.JToggleButton();
        jToggleButton40 = new javax.swing.JToggleButton();
        jToggleButton41 = new javax.swing.JToggleButton();
        jToggleButton42 = new javax.swing.JToggleButton();
        jToggleButton43 = new javax.swing.JToggleButton();
        jToggleButton44 = new javax.swing.JToggleButton();
        jToggleButton45 = new javax.swing.JToggleButton();
        jToggleButton46 = new javax.swing.JToggleButton();
        jToggleButton47 = new javax.swing.JToggleButton();
        jToggleButton48 = new javax.swing.JToggleButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        setBorder(new javax.swing.border.TitledBorder("Trigger word selection"));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("15");
        jLabel1.setAlignmentY(0.0F);
        jLabel1.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel1.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel1.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel1);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("14");
        jLabel2.setAlignmentY(0.0F);
        jLabel2.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel2.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel2.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel2);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("13");
        jLabel3.setAlignmentY(0.0F);
        jLabel3.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel3.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel3.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel3);

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("12");
        jLabel4.setAlignmentY(0.0F);
        jLabel4.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel4.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel4.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel4);

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("11");
        jLabel5.setAlignmentY(0.0F);
        jLabel5.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel5.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel5.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel5);

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("10");
        jLabel6.setAlignmentY(0.0F);
        jLabel6.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel6.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel6.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel6);

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("9");
        jLabel7.setAlignmentY(0.0F);
        jLabel7.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel7.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel7.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel7);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("8");
        jLabel8.setAlignmentY(0.0F);
        jLabel8.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel8.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel8.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel8);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("7");
        jLabel9.setAlignmentY(0.0F);
        jLabel9.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel9.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel9.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel9);

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("6");
        jLabel10.setAlignmentY(0.0F);
        jLabel10.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel10.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel10.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel10);

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("5");
        jLabel11.setAlignmentY(0.0F);
        jLabel11.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel11.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel11.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel11);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("4");
        jLabel12.setAlignmentY(0.0F);
        jLabel12.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel12.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel12.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel12);

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("3");
        jLabel13.setAlignmentY(0.0F);
        jLabel13.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel13.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel13.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel13);

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("2");
        jLabel14.setAlignmentY(0.0F);
        jLabel14.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel14.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel14.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel14);

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("1");
        jLabel15.setAlignmentY(0.0F);
        jLabel15.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel15.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel15.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel15);

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("0");
        jLabel16.setAlignmentY(0.0F);
        jLabel16.setMaximumSize(new java.awt.Dimension(26, 26));
        jLabel16.setMinimumSize(new java.awt.Dimension(26, 26));
        jLabel16.setPreferredSize(new java.awt.Dimension(26, 26));
        jPanel4.add(jLabel16);

        add(jPanel4);

        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        jToggleButton1.setSelected(true);
        jToggleButton1.setText("X");
        buttonGroup16.add(jToggleButton1);
        jToggleButton1.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton1.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton1.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton1);

        jToggleButton2.setSelected(true);
        jToggleButton2.setText("X");
        buttonGroup15.add(jToggleButton2);
        jToggleButton2.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton2.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton2.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton2.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton2);

        jToggleButton3.setSelected(true);
        jToggleButton3.setText("X");
        buttonGroup14.add(jToggleButton3);
        jToggleButton3.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton3.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton3.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton3.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton3);

        jToggleButton4.setSelected(true);
        jToggleButton4.setText("X");
        buttonGroup13.add(jToggleButton4);
        jToggleButton4.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton4.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton4.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton4.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton4);

        jToggleButton5.setSelected(true);
        jToggleButton5.setText("X");
        buttonGroup12.add(jToggleButton5);
        jToggleButton5.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton5.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton5.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton5.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton5ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton5);

        jToggleButton6.setSelected(true);
        jToggleButton6.setText("X");
        buttonGroup11.add(jToggleButton6);
        jToggleButton6.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton6.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton6.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton6.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton6ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton6);

        jToggleButton7.setSelected(true);
        jToggleButton7.setText("X");
        buttonGroup10.add(jToggleButton7);
        jToggleButton7.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton7.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton7.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton7.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton7ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton7);

        jToggleButton8.setSelected(true);
        jToggleButton8.setText("X");
        buttonGroup9.add(jToggleButton8);
        jToggleButton8.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton8.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton8.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton8.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton8ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton8);

        jToggleButton9.setSelected(true);
        jToggleButton9.setText("X");
        buttonGroup8.add(jToggleButton9);
        jToggleButton9.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton9.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton9.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton9.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton9ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton9);

        jToggleButton10.setSelected(true);
        jToggleButton10.setText("X");
        buttonGroup7.add(jToggleButton10);
        jToggleButton10.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton10.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton10.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton10.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton10ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton10);

        jToggleButton11.setSelected(true);
        jToggleButton11.setText("X");
        buttonGroup6.add(jToggleButton11);
        jToggleButton11.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton11.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton11.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton11.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton11ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton11);

        jToggleButton12.setSelected(true);
        jToggleButton12.setText("X");
        buttonGroup5.add(jToggleButton12);
        jToggleButton12.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton12.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton12.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton12.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton12ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton12);

        jToggleButton13.setSelected(true);
        jToggleButton13.setText("X");
        buttonGroup4.add(jToggleButton13);
        jToggleButton13.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton13.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton13.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton13.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton13ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton13);

        jToggleButton14.setSelected(true);
        jToggleButton14.setText("X");
        buttonGroup3.add(jToggleButton14);
        jToggleButton14.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton14.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton14.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton14.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton14ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton14);

        jToggleButton15.setSelected(true);
        jToggleButton15.setText("X");
        buttonGroup2.add(jToggleButton15);
        jToggleButton15.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton15.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton15.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton15.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton15ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton15);

        jToggleButton16.setSelected(true);
        jToggleButton16.setText("X");
        buttonGroup1.add(jToggleButton16);
        jToggleButton16.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton16.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton16.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton16.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton16ActionPerformed(evt);
            }
        });

        jPanel1.add(jToggleButton16);

        add(jPanel1);

        jPanel2.setBorder(new javax.swing.border.EtchedBorder());
        jToggleButton17.setText("1");
        buttonGroup16.add(jToggleButton17);
        jToggleButton17.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton17.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton17.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton17.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton17ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton17);

        jToggleButton18.setText("1");
        buttonGroup15.add(jToggleButton18);
        jToggleButton18.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton18.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton18.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton18.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton18ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton18);

        jToggleButton19.setText("1");
        buttonGroup14.add(jToggleButton19);
        jToggleButton19.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton19.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton19.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton19.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton19ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton19);

        jToggleButton20.setText("1");
        buttonGroup13.add(jToggleButton20);
        jToggleButton20.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton20.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton20.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton20.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton20ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton20);

        jToggleButton21.setText("1");
        buttonGroup12.add(jToggleButton21);
        jToggleButton21.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton21.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton21.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton21.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton21ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton21);

        jToggleButton22.setText("1");
        buttonGroup11.add(jToggleButton22);
        jToggleButton22.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton22.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton22.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton22.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton22ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton22);

        jToggleButton23.setText("1");
        buttonGroup10.add(jToggleButton23);
        jToggleButton23.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton23.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton23.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton23.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton23ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton23);

        jToggleButton24.setText("1");
        buttonGroup9.add(jToggleButton24);
        jToggleButton24.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton24.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton24.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton24.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton24ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton24);

        jToggleButton25.setText("1");
        buttonGroup8.add(jToggleButton25);
        jToggleButton25.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton25.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton25.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton25.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton25ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton25);

        jToggleButton26.setText("1");
        buttonGroup7.add(jToggleButton26);
        jToggleButton26.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton26.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton26.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton26.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton26ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton26);

        jToggleButton27.setText("1");
        buttonGroup6.add(jToggleButton27);
        jToggleButton27.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton27.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton27.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton27.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton27ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton27);

        jToggleButton28.setText("1");
        buttonGroup5.add(jToggleButton28);
        jToggleButton28.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton28.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton28.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton28.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton28ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton28);

        jToggleButton29.setText("1");
        buttonGroup4.add(jToggleButton29);
        jToggleButton29.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton29.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton29.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton29.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton29ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton29);

        jToggleButton30.setText("1");
        buttonGroup3.add(jToggleButton30);
        jToggleButton30.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton30.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton30.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton30.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton30ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton30);

        jToggleButton31.setText("1");
        buttonGroup2.add(jToggleButton31);
        jToggleButton31.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton31.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton31.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton31.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton31ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton31);

        jToggleButton32.setText("1");
        buttonGroup1.add(jToggleButton32);
        jToggleButton32.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton32.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton32.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton32.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton32ActionPerformed(evt);
            }
        });

        jPanel2.add(jToggleButton32);

        add(jPanel2);

        jPanel3.setBorder(new javax.swing.border.EtchedBorder());
        jToggleButton33.setText("0");
        buttonGroup16.add(jToggleButton33);
        jToggleButton33.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton33.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton33.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton33.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton33ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton33);

        jToggleButton34.setText("0");
        buttonGroup15.add(jToggleButton34);
        jToggleButton34.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton34.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton34.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton34.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton34ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton34);

        jToggleButton35.setText("0");
        buttonGroup14.add(jToggleButton35);
        jToggleButton35.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton35.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton35.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton35.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton35ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton35);

        jToggleButton36.setText("0");
        buttonGroup13.add(jToggleButton36);
        jToggleButton36.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton36.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton36.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton36.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton36ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton36);

        jToggleButton37.setText("0");
        buttonGroup12.add(jToggleButton37);
        jToggleButton37.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton37.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton37.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton37.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton37ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton37);

        jToggleButton38.setText("0");
        buttonGroup11.add(jToggleButton38);
        jToggleButton38.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton38.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton38.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton38.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton38ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton38);

        jToggleButton39.setText("0");
        buttonGroup10.add(jToggleButton39);
        jToggleButton39.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton39.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton39.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton39.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton39ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton39);

        jToggleButton40.setText("0");
        buttonGroup9.add(jToggleButton40);
        jToggleButton40.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton40.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton40.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton40.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton40ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton40);

        jToggleButton41.setText("0");
        buttonGroup8.add(jToggleButton41);
        jToggleButton41.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton41.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton41.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton41.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton41ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton41);

        jToggleButton42.setText("0");
        buttonGroup7.add(jToggleButton42);
        jToggleButton42.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton42.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton42.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton42.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton42ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton42);

        jToggleButton43.setText("0");
        buttonGroup6.add(jToggleButton43);
        jToggleButton43.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton43.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton43.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton43.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton43ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton43);

        jToggleButton44.setText("0");
        buttonGroup5.add(jToggleButton44);
        jToggleButton44.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton44.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton44.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton44.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton44ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton44);

        jToggleButton45.setText("0");
        buttonGroup4.add(jToggleButton45);
        jToggleButton45.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton45.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton45.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton45.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton45ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton45);

        jToggleButton46.setText("0");
        buttonGroup3.add(jToggleButton46);
        jToggleButton46.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton46.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton46.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton46.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton46ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton46);

        jToggleButton47.setText("0");
        buttonGroup2.add(jToggleButton47);
        jToggleButton47.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton47.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton47.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton47.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton47ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton47);

        jToggleButton48.setText("0");
        buttonGroup1.add(jToggleButton48);
        jToggleButton48.setMargin(new java.awt.Insets(2, 2, 2, 2));
        jToggleButton48.setMaximumSize(new java.awt.Dimension(26, 26));
        jToggleButton48.setMinimumSize(new java.awt.Dimension(26, 26));
        jToggleButton48.setPreferredSize(new java.awt.Dimension(26, 26));
        jToggleButton48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton48ActionPerformed(evt);
            }
        });

        jPanel3.add(jToggleButton48);

        add(jPanel3);

    }//GEN-END:initComponents

    private void jToggleButton48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton48ActionPerformed
        // Add your handling code here:
        pod[15] = '0';
    }//GEN-LAST:event_jToggleButton48ActionPerformed

    private void jToggleButton47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton47ActionPerformed
        // Add your handling code here:
        pod[14] = '0';
    }//GEN-LAST:event_jToggleButton47ActionPerformed

    private void jToggleButton46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton46ActionPerformed
        // Add your handling code here:
        pod[13] = '0';
    }//GEN-LAST:event_jToggleButton46ActionPerformed

    private void jToggleButton45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton45ActionPerformed
        // Add your handling code here:
        pod[12] = '0';
    }//GEN-LAST:event_jToggleButton45ActionPerformed

    private void jToggleButton44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton44ActionPerformed
        // Add your handling code here:
        pod[11] = '0';
    }//GEN-LAST:event_jToggleButton44ActionPerformed

    private void jToggleButton43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton43ActionPerformed
        // Add your handling code here:
        pod[10] = '0';
    }//GEN-LAST:event_jToggleButton43ActionPerformed

    private void jToggleButton42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton42ActionPerformed
        // Add your handling code here:
        pod[9] = '0';
    }//GEN-LAST:event_jToggleButton42ActionPerformed

    private void jToggleButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton41ActionPerformed
        // Add your handling code here:
        pod[8] = '0';
    }//GEN-LAST:event_jToggleButton41ActionPerformed

    private void jToggleButton40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton40ActionPerformed
        // Add your handling code here:
        pod[7] = '0';
    }//GEN-LAST:event_jToggleButton40ActionPerformed

    private void jToggleButton39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton39ActionPerformed
        // Add your handling code here:
        pod[6] = '0';
    }//GEN-LAST:event_jToggleButton39ActionPerformed

    private void jToggleButton38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton38ActionPerformed
        // Add your handling code here:
        pod[5] = '0';
    }//GEN-LAST:event_jToggleButton38ActionPerformed

    private void jToggleButton37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton37ActionPerformed
        // Add your handling code here:
        pod[4] = '0';
    }//GEN-LAST:event_jToggleButton37ActionPerformed

    private void jToggleButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton36ActionPerformed
        // Add your handling code here:
        pod[3] = '0';
    }//GEN-LAST:event_jToggleButton36ActionPerformed

    private void jToggleButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton35ActionPerformed
        // Add your handling code here:
        pod[2] = '0';
    }//GEN-LAST:event_jToggleButton35ActionPerformed

    private void jToggleButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton34ActionPerformed
        // Add your handling code here:
        pod[1] = '0';
    }//GEN-LAST:event_jToggleButton34ActionPerformed

    private void jToggleButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton33ActionPerformed
        // Add your handling code here:
        pod[0] = '0';
    }//GEN-LAST:event_jToggleButton33ActionPerformed

    private void jToggleButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton32ActionPerformed
        // Add your handling code here:
        pod[15] = '1';
    }//GEN-LAST:event_jToggleButton32ActionPerformed

    private void jToggleButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton31ActionPerformed
        // Add your handling code here:
        pod[14] = '1';
    }//GEN-LAST:event_jToggleButton31ActionPerformed

    private void jToggleButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton30ActionPerformed
        // Add your handling code here:
        pod[13] = '1';
    }//GEN-LAST:event_jToggleButton30ActionPerformed

    private void jToggleButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton29ActionPerformed
        // Add your handling code here:
        pod[12] = '1';
    }//GEN-LAST:event_jToggleButton29ActionPerformed

    private void jToggleButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton28ActionPerformed
        // Add your handling code here:
        pod[11] = '1';
    }//GEN-LAST:event_jToggleButton28ActionPerformed

    private void jToggleButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton27ActionPerformed
        // Add your handling code here:
        pod[10] = '1';
    }//GEN-LAST:event_jToggleButton27ActionPerformed

    private void jToggleButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton26ActionPerformed
        // Add your handling code here:
        pod[9] = '1';
    }//GEN-LAST:event_jToggleButton26ActionPerformed

    private void jToggleButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton25ActionPerformed
        // Add your handling code here:
        pod[8] = '1';
    }//GEN-LAST:event_jToggleButton25ActionPerformed

    private void jToggleButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton24ActionPerformed
        // Add your handling code here:
        pod[7] = '1';
    }//GEN-LAST:event_jToggleButton24ActionPerformed

    private void jToggleButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton23ActionPerformed
        // Add your handling code here:
        pod[6] = '1';
    }//GEN-LAST:event_jToggleButton23ActionPerformed

    private void jToggleButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton22ActionPerformed
        // Add your handling code here:
        pod[5] = '1';
    }//GEN-LAST:event_jToggleButton22ActionPerformed

    private void jToggleButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton21ActionPerformed
        // Add your handling code here:
        pod[4] = '1';
    }//GEN-LAST:event_jToggleButton21ActionPerformed

    private void jToggleButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton20ActionPerformed
        // Add your handling code here:
        pod[3] = '1';
    }//GEN-LAST:event_jToggleButton20ActionPerformed

    private void jToggleButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton19ActionPerformed
        // Add your handling code here:
        pod[2] = '1';
    }//GEN-LAST:event_jToggleButton19ActionPerformed

    private void jToggleButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton18ActionPerformed
        // Add your handling code here:
        pod[1] = '1';
    }//GEN-LAST:event_jToggleButton18ActionPerformed

    private void jToggleButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton17ActionPerformed
        // Add your handling code here:
        pod[0] = '1';
    }//GEN-LAST:event_jToggleButton17ActionPerformed

    private void jToggleButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton16ActionPerformed
        // Add your handling code here:
        pod[15] = 'x';
    }//GEN-LAST:event_jToggleButton16ActionPerformed

    private void jToggleButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton15ActionPerformed
        // Add your handling code here:
        pod[14] = 'x';
    }//GEN-LAST:event_jToggleButton15ActionPerformed

    private void jToggleButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton14ActionPerformed
        // Add your handling code here:
        pod[13] = 'x';
    }//GEN-LAST:event_jToggleButton14ActionPerformed

    private void jToggleButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton13ActionPerformed
        // Add your handling code here:
        pod[12] = 'x';
    }//GEN-LAST:event_jToggleButton13ActionPerformed

    private void jToggleButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton12ActionPerformed
        // Add your handling code here:
        pod[11] = 'x';
    }//GEN-LAST:event_jToggleButton12ActionPerformed

    private void jToggleButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton11ActionPerformed
        // Add your handling code here:
        pod[10] = 'x';
    }//GEN-LAST:event_jToggleButton11ActionPerformed

    private void jToggleButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton10ActionPerformed
        // Add your handling code here:
        pod[9] = 'x';
    }//GEN-LAST:event_jToggleButton10ActionPerformed

    private void jToggleButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton9ActionPerformed
        // Add your handling code here:
        pod[8] = 'x';
    }//GEN-LAST:event_jToggleButton9ActionPerformed

    private void jToggleButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton8ActionPerformed
        // Add your handling code here:
        pod[7] = 'x';
    }//GEN-LAST:event_jToggleButton8ActionPerformed

    private void jToggleButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton7ActionPerformed
        // Add your handling code here:
        pod[6] = 'x';
    }//GEN-LAST:event_jToggleButton7ActionPerformed

    private void jToggleButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton6ActionPerformed
        // Add your handling code here:
        pod[5] = 'x';
    }//GEN-LAST:event_jToggleButton6ActionPerformed

    private void jToggleButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton5ActionPerformed
        // Add your handling code here:
        pod[4] = 'x';
    }//GEN-LAST:event_jToggleButton5ActionPerformed

    private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton4ActionPerformed
        // Add your handling code here:
        pod[3] = 'x';
    }//GEN-LAST:event_jToggleButton4ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        // Add your handling code here:
        pod[2] = 'x';
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        // Add your handling code here:
        pod[1] = 'x';
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // Add your handling code here:
        pod[0] = 'x';
    }//GEN-LAST:event_jToggleButton1ActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton jToggleButton29;
    private javax.swing.JToggleButton jToggleButton28;
    private javax.swing.JToggleButton jToggleButton27;
    private javax.swing.JToggleButton jToggleButton26;
    private javax.swing.JToggleButton jToggleButton25;
    private javax.swing.JToggleButton jToggleButton24;
    private javax.swing.JToggleButton jToggleButton23;
    private javax.swing.JToggleButton jToggleButton22;
    private javax.swing.JToggleButton jToggleButton21;
    private javax.swing.JToggleButton jToggleButton20;
    private javax.swing.ButtonGroup buttonGroup9;
    private javax.swing.ButtonGroup buttonGroup8;
    private javax.swing.ButtonGroup buttonGroup7;
    private javax.swing.ButtonGroup buttonGroup6;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton jToggleButton19;
    private javax.swing.JToggleButton jToggleButton18;
    private javax.swing.JToggleButton jToggleButton17;
    private javax.swing.JToggleButton jToggleButton16;
    private javax.swing.JToggleButton jToggleButton15;
    private javax.swing.JToggleButton jToggleButton14;
    private javax.swing.ButtonGroup buttonGroup16;
    private javax.swing.JToggleButton jToggleButton13;
    private javax.swing.ButtonGroup buttonGroup15;
    private javax.swing.JToggleButton jToggleButton12;
    private javax.swing.ButtonGroup buttonGroup14;
    private javax.swing.JToggleButton jToggleButton11;
    private javax.swing.ButtonGroup buttonGroup13;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JToggleButton jToggleButton10;
    private javax.swing.ButtonGroup buttonGroup12;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel15;
    private javax.swing.ButtonGroup buttonGroup11;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel14;
    private javax.swing.ButtonGroup buttonGroup10;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JToggleButton jToggleButton48;
    private javax.swing.JToggleButton jToggleButton47;
    private javax.swing.JToggleButton jToggleButton46;
    private javax.swing.JToggleButton jToggleButton45;
    private javax.swing.JToggleButton jToggleButton44;
    private javax.swing.JToggleButton jToggleButton43;
    private javax.swing.JToggleButton jToggleButton42;
    private javax.swing.JToggleButton jToggleButton41;
    private javax.swing.JToggleButton jToggleButton40;
    private javax.swing.JToggleButton jToggleButton39;
    private javax.swing.JToggleButton jToggleButton38;
    private javax.swing.JToggleButton jToggleButton37;
    private javax.swing.JToggleButton jToggleButton36;
    private javax.swing.JToggleButton jToggleButton35;
    private javax.swing.JToggleButton jToggleButton34;
    private javax.swing.JToggleButton jToggleButton33;
    private javax.swing.JToggleButton jToggleButton32;
    private javax.swing.JToggleButton jToggleButton31;
    private javax.swing.JToggleButton jToggleButton30;
    private javax.swing.JToggleButton jToggleButton9;
    private javax.swing.JToggleButton jToggleButton8;
    private javax.swing.JToggleButton jToggleButton7;
    private javax.swing.JToggleButton jToggleButton6;
    private javax.swing.JToggleButton jToggleButton5;
    private javax.swing.JToggleButton jToggleButton4;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables
    
}
