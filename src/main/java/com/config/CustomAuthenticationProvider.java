package com.config;

import java.util.ArrayList;
import java.util.Arrays;

import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.qos.logback.classic.Logger;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	private static final Logger LOG = (Logger) LoggerFactory.getLogger(CustomAuthenticationProvider.class);
	
	@Override
	@Transactional
	public Authentication authenticate(Authentication authentication) {
		String login = authentication.getName();
		String password = authentication.getCredentials().toString();
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Authenticating user: "+login);
		}
		// Here use the user object to only check if the user exists in the database if
		// not null use his login ( principal ) and password
		return new UsernamePasswordAuthenticationToken(login, password, new ArrayList<GrantedAuthority>(Arrays.asList(new SimpleGrantedAuthority("ROLE"))));
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
