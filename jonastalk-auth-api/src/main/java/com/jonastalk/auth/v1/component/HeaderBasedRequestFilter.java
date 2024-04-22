package com.jonastalk.auth.v1.component;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jonastalk.common.api.ApiRestConrollerAdvice;
import com.jonastalk.common.component.CommonUtilComponent;
import com.jonastalk.common.component.RequestUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * @name HeaderBasedRequestFilter.java
 * @brief HeaderBased Request Validation Filter
 * @process HeaderBased Request Validation Filter
 * @author Jonas Lim
 * @date 2024.01.29
 */
@Slf4j
@Component
public class HeaderBasedRequestFilter extends OncePerRequestFilter {

	@Autowired
    private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private ApiRestConrollerAdvice customExceptionHandler;
	
	@Autowired
	private RequestUtil requestUtil;
	
	@Autowired
	private KeycloakProviderProperties keycloakProviderProperties;
	
	@Autowired
	private CommonUtilComponent commonUtilComponent;
    
    private RestTemplate restTemplate = new RestTemplate();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

    	try {
	        final String jwtToken = requestUtil.obtainAuthorization(request, response, RequestUtil.AuthType.JWT, false, false);
    		requestUtil.validateRequestAuthorization(request, response, RequestUtil.AuthType.JWT);
	        String username = null;
	        String authMethod = null;
	        if (StringUtils.hasText(jwtToken)) {
		        authMethod = requestUtil.obtainAuthenticationMethodFromHeader(request);
	        	authMethod = authMethod != null ? authMethod.toUpperCase() : authMethod; 

	        	// =======================================
	        	// Authentication Method #1> KEYCLOAK
	        	if (RequestUtil.AuthType.KEYCLOAK.name().equals(authMethod)) {
	        		if (keycloakProviderProperties.getAuthorizationUri() != null) {
	        			
	    			   HttpHeaders keycloakRequestHeaders = new org.springframework.http.HttpHeaders();
	    			   keycloakRequestHeaders.set("Content-Type", "application/json");
	    			   keycloakRequestHeaders.set("Authorization", "Bearer " + jwtToken);

	    			   	HttpEntity<String> keycloakRequest = new HttpEntity<String>(keycloakRequestHeaders);
	    				ResponseEntity<String> keycloakResponse = restTemplate.exchange(
	    						keycloakProviderProperties.getAuthorizationUri(),
	    						  HttpMethod.GET,
	    						  keycloakRequest,
	    						  String.class
	    						);
		                if (keycloakResponse.getStatusCode().is2xxSuccessful()) {
		                    String jsonResponse = keycloakResponse.getBody();
		                    try {
		                        ObjectMapper objectMapper = new ObjectMapper();
		                        Map<String, Object> resultMap = objectMapper.readValue(jsonResponse, Map.class);
					            username = resultMap != null && resultMap.get("username") instanceof String ? (String) resultMap.get("username") : null;
		                    } catch (IOException e) {
		                        e.printStackTrace();
		                    }
		                    
		                    if (username != null) {
		                    	// TODO KeyCloak IDToken
//		                    	UserDetails userDetails = this.userDetailsService.buildCustomUserDetails(username, password, role);
//			            		setTemporaryAuthentication(userDetails);
		                    }
		                    
		                } else {
//		                    log.error("Could not propagate logout to Keycloak");
		                }
	        		}
	        	}
	        	// =======================================
	        	// Authentication Method #2> JWT (default)
	        	else {
	    	        UserDetails userDetails = null;
	    	        if ((userDetails = jwtTokenUtil.validateAccessTokenWithRedis(jwtToken)) != null) {
	    	        	requestUtil.setTemporaryAuthentication(userDetails);
	    	        }
	        	}
	        }
	        
	        // Go to the controller
	        chain.doFilter(request, response);

	        // Clear SecurityContext after filtering
	        SecurityContextHolder.clearContext();
	        
    	}catch (Exception e) {
    		if (request != null) {
    			final String requestUrl = requestUtil.getRequestURL(request);
    			final String requestQueryString = requestUtil.getQueryParameter(request, RequestUtil.AuthType.JWT.getQueryStringName());
    			final String aggregation = commonUtilComponent.aggregateString(", ",
    					new String[] {"requestUrl : " + (requestUrl == null ? "" : requestUrl), "requestQueryString : " + (requestQueryString == null ? "" : requestQueryString)});
    			log.error(aggregation);
    		}
    		customExceptionHandler.handleExceptionsFromOncePerRequestFilter(e, response);
    	}
    }
}
