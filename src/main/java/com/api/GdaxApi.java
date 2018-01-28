package com.api;

import java.math.BigDecimal;
import java.util.Date;

import javax.websocket.ClientEndpoint;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.client.CoinPerUserMapper;
import com.client.QuotationMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jsonmodel.PriceBittrex;
import com.main.CalcSatoshis;
import com.main.UserOnline;
import com.model.Coin;
import com.model.CoinByExchange;
import com.model.Exchange;
import com.model.Quotation;

import ch.qos.logback.classic.Logger;

@Configuration
@ClientEndpoint
@EnableScheduling
public class GdaxApi {

	@Autowired
	private QuotationMapper quotationMapper;
	@Autowired
	private CoinPerUserMapper service;
	@Autowired
	private SimpMessagingTemplate webSocket;

	private static final String PATH_API = "https://api.gdax.com/products/BTC-EUR/ticker";
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(GdaxApi.class);

	@Scheduled(cron = "*/10 * * * * *")
	public void startGdax() {
		LOG.info("INIT - startGdax");

		RestTemplate restTemplate = new RestTemplate();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Calling " + PATH_API);
		}
		PriceBittrex result = restTemplate.getForObject(PATH_API, PriceBittrex.class);

		String last = result.getAdditionalProperties().get("price").toString();
		Quotation record = new Quotation();
		CoinByExchange reg = new CoinByExchange();
		Coin coin = new Coin();
		coin.setId(1);
		reg.setCoin(coin);
		Exchange exchange = new Exchange();
		exchange.setId(1);
		reg.setExchange(exchange);
		record.setCoinByExchange(reg);
		record.setSatoshis(new BigDecimal(last));
		record.setTimestamp(new Date());
		quotationMapper.insertSelective(record);
		try {
			new CalcSatoshis().last(UserOnline.listUserOnline(), record, exchange.getId(), coin.getId(), service, quotationMapper, webSocket);
		} catch (JsonProcessingException e) {
			LOG.error("",e);
		}
		LOG.info("END - startGdax");
	}

}
