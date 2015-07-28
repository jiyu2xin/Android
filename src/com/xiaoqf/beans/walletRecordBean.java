package com.xiaoqf.beans;

public class walletRecordBean {
	/** 交易金额 */
	private String transactionAmount;
	/** 交易创建时间 */
	private String transactionCreateTime;
	/** 月份**/
	private String transactionMonth;
	/** 月总记录数**/
	private String transactionCounts;
	/** 年份**/
	private String transactionYear;
	/** 当月记录总数**/
	private String transactionCountsOfMount;
	
	public String getTransactionCounts() {
		return transactionCounts;
	}
	public void setTransactionCounts(String transactionCounts) {
		this.transactionCounts = transactionCounts;
	}
	public String getTransactionYear() {
		return transactionYear;
	}
	public void setTransactionYear(String transactionYear) {
		this.transactionYear = transactionYear;
	}
	
	public String getTransactionMonth() {
		return transactionMonth;
	}
	public void setTransactionMonth(String transactionMonth) {
		this.transactionMonth = transactionMonth;
	}
	public String getTransactionCountsOfMount() {
		return transactionCountsOfMount;
	}
	public void setTransactionCountsOfMount(String transactionCountsOfMount) {
		this.transactionCountsOfMount = transactionCountsOfMount;
	}
	public String getTransactionAmount() {
		return transactionAmount;
	}
	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	public String getTransactionCreateTime() {
		return transactionCreateTime;
	}
	public void setTransactionCreateTime(String transactionCreateTime) {
		this.transactionCreateTime = transactionCreateTime;
	}

}
