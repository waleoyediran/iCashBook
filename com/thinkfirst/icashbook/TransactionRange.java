package com.thinkfirst.icashbook;
/**
 * @author ESSIENNTA EMMANUEL
 * @version 1.0
 */
//-------------USEFUL CLASS----------DON'T DELETE
public class TransactionRange implements java.io.Serializable{
    private java.util.ArrayList<java.util.ArrayList<Date.Transaction>>list=new java.util.ArrayList<java.util.ArrayList<Date.Transaction>>();
    public void add(java.util.ArrayList<Date.Transaction> transactions){
        list.add(transactions);
    }
    //returns the list of Transaction iterables.
    public java.util.ArrayList<java.util.ArrayList<Date.Transaction>>getSegments(){
        return list;
    }
}
