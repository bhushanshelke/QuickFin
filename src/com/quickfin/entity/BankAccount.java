package com.quickfin.entity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class BankAccount implements Serializable{

	public BankAccount(String acctId, double openingBalance, String firstName, String lastName, String id) {
		accountId = acctId;
		balance= openingBalance;
		this.firstName= firstName;
		this.lastName = lastName;
		this.id = id;
		
		Transaction first = new Transaction(Calendar.getInstance() , TransactionType.CR, openingBalance,
				0, acctId, "Initial Deposit");
		addTransaction(first);
	}
	
	public String getAccountId() {
		return accountId;
	}
	public double getBalance() {
		return balance;
	}
	public double addBalance(double bal) {
		balance += bal;
		return balance;
	}
	
	public List<Transaction> getTransactions() {
		return Collections.unmodifiableList(transactions);
	}

	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
	}
	
	
	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getId() {
		return id;
	}


	private String firstName;
	private String lastName;
	private String id;
	private String accountId;
	private double balance;
	private List<Transaction> transactions = new ArrayList<>();
}
