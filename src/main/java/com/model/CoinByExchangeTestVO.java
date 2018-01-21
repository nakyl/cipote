package com.model;

public class CoinByExchangeTestVO {

	private boolean valid;
	private String exchange;
	private String coin;

	public CoinByExchangeTestVO(boolean isValid, String exchange, String coin) {
		super();
		this.valid = isValid;
		this.exchange = exchange;
		this.coin = coin;
	}

	public boolean getValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getExchange() {
		return exchange;
	}

	public void setExchange(String exchange) {
		this.exchange = exchange;
	}

	public String getCoin() {
		return coin;
	}

	public void setCoin(String coin) {
		this.coin = coin;
	}

}