package com.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.client.CoinByExchangeMapper;
import com.client.CoinMapper;
import com.client.CoinPerUserMapper;
import com.client.ExchangeMapper;
import com.client.UserInfoMapper;
import com.form.response.CoinPerUserConfigResponse;
import com.jsonmodel.PriceBinance;
import com.jsonmodel.PriceBittrex;
import com.model.Coin;
import com.model.CoinByExchange;
import com.model.CoinPerUser;
import com.model.Exchange;

@Controller
@RequestMapping("/coinPerUserConfig")
public class CoinPerUserConfigController extends PrincipalController {

	@Autowired
	private CoinPerUserMapper coinPerUserservice;
	@Autowired
	private CoinByExchangeMapper coinByExchangeMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private ExchangeMapper exchangeMapper;
	@Autowired
	private CoinMapper coinMapper;

	@RequestMapping
	public String configPerUser(Map<String, Object> model) {
		model.put("registroEditado", new CoinPerUser());

		return "coinPerUserConfig";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	@ResponseBody
	public List<CoinPerUser> coinPerUserList(Map<String, Object> model) {
		return coinPerUserservice.selectByUserLogin(getUserName());
	}

	@PostMapping(value = "/add", produces = { MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public CoinPerUserConfigResponse add(@Valid @ModelAttribute("registroEditado") CoinPerUser registroEditado,
			BindingResult bindingResult) {

		CoinPerUserConfigResponse response = new CoinPerUserConfigResponse();

		if (bindingResult.hasErrors()) {
			Map<String, String> errors = bindingResult.getFieldErrors().stream()
					.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

			response.setValidated(false);
			response.setErrorMessages(errors);
		} else {

			// Comprobamos si existe la combinación de moneda y exchange
			CoinByExchange coinByExchange = coinByExchangeMapper.selectByCoinExchange(
					registroEditado.getCoinByExchange().getCoin().getId(),
					registroEditado.getCoinByExchange().getExchange().getId());

			if (coinByExchange == null) {
				// Obtenemos el nombre del exchange para la api
				Exchange exchange = exchangeMapper
						.selectByPrimaryKey(registroEditado.getCoinByExchange().getExchange().getId());
				if ("BITTREX".equals(exchange.getName())) {

					Coin coin = coinMapper.selectByPrimaryKey(registroEditado.getCoinByExchange().getCoin().getId());
					RestTemplate restTemplate = new RestTemplate();

					// Llamamos a la api del exchange, si devuelve price es que este exchange tradea
					// la moneda
					String url = "https://bittrex.com/api/v1.1";
					PriceBittrex result = restTemplate.getForObject(
							url + "/public/getticker?market=BTC-" + coin.getShortName(), PriceBittrex.class);

					if (result.getAdditionalProperties().get("result") != null) {

						CoinByExchange reg = new CoinByExchange();
						reg.setCoin(coin);
						reg.setExchange(exchange);
						reg.setApiName(coin.getShortName() + "BTC");
						// Insertamos la combinación de moneda/exchange
						coinByExchangeMapper.insertSelective(reg);

						// Añadimos los datos insertados del usuario
						registroEditado.setCoinByExchange(reg);
						registroEditado.setUserInfo(userInfoMapper.selectByLogin(getUserName()));
						coinPerUserservice.insertSelective(registroEditado);
						response.setValidated(Boolean.TRUE);
						response.setCoinPerUser(new CoinPerUser());
					} else {
						// Si no existe la moneda en el exchange informamos de error
						response.setValidated(Boolean.FALSE);
						// TODO implementar error
					}
				} else if ("BINANCE".equals(exchange.getName())) {

					Coin coin = coinMapper.selectByPrimaryKey(registroEditado.getCoinByExchange().getCoin().getId());
					RestTemplate restTemplate = new RestTemplate();

					// Llamamos a la api del exchange, si devuelve price es que este exchange tradea
					// la moneda
					String url = "https://api.binance.com/api/v1";
					PriceBinance result = restTemplate.getForObject(
							url + "/ticker/price?symbol=" + coin.getShortName() + "BTC", PriceBinance.class);
					if (!StringUtils.isEmpty(result.getAdditionalProperties().get("price").toString())) {

						CoinByExchange reg = new CoinByExchange();
						reg.setCoin(coin);
						reg.setExchange(exchange);
						reg.setApiName(coin.getShortName() + "BTC");
						// Insertamos la combinación de moneda/exchange
						coinByExchangeMapper.insertSelective(reg);

						// Añadimos los datos insertados del usuario
						registroEditado.setCoinByExchange(reg);
						registroEditado.setUserInfo(userInfoMapper.selectByLogin(getUserName()));
						coinPerUserservice.insertSelective(registroEditado);
						response.setValidated(Boolean.TRUE);
						response.setCoinPerUser(new CoinPerUser());
					} else {
						// Si no existe la moneda en el exchange informamos de error
						response.setValidated(Boolean.FALSE);
						// TODO implementar error
					}
				}

			} else {
				// Si la combinación de la moneda/exchange existe añadimos los datos del usuario
				registroEditado.setCoinByExchange(coinByExchange);
				registroEditado.setUserInfo(userInfoMapper.selectByLogin(getUserName()));
				coinPerUserservice.insertSelective(registroEditado);
				response.setValidated(Boolean.TRUE);
				response.setCoinPerUser(new CoinPerUser());
			}

		}

		return response;

	}

	@RequestMapping(value = "/removeValue", method = RequestMethod.POST)
	@ResponseBody
	public void remove(@RequestParam("coinPerUserID") Integer coinPerUserID) {
		// TODO añadir seguridad

		coinPerUserservice.deleteByPrimaryKey(coinPerUserID);
	}

}
