package com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.client.CoinMapper;
import com.jsonmodel.PriceBittrex;
import com.model.Coin;
import com.model.CoinByExchangeTestVO;

@Controller
@RequestMapping("/coinByExchangeTest")
public class CoinByExchangeTest extends PrincipalController {

	@Autowired
	private CoinMapper coinService;

	static final String BASE_API = "https://api.binance.com/api/v1";

	@RequestMapping
	public String configPerUser(Map<String, Object> model) {

		List<CoinByExchangeTestVO> test = new ArrayList<>();
		List<Coin> coins = coinService.selectAll();
		for (Coin coin : coins) {
			System.out.println(coins.indexOf(coin) + " de " + coins.size());
			RestTemplate restTemplate = new RestTemplate();
			try {
				PriceBittrex result = restTemplate.getForObject(
						BASE_API + "/ticker/price?symbol=" + coin.getShortName() + "BTC", PriceBittrex.class);
				if (StringUtils.isEmpty(result.getAdditionalProperties().get("price").toString())) {
					test.add(new CoinByExchangeTestVO(false, "BINANCE", coin.getFormatedName()));
				} else {
					test.add(new CoinByExchangeTestVO(true, "BINANCE", coin.getFormatedName()));
				}
			} catch (Exception e) {
				test.add(new CoinByExchangeTestVO(false, "BINANCE", coin.getFormatedName()));
			}
		}

		model.put("registroEditado", test);

		return "coinByExchangeTest";
	}

}
