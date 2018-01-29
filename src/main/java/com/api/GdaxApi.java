package com.api;

import java.math.BigDecimal;

import javax.websocket.ClientEndpoint;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.client.CoinByExchangeMapper;
import com.client.CoinMapper;
import com.jsonmodel.PriceGdax;
import com.model.CoinByExchange;

import ch.qos.logback.classic.Logger;

@Configuration
@ClientEndpoint
@EnableScheduling
public class GdaxApi extends ApiProcessor {

	@Autowired
	private CoinByExchangeMapper coinByExchangeMapper;
	@Autowired
	private CoinMapper coinMapper;
	
	private static final String EXCHANGE_NAME = "GDAX";

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(GdaxApi.class);

	@Scheduled(cron = "*/10 * * * * *")
	public void startGdax() {
		LOG.info("INIT - startGdax");

		RestTemplate restTemplate = new RestTemplate();

		PriceGdax result = restTemplate.getForObject(getUrlApiByExchangeName(EXCHANGE_NAME, "BTC", "EUR"), PriceGdax.class);
		
		CoinByExchange reg = coinByExchangeMapper.selectByCoinExchange(coinMapper.selectByShortName("BTC").getId(), 1);
		
		String last = result.getPrice();
		insertQuotaAndSend(reg, new BigDecimal(last));

		LOG.info("END - startGdax");
	}

}
