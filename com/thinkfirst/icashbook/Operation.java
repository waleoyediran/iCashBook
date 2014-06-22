package com.thinkfirst.icashbook;
/**
 * @author ESSIENNTA EMMANUEL
 * @version 1.0
 */
public class Operation{
    private int command;
    private Date.Transaction transaction;
    private int[]indices;//the indices that were deleted.
    public Operation(int command,Date.Transaction transaction){
        this.command=command;
        this.transaction=transaction;
    }
    public Operation(int command,int[]indices){
        this.command=command;
        this.indices=indices;
    }
    public int getCommand(){
        return command;
    }
    public Date.Transaction getTransaction(){
        return transaction;
    }
    public int[] getDeletedIndices(){
        return indices;
    }
}
