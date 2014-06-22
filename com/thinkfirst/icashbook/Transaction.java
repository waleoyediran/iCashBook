package com.thinkfirst.icashbook;
/**
 * @author ESSIENNTA EMMANUEL
 * @version 1.0
 */
public class Transaction extends OnButton{
    private static final java.text.DateFormat timeFormatter=java.text.DateFormat.getTimeInstance(java.text.DateFormat.MEDIUM);
    //280-3*3=271
    private static final int MAX_TEXT_WIDTH=395/*max total width*/-3/*width of a dot*/*3/*3 dots (elipsis)*/;
    private java.awt.FontMetrics fontMetrics;
    private int maxAdvance;
    /**
     * 
     * @return true if the check box is checked. false otherwise.
     */
    public boolean isChecked(){
        return isChecked==1;
    }
    private int isChecked=-1;
    private OnButton checkBox=new OnButton("checkOn.gif","checkOff.gif"){//Change zone
        private java.awt.Image checkBoxImage=App.getImage("check.gif");
        public void doAction(){
            isChecked=-isChecked;
            selectionCount[0]+=isChecked;
        }
        public void paintComponent(java.awt.Graphics g){
            super.paintComponent(g);
            if(isChecked==1){
                java.awt.Graphics2D g2d=(java.awt.Graphics2D)g;
                g2d.drawImage(checkBoxImage,3,4,null);
            }
        }
        public void mouseEntered(java.awt.event.MouseEvent e){
            Transaction.this.setState(ENTERED);
            super.mouseEntered(e);
        }
        public void mouseExited(java.awt.event.MouseEvent e){
            super.mouseExited(e);
        }
        public void mouseClicked(java.awt.event.MouseEvent e){
            isChecked=-isChecked;
            selectionCount[0]+=isChecked;
            repaint();
            requestFocusInWindow();
        }
        public void keyReleased(java.awt.event.KeyEvent e){
            if(e.getKeyCode()==java.awt.event.KeyEvent.VK_SPACE){
                doAction();
                repaint();
            }
        }
        public void focusGained(java.awt.event.FocusEvent e){
            super.focusGained(e);
            Transaction.this.setState(ENTERED);
        }
        public void focusLost(java.awt.event.FocusEvent e){
            super.focusLost(e);
            Transaction.this.setState(EXITED);
        }
    };
    private OnButton editMenu=new OnButton("edit_entered.gif","edit_exited.gif"){
        {setToolTipText("Edit this entry");}
        public void doAction(){
            if(!App.getLogin().isAdminLogin()){
                java.awt.Toolkit.getDefaultToolkit().beep();
                return;
            }
            
            //set all fields to their values
            
            Trans trans=App.getTrans();
            trans.setTransaction(Transaction.this);//It's important that this comes before the trans object is set visible
            
            trans.setParticular(particular);
            trans.setMode(transactionType);
            trans.setAmount(amount);
            
            //Pop up the edit dialog
            trans.setVisible(true);
        }
        public void mouseEntered(java.awt.event.MouseEvent e){
            Transaction.this.setState(ENTERED);
            super.mouseEntered(e);
        }
        public void focusGained(java.awt.event.FocusEvent e){
            super.focusGained(e);
            Transaction.this.setState(ENTERED);
        }
        public void focusLost(java.awt.event.FocusEvent e){
            super.focusLost(e);
            Transaction.this.setState(EXITED);
        }
    };
    public void mouseExited(java.awt.event.MouseEvent e){
        super.mouseExited(e);
    }
    /**
     * Method to convert a string to its short form.
     * If the string is too long to fit in the maximum width,
     * it is expressed in ellipsis form.
     * @return the string in its short form.
     */
    private String setShortParticular(String particular){
        byte[]bytes=particular.getBytes();        
        maxAdvance=fontMetrics.getMaxAdvance();
        
        int minNumOfChars=MAX_TEXT_WIDTH/maxAdvance;
        if(bytes.length<=minNumOfChars)
            return particular;//Bound to contain this since this is for MAXIMUM
        int currentAdvance=fontMetrics.bytesWidth(bytes,0,minNumOfChars);
        int index=minNumOfChars;//index of next character to be checked.
        boolean isWithin=true;//true if 'particular' can be used without ellipsis
        while(index<bytes.length){
            isWithin=false;
            int charWidth=fontMetrics.charWidth(bytes[index]);
            if(currentAdvance+charWidth>=MAX_TEXT_WIDTH)
                break;
            isWithin=true;
            currentAdvance+=charWidth;
            index++;
        }
        return new String(bytes,0,index)+(isWithin?"":"...");
    }
    private int serialNo;
    private String particular;
    private int amount;
    private int transactionType;
    private int[]selectionCount;
    
