package com.pluralsight.security.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class ListTransactionsDto {

	private String username;
	private List<TransactionDetailsDto> transactions;
	
}
