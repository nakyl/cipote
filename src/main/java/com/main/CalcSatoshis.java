package com.main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.client.CoinMapper;
import com.client.CoinPerUserMapper;
import com.client.QuotationMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonmodel.PriceChart;
import com.model.CoinByExchange;
import com.model.CoinPerUser;
import com.model.Exchange;
import com.model.Quotation;

import ch.qos.logback.classic.Logger;

public class CalcSatoshis {
	
	private static final Logger LOG = (Logger) LoggerFactory.getLogger(CalcSatoshis.class);
	
	public void last(List<String> list, Quotation record, CoinByExchange crypto, CoinPerUserMapper service, QuotationMapper quotationMapper, SimpMessagingTemplate webSocket, CoinMapper coinMapper) throws JsonProcessingException {
		
		CoinByExchange reg = new CoinByExchange();
		Quotation quot = new Quotation();
		reg.setCoin(crypto.getCoinPair());
		Exchange exchange = new Exchange();
		exchange.setId(1);
		reg.setExchange(exchange);
		quot.setCoinByExchange(reg);
		
		Quotation btcPrice = quotationMapper.getLastExchangeCoin(quot);
		
		
		// CALCULO
		// TOTAL (UNIDADES MONEDA * (SATOSHIS ACTUALES RESULTADO API) * PRECIO BTC) - INVERTIDO
		for (String userLogin : list) {
			List<CoinPerUser> listCoinPerUser = getTotalsGroupedByCoin(service, userLogin, crypto);
			for (CoinPerUser coinPerUser : listCoinPerUser) {
					
				PriceChart resultToChart = new PriceChart();
				Double price = 
					(coinPerUser.getQuantity().doubleValue() * 
					record.getSatoshis().doubleValue() * 
					(coinPerUser.getCoinByExchange().getCoinPair().getIsFinal() ? 1D : btcPrice.getSatoshis().doubleValue())) 
					- coinPerUser.getInvested().doubleValue();
				
				
				resultToChart.setPrice(price);
				resultToChart.setTime(record.getTimestamp().getTime());
				resultToChart.setIdCrypto(crypto.getCoin().getId());
				ObjectMapper mapper = new ObjectMapper();
				String jsonInString = mapper.writeValueAsString(resultToChart);
				webSocket.convertAndSendToUser(userLogin, "/"+crypto.getCoin().getId() + "" + crypto.getExchange().getId(), jsonInString);
				LOG.info("send to /"+crypto.getCoin().getId() + "" + crypto.getExchange().getId());
			}
		}
	}
	
	private List<CoinPerUser> getTotalsGroupedByCoin(CoinPerUserMapper mapper, String user, CoinByExchange crypto) {
		List<CoinPerUser> totalsByCoin = mapper.selectByUserCryptoExcange(user, crypto.getCoin().getId(), crypto.getExchange().getId());
		Map<Integer, CoinPerUser> coinsMap = new HashMap<>();
		
		for (CoinPerUser coin:totalsByCoin) {
			if (coinsMap.containsKey(coin.getCoinByExchange().getCoin().getId())) {
				CoinPerUser innerCoin = coinsMap.get(coin.getCoinByExchange().getCoin().getId());
				coin.setQuantity(coin.getQuantity().add(innerCoin.getQuantity()));
				coin.setSatoshiBuy(coin.getSatoshiBuy().add(innerCoin.getSatoshiBuy()).divide(new BigDecimal(2)));
				coin.setInvested(coin.getInvested().add(innerCoin.getInvested()));
				
			}			
			coinsMap.put(coin.getCoinByExchange().getCoin().getId(), coin);
		}
		
		totalsByCoin.clear();
		for (CoinPerUser entry:coinsMap.values()) {
			totalsByCoin.add(entry);
		}
		
		return totalsByCoin;
	}
}