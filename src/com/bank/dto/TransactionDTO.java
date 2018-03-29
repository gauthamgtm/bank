package com.bank.dto;

public class TransactionDTO {
	
	private long transactionId;
	private double transcactionAmount;
	private double balance;
	private String transcationType;
	private String date;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public double getTranscactionAmount() {
		return transcactionAmount;
	}

	public void setTranscactionAmount(double transcactionAmount) {
		this.transcactionAmount = transcactionAmount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public String getTranscationType() {
		return transcationType;
	}

	public void setTranscationType(String transcationType) {
		this.transcationType = transcationType;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	

}
