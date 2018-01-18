package com.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CryptoChartData {

		private static final String PATH = "/allCryptoChar";

		@RequestMapping(value = PATH)
		public String error() {
			return "Error handling";
		}

		public String getErrorPath() {
			return PATH;
		}
}
