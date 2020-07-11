package com.pluralsight.security;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.pluralsight.security.entity.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import com.pluralsight.security.entity.CryptoCurrency;
import com.pluralsight.security.entity.Portfolio;
import com.pluralsight.security.repository.CryptoCurrencyRepository;
import com.pluralsight.security.repository.PortfolioRepository;

import lombok.RequiredArgsConstructor;

import static com.pluralsight.security.entity.Type.BUY;

@SpringBootApplication
@RequiredArgsConstructor
public class WebApplication {

	@Autowired
	private PortfolioRepository portfolioRepository;

	@Autowired
	private CryptoCurrencyRepository cryptoRepository;

	public static void main(String[] args) {
		SpringApplication.run(WebApplication.class, args);
	}	
	
	@EventListener(ApplicationReadyEvent.class)
	public void intializeUserData() {
		List<Transaction> transactions = new ArrayList<Transaction>();
		CryptoCurrency bitcoin = new CryptoCurrency("BTC", "Bitcoin");
		CryptoCurrency litecoin = new CryptoCurrency("LTC", "Litecoin");
        cryptoRepository.save(bitcoin);
        cryptoRepository.save(litecoin);
		transactions.add(new Transaction(bitcoin, BUY, new BigDecimal(3.1), new BigDecimal(15000.00), System.currentTimeMillis()));
		transactions.add(new Transaction(litecoin, BUY, new BigDecimal(20.1), new BigDecimal(13000.00), System.currentTimeMillis()));
		transactions.add(new Transaction(litecoin, BUY, new BigDecimal(200.1), new BigDecimal(130000.00), System.currentTimeMillis()));
		Portfolio portfolios = new Portfolio("user", "user", transactions);
		portfolioRepository.save(portfolios);
	}
	
}