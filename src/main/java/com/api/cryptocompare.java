package com.api;

import java.util.Map;
import java.util.Map.Entry;

import javax.websocket.ClientEndpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import com.client.CoinMapper;
import com.jsonmodel.PriceBittrex;
import com.model.Coin;

@Configuration
@ClientEndpoint
@EnableScheduling
public class cryptocompare {
	
	@Autowired
	private CoinMapper coinMapper;

	static final String BASE_API = "https://www.cryptocompare.com/api/data/coinlist/";

	@Scheduled(fixedDelay = 86400000)
	public void startBittrex() {

				RestTemplate restTemplate = new RestTemplate();
				PriceBittrex result = restTemplate
						.getForObject(BASE_API, PriceBittrex.class);

				Map<String, Map<String,String>> last = (Map<String, Map<String,String>>)result.getAdditionalProperties().get("Data");
				for (Entry<String, Map<String, String>> map : last.entrySet()) {
					Coin coin = new Coin();
					coin.setName(map.getValue().get("CoinName"));
					if(coinMapper.selectByName(coin) == null) {
						coin.setShortName(map.getValue().get("Name"));
						coinMapper.insertSelective(coin);
					}
					
				}
			}

}
