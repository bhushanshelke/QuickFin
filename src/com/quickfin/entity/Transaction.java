package com.quickfin.entity;

import java.io.Serializable;
import java.util.Calendar;

public class Transaction implements Serializable {
	
	public Calendar getTransactionDate() {
		Calendar txnDate = Calendar.getInstance();
		txnDate.setTimeInMillis(transactionDate.getTimeInMillis());
		return txnDate;
	}
	public TransactionType getTransactionType() {
		return transactionType;
	}
	public double getAmount() {
		return amount;
	}
	public String getAccountId() {
		return accountId;
	}
	public String getTransactionNotes() {
		return transactionNotes;
	}
	public double getOpeningBlance() {
		return openingBlance;
	}
	
	private Calendar transactionDate;
	private TransactionType transactionType;
	private double openingBlance;
	private double amount;
	private String accountId;
	private String transactionNotes;

	public Transaction(Calendar txnDate, TransactionType txnType, double opnBal,
			double txnAmt, String acctId, String txnNotes) {
		transactionDate=txnDate;
		transactionType=txnType;
		openingBlance=opnBal;
		amount=txnAmt;
		accountId=acctId;
		transactionNotes=txnNotes;
	}
}
