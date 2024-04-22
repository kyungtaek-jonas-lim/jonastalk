package com.jonastalk.auth.v1.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.jonastalk.common.component.RequestUtil;

/**
 * @name HeaderBasedAuthenticationProvider.java
 * @brief Authenticate the user with username and password
 * @process Authentication with Provider that's decided based on Header
 * @author Jonas Lim
 * @date 2024.01.29
 */
@Component
public class HeaderBasedAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    @Autowired
    private KeycloakAuthenticationProvider keycloakAuthenticationProvider;

    @Autowired
    private HttpServletRequest request;
    
    @Autowired
    private RequestUtil requestUtil;
	
	@Lazy
	@Autowired
	JwtTokenUtil jwtTokenUtil;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String authenticationMethod = requestUtil.obtainAuthenticationMethodFromHeader(request);

        AuthenticationProvider selectedProvider = determineProvider(authenticationMethod);

        return selectedProvider.authenticate(authentication);
    }

    private AuthenticationProvider determineProvider(String authenticationMethod) {
    	if (authenticationMethod != null) authenticationMethod = authenticationMethod.toUpperCase();
    	
    	// KEYCLOAK
        if ("KEYCLOAK".equals(authenticationMethod)) {
            return keycloakAuthenticationProvider;
        }
        // JWT Default
        else {
            return jwtAuthenticationProvider;
        }
//        else {
//            throw new AuthenticationServiceException("Unsupported authentication method");
//        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
    
	/**
	 * @brief Get Login Status
	 * @author Jonas Lim
	 * @date 2024.02.10
	 * @return Login Status
	 */
	public boolean isLoggedIn() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null && /*!(authentication instanceof AnonymousAuthenticationToken) &&*/ (authentication instanceof UsernamePasswordAuthenticationToken) && authentication.isAuthenticated();
	}
	
	
	/**
	 * @name logout(HttpServletRequest request, HttpServletResponse response)
	 * @brief Logout by deleting Access Token and Refresh Token
	 * @author Jonas Lim
	 * @date 2024.02.08
	 * @return success
	 */
	public boolean logout(HttpServletRequest request, HttpServletResponse response) {
		String accessToken = requestUtil.obtainAuthorization(request, response, RequestUtil.AuthType.JWT, false, false);
        
		// --------------
		// 1) Remove Access Token in Redis
		boolean result = jwtTokenUtil.deleteAccessTokenAndRefreshTokenFromRedisByAccessToken(accessToken);
		if (result) {
			
			// --------------
			// 2) Remove Cookie
			requestUtil.removeCookieValue(response, RequestUtil.CookieName.ACCESS_TOKEN.getName());
	        
			// --------------
			// 3) Remove Context
			SecurityContextHolder.clearContext();
		}
		return result;
		
	}
}