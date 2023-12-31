package com.apostolis.spms.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.apostolis.spms.service.AppUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Definition of filter that is used in every incoming request to authenticate the user by the JWT token.

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	@Autowired
	AppUserDetailsService userDetailsService;

	@Autowired
	JwtGeneratorValidator jwtgenVal;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authorizationHeader = request.getHeader("Authorization");

		String token = null;
		String userName = null;

		try{

			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				token = authorizationHeader.substring(7);
				userName = jwtgenVal.extractUsername(token);
			}

			// If JWT token is valid authenticate the user and update the SecurityContextHolder object.
			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
	
				if (jwtgenVal.validateToken(token, userDetails)) {

					// Spirng security authentication token using userDetails and JWT token information.
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						jwtgenVal.getAuthenticationToken(token, SecurityContextHolder.getContext().getAuthentication(), userDetails);
					
					// Include details from the request
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}

		}catch(ExpiredJwtException e){
			System.out.println(e.getMessage());
		}

		filterChain.doFilter(request, response);
	}
    
}
