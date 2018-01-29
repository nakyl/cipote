package com.api;

import java.math.BigDecimal;
import java.util.List;

import javax.websocket.ClientEndpoint;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.jsonmodel.PriceBinance;
import com.model.CoinByExchange;
import com.model.Exchange;

import ch.qos.logback.classic.Logger;

@Configuration
@ClientEndpoint
@EnableScheduling
public class BinanceApi extends ApiProcessor {
	
	private static final String EXCHANGE_NAME = "BINANCE";
	
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(BinanceApi.class);

	@Scheduled(cron = "*/10 * * * * *")
	public void startBinance() {
		
		LOG.info("INIT - startBinance");

		Exchange exchange = getExchangeByName(EXCHANGE_NAME);
		List<CoinByExchange> list = getListByIdExchange(exchange.getId());

		for (CoinByExchange crypto : list) {
			BigDecimal price = getPriceCoin(crypto.getCoin().getShortName(), crypto.getCoinPair().getShortName());
			insertQuotaAndSend(crypto, price);
		}
		
		LOG.info("END - startBinance");
	}
	
	public static BigDecimal getPriceCoin(String shortNameCoin, String shortNameCoinPair) {

		RestTemplate restTemplate = new RestTemplate();
		PriceBinance result = restTemplate
				.getForObject(getUrlApiByExchangeName(EXCHANGE_NAME, shortNameCoin, shortNameCoinPair), PriceBinance.class);
		
		return new BigDecimal(result.getPrice());

	}
}
