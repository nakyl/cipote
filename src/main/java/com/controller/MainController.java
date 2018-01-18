package com.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.client.CoinByExchangeMapper;
import com.client.CoinMapper;
import com.client.CoinPerUserMapper;
import com.client.ExchangeMapper;
import com.client.QuotationMapper;
import com.client.UserInfoMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.jsonmodel.PriceChart;
import com.main.CalcSatoshisHist;
import com.model.Coin;
import com.model.CoinByExchange;
import com.model.CoinPerUser;
import com.model.Exchange;
import com.model.Quotation;

@Controller
public class MainController {
	
	@Autowired
	private CoinPerUserMapper coinPerUserservice;
	@Autowired
	private QuotationMapper quotationMapper;
	@Autowired
	private CoinByExchangeMapper coinByExchangeMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	@Autowired
	private ExchangeMapper exchangeMapper;
	@Autowired
	private CoinMapper coinMapper;

	@GetMapping("/")
	public String welcome(Map<String, Object> model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     String name = null;
	    if (auth != null) {
	        Object principal = auth.getPrincipal();
	        name = ((String) principal);
	    }

	    for (CoinPerUser coinUser : coinPerUserservice.selectByUserLogin(name)) {
	    	quotationMapper.selectByBuyDate(
	    			coinUser.getCoinByExchange().getCoin(), coinUser.getCoinByExchange().getExchange(), coinUser.getBuyDate());
		}
		model.put("crypto", coinPerUserservice.selectByUserLogin(name));
		
		return "index";
	}
	
	@RequestMapping(value = "/configPerUser", method = RequestMethod.GET)
	public String configPerUser(Map<String, Object> model) {
		model.put("registroEditado", new CoinPerUser());
		
		return "configUserPerCoin";
	}
	
	@RequestMapping(value = "/coinPerUserList", method = RequestMethod.GET)
	@ResponseBody
	public List<CoinPerUser> coinPerUserList(Map<String, Object> model) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     String name = null;
	    if (auth != null) {
	        Object principal = auth.getPrincipal();
	        name = ((String) principal);
	    }

	    
		
		return coinPerUserservice.selectByUserLogin(name);
	}
	
	@RequestMapping(value = "/exchangeList", method = RequestMethod.GET)
	@ResponseBody
	public List<Exchange> exchangeList(Map<String, Object> model) {
		
		
		return exchangeMapper.selectAll();
	}
	
	@RequestMapping(value = "/coinList", method = RequestMethod.GET)
	@ResponseBody
	public List<Coin> coinList(Map<String, Object> model) {
		return coinMapper.selectAll();
	}
	
	
	@RequestMapping(value="/addCoinPerUser", method=RequestMethod.POST)
	public String coinPerUserList(@ModelAttribute CoinPerUser coinPerUser, Model m) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     String name = null;
	    if (auth != null) {
	        Object principal = auth.getPrincipal();
	        name = ((String) principal);
	    }
	    
	    CoinByExchange coinByExchange = coinByExchangeMapper.selectByCoinExchange(coinPerUser.getCoinByExchange().getCoin().getId(),
	    		coinPerUser.getCoinByExchange().getExchange().getId());
	    
	    coinPerUser.setCoinByExchange(coinByExchange);
	    coinPerUser.setUserInfo(userInfoMapper.selectByLogin(name));
	    // TODO
	    coinPerUser.setBuyDate(new Date());
	    coinPerUserservice.insertSelective(coinPerUser);
	    
	    return "coinPerUserList";
		
	}
	
	@RequestMapping(value = "/getHistoricData", method = RequestMethod.GET)
	@ResponseBody
	public List<PriceChart> welcome(@RequestParam("idCrypto") Integer idCrypto, @RequestParam("idExchange") Integer idExchange) throws JsonProcessingException {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     String name = null;
	    if (auth != null) {
	        Object principal = auth.getPrincipal();
	        name = ((String) principal);
	    }

	    List<PriceChart> result = new ArrayList<>();
	    for (CoinPerUser coinUser : coinPerUserservice.selectByUserCryptoExcange(name, idCrypto, idExchange)) {
	    	List<Quotation> quota = quotationMapper.selectByBuyDate(
	    			coinUser.getCoinByExchange().getCoin(), coinUser.getCoinByExchange().getExchange(), coinUser.getBuyDate());
	    	
	    	for (Quotation quotation : quota) {
	    		Coin coin = new Coin();
	    		coin.setId(1);
	    		Exchange exchange = new Exchange();
	    		exchange.setId(1);
	    		Quotation btcPrice = quotationMapper.selectCoinByDate(
	    				coin, exchange, quotation.getTimestamp());
	    		if(btcPrice != null) {
	    			result.add(new CalcSatoshisHist().calc(coinUser, quotation, btcPrice.getSatoshis().doubleValue()));
	    		}
	    	}
	    	
	    	
	    	
		}
		
		return result;
	}

}
