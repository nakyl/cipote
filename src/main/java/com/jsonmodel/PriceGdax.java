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
@JsonPropertyOrder({ "type", "side", "order_id", "reason", "product_id", "price", "remaining_size", "sequence",
		"time" })
public class PriceGdax {

	@JsonProperty("type")
	private String type;
	@JsonProperty("side")
	private String side;
	@JsonProperty("order_id")
	private String orderId;
	@JsonProperty("reason")
	private String reason;
	@JsonProperty("product_id")
	private String productId;
	@JsonProperty("price")
	private String price;
	@JsonProperty("remaining_size")
	private String remainingSize;
	@JsonProperty("sequence")
	private Long sequence;
	@JsonProperty("time")
	private String time;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("side")
	public String getSide() {
		return side;
	}

	@JsonProperty("side")
	public void setSide(String side) {
		this.side = side;
	}

	@JsonProperty("order_id")
	public String getOrderId() {
		return orderId;
	}

	@JsonProperty("order_id")
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@JsonProperty("reason")
	public String getReason() {
		return reason;
	}

	@JsonProperty("reason")
	public void setReason(String reason) {
		this.reason = reason;
	}

	@JsonProperty("product_id")
	public String getProductId() {
		return productId;
	}

	@JsonProperty("product_id")
	public void setProductId(String productId) {
		this.productId = productId;
	}

	@JsonProperty("price")
	public String getPrice() {
		return price;
	}

	@JsonProperty("price")
	public void setPrice(String price) {
		this.price = price;
	}

	@JsonProperty("remaining_size")
	public String getRemainingSize() {
		return remainingSize;
	}

	@JsonProperty("remaining_size")
	public void setRemainingSize(String remainingSize) {
		this.remainingSize = remainingSize;
	}

	@JsonProperty("sequence")
	public Long getSequence() {
		return sequence;
	}

	@JsonProperty("sequence")
	public void setSequence(Long sequence) {
		this.sequence = sequence;
	}

	@JsonProperty("time")
	public String getTime() {
		return time;
	}

	@JsonProperty("time")
	public void setTime(String time) {
		this.time = time;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
