package com.pluralsight.security.entity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Document
@NoArgsConstructor
@Getter
public class Portfolio {

	@Id
	private String id;
	@NonNull
	private String username;
	private List<Transaction> transactions;

	public Portfolio(String id, List<Transaction> transactions) {
		this.id = id;
		this.transactions = transactions;
	}

    public Portfolio(String id, String username, List<Transaction> transactions) {
        this.id = id;
        this.username = username;
        this.transactions = transactions;
    }

	public void addTransaction(Transaction transaction) {
		this.transactions.add(transaction);
	}
	
	public List<Transaction> getTransactions() {
		return Collections.unmodifiableList(transactions);
	}
	
	public List<Transaction> getTransactionsForCoin(String symbol) {
		return transactions.stream().filter(transaction -> 
			transaction.getCryptoCurrency().getSymbol().equals(symbol)).collect(Collectors.toList());
	}
	
	public void deleteTransaction(Transaction transaction) {
		transactions.remove(transaction);
	}
	
	public Transaction getTransactionById(String id) {
		for(Transaction transaction : this.transactions) {
			if(id.equals(transaction.getId())) {
				return transaction;
			}
		}
		return null;
	}
	
}
