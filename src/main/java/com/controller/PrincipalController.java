package com.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class PrincipalController {

	public final String getUserName() {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	     String name = null;
	    if (auth != null) {
	        Object principal = auth.getPrincipal();
	        name = ((String) principal);
	    }
	    
	    return name;
	}
}
