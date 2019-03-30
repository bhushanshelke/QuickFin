package com.quickfin.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionSearchCriteria {

	public static String TXN_DATE="transactionDate";
	public static String TXN_AMT="transactionAmount";
	public static String TXN_TYPE="transactionType";
	
	enum SearchType{EQ, LT, GT, RANGE};
	
	private SearchType type;
	private String searchAttributName;
	private Object value;
	private Object lowerValue;
	private Object upperValue;
	private long accountId;
	
	
	public TransactionSearchCriteria(long acctId) {
		accountId=acctId;
	}
	
	private TransactionSearchCriteria(long acctId, String attrName, SearchType searchType, Object searchValue) {
		this.searchAttributName=attrName;
		value=searchValue;
		type=searchType;
		accountId = acctId;
	}

	private TransactionSearchCriteria(long acctId, String attrName, SearchType searchType, Object criteria1, Object criteria2) {
		this.searchAttributName=attrName;
		lowerValue=criteria1;
		upperValue=criteria2;
		type=searchType;
		accountId = acctId;
	}
	
	public TransactionSearchCriteria eq(String name, Object criteria) {
		return new TransactionSearchCriteria(accountId, name, SearchType.EQ, criteria);
	}

	public TransactionSearchCriteria lt(String name, Object criteria) {
		return new TransactionSearchCriteria(accountId, name, SearchType.LT, criteria);
	}

	public TransactionSearchCriteria gt(String name, Object criteria) {
		return new TransactionSearchCriteria(accountId, name, SearchType.LT, criteria);
	}
	
	public TransactionSearchCriteria range(String name, Object criteria1, Object criteria2) {
		return new TransactionSearchCriteria(accountId, name, SearchType.RANGE, criteria1, criteria2);
	}	
}
