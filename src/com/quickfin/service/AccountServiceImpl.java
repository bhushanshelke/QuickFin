package com.quickfin.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import com.quickfin.entity.BankAccount;
import com.quickfin.entity.Transaction;
import com.quickfin.entity.TransactionSearchCriteria;
import com.quickfin.entity.TransactionType;

public class AccountServiceImpl implements AccountService {

	Map<String, BankAccount> accounts= new ConcurrentHashMap<>();
	Map<String, Lock> accountLocks= new ConcurrentHashMap<>();
	
	@Override
	public double deposit(String accountId, double amount) {
		System.out.println("depositing="+amount);
		BankAccount account = accounts.get(accountId);
		Lock lock = accountLocks.get(accountId);
		try {
			lock.lock();
			double opBal= getAccountBalance(accountId);
			double balance = account.addBalance(amount);
			System.out.println("deposited="+amount + " opBal="+opBal);
			Transaction deposit = new Transaction(Calendar.getInstance() , 
					TransactionType.CR, opBal, amount,
					accountId, "Deposit");
			account.addTransaction(deposit);
			reconcile(accountId);
			return balance;
		}finally {
			lock.unlock();
		}
	}

	@Override
	public double withdraw(String accountId, double amount) {
		System.out.println("withdrawing="+amount);
		BankAccount account = accounts.get(accountId);
		Lock lock = accountLocks.get(accountId);
		try {
			lock.lock();
			
			if(getAccountBalance(accountId) < amount) {
				System.out.println("Insufficent Balance");
				throw new RuntimeException("Insufficent Balance for AccountId=" + accountId);
			}
			double opBal= getAccountBalance(accountId);
			double balance = account.addBalance(amount*(-1));
			System.out.println("withdrawn="+amount+ " opBal="+opBal);
			Transaction withdrawal = new Transaction(Calendar.getInstance() , 
					TransactionType.DR, opBal, amount,
					 accountId, "Withdraw");
			account.addTransaction(withdrawal);
			reconcile(accountId);
			return balance;
		}finally {
			lock.unlock();
		}
	}

	private void reconcile(String accountId) {
		List<Transaction> txnList = accounts.get(accountId).getTransactions();
		double balance = accounts.get(accountId).getBalance();
		double totalCredit = txnList.stream().filter(t->t.getTransactionType().equals(TransactionType.CR))
		.mapToDouble(new ToDoubleFunction<Transaction>() {

			@Override
			public double applyAsDouble(Transaction value) {
				return value.getAmount()==0?value.getOpeningBlance():value.getAmount();
			}
		}).sum();
		
		System.out.println("totalCredit="+totalCredit);
		double totalDebit = txnList.stream().filter(t->t.getTransactionType().equals(TransactionType.DR))
		.mapToDouble(new ToDoubleFunction<Transaction>() {

			@Override
			public double applyAsDouble(Transaction value) {
				return value.getAmount();
			}
		}).sum();
		
		System.out.println("totalDebit="+totalDebit);
		System.out.println("balance In Account="+balance);
		double txnBalance = (totalCredit-totalDebit);
		System.out.println("balance From Transaction="+ txnBalance);
		if(balance != txnBalance) {
			System.out.println("Reconciliation failed");
			throw new RuntimeException("Reconciliation failed");
		}
	}
	
	@Override
	public double getAccountBalance(String accountId) {
		Lock lock = accountLocks.get(accountId);
		try {
			lock.lock();
			if(accounts.get(accountId) == null) {
				System.out.println("Invalid Account No=" + accountId);
				throw new RuntimeException("Invalid Account No");
			}
			return accounts.get(accountId).getBalance();
		}finally {
			lock.unlock();
		}
	}

	@Override
	public List<Transaction> listTransactions(String accountId, int offSet, int noOfRecords) {
		List<Transaction> list = new ArrayList<>();
		List<Transaction> txnList = accounts.get(accountId).getTransactions();
		list.addAll(txnList.stream().skip(offSet).limit(noOfRecords).collect(Collectors.toList()));
		return list;
	}

	@Override
	public List<Transaction> listTransactions(String accountId, TransactionSearchCriteria criteria) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createAccount(String firstName, String lastName, String id, double openingBalance) {
		// TODO Auto-generated method stub
		String accountId=UUID.randomUUID().toString();
		BankAccount account = new BankAccount(accountId, openingBalance, firstName, lastName, id);
		accounts.put(accountId, account);
		accountLocks.put(accountId, new ReentrantLock());
		return account.getAccountId();
	}

	@Override
	public double removeAccount(String accountId, String id) {
		if(accounts.containsKey(accountId)){
			BankAccount account = accounts.remove(accountId);
			Lock lock = accountLocks.remove(accountId);
			if(lock !=null) {
				try {
					lock.unlock();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return account.getBalance();
		}
		
		throw new RuntimeException("Account with accountd=" + accountId + " not avaiable");
	}

}
