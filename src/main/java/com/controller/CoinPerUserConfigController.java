package com.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.client.CoinByExchangeMapper;
import com.client.CoinPerUserMapper;
import com.client.UserInfoMapper;
import com.form.response.CoinPerUserConfigResponse;
import com.model.CoinByExchange;
import com.model.CoinPerUser;

@Controller
@RequestMapping("/coinPerUserConfig")
public class CoinPerUserConfigController extends PrincipalController {
	
	@Autowired
	private CoinPerUserMapper coinPerUserservice;
	@Autowired
	private CoinByExchangeMapper coinByExchangeMapper;
	@Autowired
	private UserInfoMapper userInfoMapper;
	
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
	public CoinPerUserConfigResponse add(@Valid @ModelAttribute("registroEditado") CoinPerUser registroEditado, BindingResult bindingResult) {
		
		CoinPerUserConfigResponse response = new CoinPerUserConfigResponse();
		
		if (bindingResult.hasErrors()) {
	         Map<String, String> errors = bindingResult.getFieldErrors().stream()
	               .collect(
	                     Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
	                 );
	         
	         response.setValidated(false);
	         response.setErrorMessages(errors);
		} else {

			CoinByExchange coinByExchange = coinByExchangeMapper.selectByCoinExchange(
					registroEditado.getCoinByExchange().getCoin().getId(),
					registroEditado.getCoinByExchange().getExchange().getId());

			registroEditado.setCoinByExchange(coinByExchange);
			registroEditado.setUserInfo(userInfoMapper.selectByLogin(getUserName()));
			coinPerUserservice.insertSelective(registroEditado);
			response.setValidated(Boolean.TRUE);
			response.setCoinPerUser(new CoinPerUser());

		}
	    
	    return response;
		
	}
	
	@RequestMapping(value="/removeValue", method=RequestMethod.POST)
	@ResponseBody
	public void remove(@RequestParam("coinPerUserID") Integer coinPerUserID) {
	    // TODO añadir seguridad

	    coinPerUserservice.deleteByPrimaryKey(coinPerUserID);
	}
	

}
