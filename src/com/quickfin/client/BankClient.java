package com.quickfin.client;

import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

import com.quickfin.entity.Transaction;
import com.quickfin.service.AccountService;
import com.quickfin.service.AccountServiceImpl;

public class BankClient {

	public static void main(String args[]) {
		AccountService accountService = new AccountServiceImpl();
		String accountId = accountService.createAccount("Bhushan", "Shelke", "111", 10);
		
		System.out.println("Initial balance="+accountService.getAccountBalance(accountId));
//		accountService.withdraw(accountId, 100);
		accountService.deposit(accountId, 100);
		accountService.withdraw(accountId, 110);
		System.out.println("Balance after withdraw="+accountService.getAccountBalance(accountId));
		messItUp(accountId, accountService);
	}
	
	private static <V> void messItUp(String accountId, AccountService accountService) {
		ForkJoinPool pool = new ForkJoinPool(5);
		DepositTask dtask1 = new DepositTask(accountId, accountService);
		DepositTask dtask2 = new DepositTask(accountId, accountService);
		WithdrawTask wTask1 = new WithdrawTask(accountId, accountService);
		WithdrawTask wTask2 = new WithdrawTask(accountId, accountService);
		WithdrawTask wTask3 = new WithdrawTask(accountId, accountService);
		WithdrawTask wTask4 = new WithdrawTask(accountId, accountService);
		WithdrawTask wTask5 = new WithdrawTask(accountId, accountService);
		
		pool.submit(dtask1);
		pool.submit(wTask1);
		pool.submit(dtask2);
		pool.submit(wTask2);
		pool.submit(wTask3);
		pool.submit(wTask4);
		pool.submit(wTask5);
		
	      do
	      {
	         try
	         {
	            TimeUnit.SECONDS.sleep(3);
	         } catch (InterruptedException e)
	         {
	            e.printStackTrace();
	         }
	      } while ((!dtask1.isDone()) || (!dtask1.isDone()
	    		  || (!wTask1.isDone()) || (!wTask2.isDone()))
	    		  || (!wTask3.isDone()) || (!wTask4.isDone())
	    		  || (!wTask5.isDone())
	    		  );
	    
	    pool.shutdown();
		System.out.println("Balance after deposits="+accountService.getAccountBalance(accountId));
		List<Transaction> txns = accountService.listTransactions(accountId, 0, 10);
		for(Transaction t :txns) {
			System.out.print("OB="+t.getOpeningBlance());
			System.out.print(" AMT="+t.getAmount());
			System.out.print(" TT="+t.getTransactionType());
			System.out.println();
		}
	}
}
