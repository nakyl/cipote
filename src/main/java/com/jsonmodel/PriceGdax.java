package com.jsonmodel;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "trade_id", "price", "size", "bid", "ask", "volume", "time" })
public class PriceGdax {

	@JsonProperty("trade_id")
	private Integer tradeId;
	@JsonProperty("price")
	private String price;
	@JsonProperty("size")
	private String size;
	@JsonProperty("bid")
	private String bid;
	@JsonProperty("ask")
	private String ask;
	@JsonProperty("volume")
	private String volume;
	@JsonProperty("time")
	private String time;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("trade_id")
	public Integer getTradeId() {
		return tradeId;
	}

	@JsonProperty("trade_id")
	public void setTradeId(Integer tradeId) {
		this.tradeId = tradeId;
	}

	public PriceGdax withTradeId(Integer tradeId) {
		this.tradeId = tradeId;
		return this;
	}

	@JsonProperty("price")
	public String getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(String price) {
		this.price = price;
	}

	public PriceGdax withPrice(String price) {
		this.price = price;
		return this;
	}

	@JsonProperty("size")
	public String getSize() {
		return size;
	}

	@JsonProperty("size")
	public void setSize(String size) {
		this.size = size;
	}

	public PriceGdax withSize(String size) {
		this.size = size;
		return this;
	}

	@JsonProperty("bid")
	public String getBid() {
		return bid;
	}

	@JsonProperty("bid")
	public void setBid(String bid) {
		this.bid = bid;
	}

	public PriceGdax withBid(String bid) {
		this.bid = bid;
		return this;
	}

	@JsonProperty("ask")
	public String getAsk() {
		return ask;
	}

	@JsonProperty("ask")
	public void setAsk(String ask) {
		this.ask = ask;
	}

	public PriceGdax withAsk(String ask) {
		this.ask = ask;
		return this;
	}

	@JsonProperty("volume")
	public String getVolume() {
		return volume;
	}

	@JsonProperty("volume")
	public void setVolume(String volume) {
		this.volume = volume;
	}

	public PriceGdax withVolume(String volume) {
		this.volume = volume;
		return this;
	}

	@JsonProperty("time")
	public String getTime() {
		return time;
	}

	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}

	public PriceGdax withTime(String time) {
		this.time = time;
		return this;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

	public PriceGdax withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}
}