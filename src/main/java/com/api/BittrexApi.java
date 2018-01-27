package com.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.websocket.ClientEndpoint;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.client.CoinByExchangeMapper;
import com.client.CoinPerUserMapper;
import com.client.QuotationMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jsonmodel.PriceBittrex;
import com.jsonmodel.PriceChart;
import com.main.CalcSatoshis;
import com.main.UserOnline;
import com.model.CoinByExchange;
import com.model.Quotation;

import ch.qos.logback.classic.Logger;

@Configuration
@ClientEndpoint
@EnableScheduling
public class BittrexApi {

	@Autowired
	private CoinByExchangeMapper coinByExchangeMapper;
	@Autowired
	private QuotationMapper quotationMapper;
	@Autowired
	private CoinPerUserMapper service;
	@Autowired
	private SimpMessagingTemplate webSocket;
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(BittrexApi.class);
	private static final String PATH_API = "https://bittrex.com/api/v1.1/public/getticker?market=BTC-{0}";
	private String lastPrice = "0";

	@Scheduled(cron = "*/10 * * * * *")
	public void startBittrex() {
		LOG.info("INIT - startBittrex");

		List<CoinByExchange> list = coinByExchangeMapper.getAll();

		for (CoinByExchange crypto : list) {

			if (crypto.getExchange().getName().equals("BITTREX")) {
				RestTemplate restTemplate = new RestTemplate();
				String request = PATH_API.replace("{0}", crypto.getCoin().getShortName());

				if (LOG.isDebugEnabled()) {
					LOG.debug("Calling: " + request);
				}

				PriceBittrex result = restTemplate.getForObject(request, PriceBittrex.class);

				@SuppressWarnings("unchecked")
				String last = ((LinkedHashMap<String, Object>) result.getAdditionalProperties().get("result"))
						.get("Last").toString();
				lastPrice = last;
				Quotation record = new Quotation();
				CoinByExchange reg = new CoinByExchange();
				reg.setCoin(crypto.getCoin());
				reg.setExchange(crypto.getExchange());
				record.setCoinByExchange(reg);
				record.setSatoshis(new BigDecimal(lastPrice));
				record.setTimestamp(new Date());
				quotationMapper.insertSelective(record);
				PriceChart price = new PriceChart();
				price.setPrice(Double.valueOf(lastPrice));
				price.setTime(new Date().getTime());
				try {
					new CalcSatoshis().last(UserOnline.listUserOnline(), record, crypto.getExchange().getId(),
							crypto.getCoin().getId(), service, quotationMapper, webSocket);
				} catch (JsonProcessingException e) {
					LOG.error("Error calculating satoshis of " + crypto.getCoin().getShortName());
					LOG.error(e.getMessage());
				}
			}
		}
		LOG.info("END - startBittrex");
	}
}
