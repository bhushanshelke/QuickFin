package com.quickfin.client;

import java.util.concurrent.RecursiveAction;

import com.quickfin.service.AccountService;

public class WithdrawTask extends RecursiveAction {

	private String accountId;
	private AccountService accountService;
	
	public WithdrawTask(String acctId, AccountService acctService) {
		accountId = acctId;
		accountService = acctService;
	}
	@Override
	protected void compute() {
		accountService.withdraw(accountId, 50);
	}

}
