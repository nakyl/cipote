package com.main;

import java.util.List;

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
			List<CoinPerUser> listCoinPerUser = service.selectByUserCryptoExcange(userLogin, idCrypto, idExchange);
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
}
