package com.api;

import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.ClientEndpoint;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.client.CoinMapper;
import com.jsonmodel.PriceBittrex;
import com.model.Coin;

import ch.qos.logback.classic.Logger;

@Configuration
@ClientEndpoint
@EnableScheduling
public class Cryptocompare {

	@Autowired
	private CoinMapper coinMapper;

	private static final String PATH_API = "https://www.cryptocompare.com/api/data/coinlist/";
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(Cryptocompare.class);

	@Scheduled(fixedDelay = 86400000)
	public void startCryptocompare() {
		LOG.info("INIT - startCryptocompare");
		RestTemplate restTemplate = new RestTemplate();

		if (LOG.isDebugEnabled()) {
			LOG.debug("Calling " + PATH_API);
		}
		// TODO está esto correcto? por qué usamos aquí también PriceBittrex? Se está
		// ejecutando esto?
		PriceBittrex result = restTemplate.getForObject(PATH_API, PriceBittrex.class);

		@SuppressWarnings("unchecked")
		Map<String, Map<String, String>> last = (Map<String, Map<String, String>>) result.getAdditionalProperties()
				.get("Data");
		for (Entry<String, Map<String, String>> map : last.entrySet()) {
			Coin coin = new Coin();
			coin.setName(map.getValue().get("CoinName"));
			if (coinMapper.selectByName(coin) == null) {
				coin.setShortName(map.getValue().get("Name"));
				coinMapper.insertSelective(coin);
			}
		}
		LOG.info("END - startCryptocompare");
	}

}
