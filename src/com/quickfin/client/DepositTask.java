package com.quickfin.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import com.quickfin.service.AccountService;

public class DepositTask extends RecursiveAction {

	private String accountId;
	private AccountService accountService;
	
	public DepositTask(String acctId, AccountService acctService) {
		accountId = acctId;
		accountService = acctService;
	}
	
	@Override
	protected void compute() {
		List<DepositTask> list = new ArrayList<>();
		accountService.deposit(accountId, 100);
	}

}