    private String shortParticular;
    public Transaction(int[]selectionCount,int serialNo,String particular,int transactionType,int amount,java.util.Date date){
        super("transactionRow_entered.gif","transactionRow_exited.gif");
        this.removeFocusListener(this);
        this.selectionCount=selectionCount;
        this.serialNo=serialNo;
        this.particular=particular;        
        //Calculate the width used by this particular
        this.amount=amount;
        this.transactionType=transactionType;
        formattedDate=timeFormatter.format(date);
        checkBox.setSize(15,15);
        checkBox.setLocation(10,6);
        add(checkBox);

        editMenu.setSize(16,11);
        editMenu.setLocation(912,10);
        add(editMenu);
    }
    private boolean firstTime=true;
    private java.awt.Font amountFont=CashBookConstants.NUMERIC_FONT.deriveFont(15f),
            otherFont=CashBookConstants.OTHER_FONT.deriveFont(12f);
    private int amountWidth, serialWidth;
    private java.awt.FontMetrics amountMetrics;
    private String formattedDate;
    private String comma$Amount;
    public void paintComponent(java.awt.Graphics g){
        super.paintComponent(g);
        java.awt.Graphics2D g2d=(java.awt.Graphics2D)g;
        g2d.drawImage(currentImage,0,0,null);
        
        if(firstTime){
            fontMetrics=g2d.getFontMetrics(otherFont);
            shortParticular=setShortParticular(particular);
            comma$Amount=ICashBook.encodeAmount(""+amount);
            amountMetrics=g2d.getFontMetrics(amountFont);
            amountWidth=amountMetrics.stringWidth(comma$Amount);//669
            serialWidth=amountMetrics.stringWidth(""+serialNo);
            firstTime=false;
        }
        g2d.setFont(otherFont);
        g2d.drawString(shortParticular,100,19);
        g2d.drawString(transactionType==CashBookConstants.CREDIT?"Cr.":"Dr.",710,19);
        g2d.drawString(formattedDate,795,19);
        
        g2d.setFont(amountFont);
        g2d.drawString(""+serialNo,40+(49-serialWidth)/2,19);
        if(transactionType==CashBookConstants.DEBIT){
            if(g2d.getColor()!=java.awt.Color.RED)
                g2d.setColor(java.awt.Color.RED);
        }else
            if(g2d.getColor()!=java.awt.Color.BLACK)
                g2d.setColor(java.awt.Color.BLACK);
        g2d.drawString(comma$Amount,651-amountWidth,19);
    }
    public void doAction(){
        //do nothing!
    }
    public boolean isFocusable(){
        return false;
    }
    public int getAmount(){
        return amount;
    }
    public String getParticular(){
        return particular;
    }
    public int getSerialNo(){
        return serialNo;
    }
    public int getTransactionType(){
        return transactionType;
    }
    public void setSerialNo(int serialNo){
        this.serialNo=serialNo;        
        serialWidth=amountMetrics.stringWidth(""+serialNo);
    }
    public void setFields(String particular,int transactionType,int amount,java.util.Date date){
        this.particular=particular;
        this.transactionType=transactionType;
        this.amount=amount;
        formattedDate=timeFormatter.format(date);
        shortParticular=setShortParticular(particular);
        comma$Amount=ICashBook.encodeAmount(""+amount);
        amountWidth=amountMetrics.stringWidth(comma$Amount);//669
        serialWidth=amountMetrics.stringWidth(""+serialNo);
        repaint();
    }
    
}
