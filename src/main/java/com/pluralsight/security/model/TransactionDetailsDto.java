package com.pluralsight.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class TransactionDetailsDto {

	private String id;
	private String symbol;
	private String type;
	private String quantity;
	private String price;
	
}
