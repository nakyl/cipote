package com.main;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.client.CoinPerUserMapper;
import com.client.QuotationMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jsonmodel.PriceChart;
import com.model.Coin;
import com.model.CoinByExchange;
import com.model.CoinPerUser;
import com.model.Exchange;
import com.model.Quotation;

public class CalcSatoshis {
	
	public void last(List<String> list, Quotation record, Integer idExchange, Integer idCrypto, CoinPerUserMapper service, QuotationMapper quotationMapper, SimpMessagingTemplate webSocket) throws JsonProcessingException {
		
		CoinByExchange reg = new CoinByExchange();
		Quotation quot = new Quotation();
		Coin coin = new Coin();
		coin.setId(1);
		reg.setCoin(coin);
		Exchange exchange = new Exchange();
		exchange.setId(1);
		reg.setExchange(exchange);
		quot.setCoinByExchange(reg);
		
		Quotation btcPrice = quotationMapper.getLastExchangeCoin(quot);
		
		
		// CALCULO
		// TOTAL (UNIDADES MONEDA * (SATOSHIS ACTUALES RESULTADO API) * PRECIO BTC) - INVERTIDO
		for (String userLogin : list) {
			List<CoinPerUser> listCoinPerUser = getTotalsGroupedByCoin(service, userLogin, idCrypto, idExchange);
			for (CoinPerUser coinPerUser : listCoinPerUser) {
				if(coinPerUser.getCoinByExchange().getCoin().getId().equals(idCrypto) && 
						coinPerUser.getCoinByExchange().getExchange().getId().equals(idExchange)) {
					
					PriceChart resultToChart = new PriceChart();
					Double price = 
							(coinPerUser.getQuantity().doubleValue() * 
							record.getSatoshis().doubleValue() * 
							btcPrice.getSatoshis().doubleValue()) 
							- coinPerUser.getInvested().doubleValue();
					
					resultToChart.setPrice(price);
					resultToChart.setTime(record.getTimestamp().getTime());
					resultToChart.setIdCrypto(idCrypto);
					ObjectMapper mapper = new ObjectMapper();
					String jsonInString = mapper.writeValueAsString(resultToChart);
					webSocket.convertAndSendToUser(userLogin, "/"+idCrypto + "" + idExchange, jsonInString);
					System.out.println("send to /"+idCrypto + "" + idExchange);
				}
			}
		}
	}
	
	private List<CoinPerUser> getTotalsGroupedByCoin(CoinPerUserMapper mapper, String user, Integer idCrypto, Integer idExchange) {
		List<CoinPerUser> totalsByCoin = mapper.selectByUserCryptoExcange(user, idCrypto, idExchange);
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
