package com.quickfin.service;

import java.util.List;

import com.quickfin.entity.BankAccount;
import com.quickfin.entity.Transaction;
import com.quickfin.entity.TransactionSearchCriteria;

public interface AccountService {
	int MAX_TX_RECORDS_PER_PAGE=100;
	int DEF_TX_RECORDS_PER_PAGE=20;
	
	public String createAccount(String firstName, String lastName, String id, double openingBalance);
	public double removeAccount(String accountId, String id);
	public double deposit(String accountId, double amount);
	public double withdraw(String accountId, double amount);
	public double getAccountBalance(String accountId);
	public List<Transaction> listTransactions(String accountId, int offSet, int noOfRecords);
	public List<Transaction> listTransactions(String accountId, TransactionSearchCriteria criteria);
}
