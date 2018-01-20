package com.form.response;

import java.util.Map;

import com.model.CoinPerUser;

public class CoinPerUserConfigResponse {
	private CoinPerUser coinPerUser;
	private boolean validated;
	private Map<String, String> errorMessages;

	public CoinPerUser getCoinPerUser() {
		return coinPerUser;
	}

	public void setCoinPerUser(CoinPerUser coinPerUser) {
		this.coinPerUser = coinPerUser;
	}

	public boolean isValidated() {
		return validated;
	}

	public void setValidated(boolean validated) {
		this.validated = validated;
	}

	public Map<String, String> getErrorMessages() {
		return errorMessages;
	}

	public void setErrorMessages(Map<String, String> errorMessages) {
		this.errorMessages = errorMessages;
	}

	// Getter and Setter methods
}
