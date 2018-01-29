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
@JsonPropertyOrder({ "success", "message", "result" })
public class PriceBittrex {

	@JsonProperty("success")
	private Boolean success;
	@JsonProperty("message")
	private String message;
	@JsonProperty("result")
	private ResultBittrex result;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<>();

	@JsonProperty("success")
	public Boolean getSuccess() {
		return success;
	}

	@JsonProperty("success")
	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public PriceBittrex withSuccess(Boolean success) {
		this.success = success;
		return this;
	}

	@JsonProperty("message")
	public String getMessage() {
		return message;
	}

	@JsonProperty("message")
	public void setMessage(String message) {
		this.message = message;
	}

	public PriceBittrex withMessage(String message) {
		this.message = message;
		return this;
	}

	@JsonProperty("result")
	public ResultBittrex getResult() {
		return result;
	}

	@JsonProperty("result")
	public void setResult(ResultBittrex result) {
		this.result = result;
	}

	public PriceBittrex withResult(ResultBittrex result) {
		this.result = result;
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

	public PriceBittrex withAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
		return this;
	}

}
