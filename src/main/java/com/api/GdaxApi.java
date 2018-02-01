package com.api;

import java.math.BigDecimal;
import java.util.List;

import javax.websocket.ClientEndpoint;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.client.CoinByExchangeMapper;
import com.jsonmodel.PriceGdax;
import com.model.CoinByExchange;
import com.model.Exchange;

import ch.qos.logback.classic.Logger;

@Configuration
@ClientEndpoint
@EnableScheduling
public class GdaxApi extends ApiProcessor {

	@Autowired
	private CoinByExchangeMapper coinByExchangeMapper;
	
	private static final String EXCHANGE_NAME = "GDAX";

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(GdaxApi.class);

	@Scheduled(cron = "*/10 * * * * *")
	public void startGdax() {
		LOG.info("INIT - startGdax");

		RestTemplate restTemplate = new RestTemplate();
		
		Exchange exchange = getExchangeByName(EXCHANGE_NAME);
		List<CoinByExchange> list = getListByIdExchange(exchange.getId());

		for (CoinByExchange crypto : list) {
			PriceGdax result = restTemplate.getForObject(getUrlApiByExchangeName(EXCHANGE_NAME, crypto.getCoin().getShortName(), crypto.getCoinPair().getShortName()), PriceGdax.class);
			
			CoinByExchange reg = coinByExchangeMapper.selectByCoinExchange(crypto.getCoin().getId(), exchange.getId());
			
			String last = result.getPrice();
			insertQuotaAndSend(reg, new BigDecimal(last));
		}



		LOG.info("END - startGdax");
	}

}
