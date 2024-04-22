package com.jonastalk.common.component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jonastalk.auth.v1.component.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @name RequestUtil.java
 * @brief Request Util Component
 * @author Jonas Lim
 * @date 2024.01.29
 */
@Slf4j
@Component
public class RequestUtil {

    /**
	 * @name AuthType
	 * @brief Auth Type
	 * @author Jonas Lim
	 * @date 2024.01.29
     */
	@Getter
	public enum AuthType {
		JWT			("Bearer ", "Bearer ".length(), CookieName.ACCESS_TOKEN.getName(), "access-token")		// json web token - default
		,KEYCLOAK	("Bearer ", "Bearer ".length(), CookieName.ACCESS_TOKEN.getName(), "access-token")		// keycloak
		;
		private String authorizationHeader;
		private int authorizationHeaderSize;
		private String cookieName;
		private String queryStringName;
		private AuthType(String authorizationHeader, int authorizationHeaderSize, String cookieName, String queryStringName) {
			this.authorizationHeader = authorizationHeader;
			this.authorizationHeaderSize = authorizationHeaderSize;
			this.cookieName = cookieName;
			this.queryStringName = queryStringName;
		}
	}

    /**
	 * @name CookieName
	 * @brief Cookie Type
	 * @author Jonas Lim
	 * @date 2024.02.21
     */
	@Getter
	public enum CookieName {
		USERNAME		("username")
		,ACCESS_TOKEN	("access-token")
		,REFRESH_TOKEN	("refresh-token")
		;
		private String name;
		private CookieName(String name) {
			this.name = name;
		}
	}
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

    /**
	 * @name obtainAuthenticationMethodFromHeader(HttpServletRequest request)
	 * @brief Obtain Authentication Method From Header
	 * @author Jonas Lim
	 * @date 2024.01.29
     */
    public String obtainAuthenticationMethodFromHeader(HttpServletRequest request) {
    	if (request != null) return request.getHeader("Authentication-Method");
    	return null;
    }

    /**
	 * @name obtainAuthorizationFromHeader(HttpServletRequest request)
	 * @brief Obtain Authorization From Header
	 * @author Jonas Lim
	 * @date 2024.01.29
     */
    public String obtainAuthorizationFromHeader(HttpServletRequest request) {
    	if (request != null) return request.getHeader("Authorization");
    	return null;
    }
    
    /**
	 * @name obtainAuthorizationFromHeader(HttpServletRequest request, RequestUtil.AuthType authType)
	 * @brief Obtain Access Token From Header
	 * @author Jonas Lim
	 * @date 2024.01.29
     */
    public String obtainAuthorizationFromHeader(HttpServletRequest request, RequestUtil.AuthType authType) {
    	if (request != null && authType != null) {
    		String authorization = obtainAuthorizationFromHeader(request);
    		if (authorization != null && authorization.length() > authType.authorizationHeaderSize) {
    			return authorization.substring(authType.authorizationHeaderSize);
    		}
    	}
    	return null;
    }
    
    /**
	 * @name obtainAuthorizationFromCookie(HttpServletRequest request, RequestUtil.AuthType authType)
	 * @brief Obtain Access Token From Cookie
	 * @author Jonas Lim
	 * @date 2024.02.12
     */
    public String obtainAuthorizationFromCookie(HttpServletRequest request, RequestUtil.AuthType authType) {
    	
        // -------------------------
    	// Validation
    	if (authType == null) return null;
    	return getCookieValue(request, authType.getCookieName());

//        // -------------------------
//    	// Validation
//    	if (request == null || authType == null) return null;
//        Cookie[] cookies = request.getCookies();
//        if (cookies == null) return null;
//        
//        // -------------------------
//        // Get Cookie
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if (cookie.getName().equals(authType.getCookieName())) {
//                    String accessToken = cookie.getValue();
//                    return accessToken;
//                }
//            }
//        }
//        return null;
    }
    
    /**
	 * @name getCookieValue(HttpServletRequest request, RequestUtil.AuthType authType)
	 * @brief Get Cookie Value
	 * @author Jonas Lim
	 * @date 2024.02.22
     */
    public String getCookieValue(HttpServletRequest request, String name) {

        // -------------------------
    	// Validation
    	if (request == null || !StringUtils.hasText(name)) return null;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        
        // -------------------------
        // Get Cookie
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
    
    /**
	 * @name switchCurrentCookieValueInRequest(HttpServletRequest request, String name, String value)
	 * @brief Switch the Current Cookie Value in the Request Object
	 * @author Jonas Lim
	 * @date 2024.02.24
     */
    public void switchCurrentCookieValueInRequest(HttpServletRequest request, String name, String value) { 

	    // -------------------------
		// Validation
		if (request == null || !StringUtils.hasText(name)) return;
	    Cookie[] cookies = request.getCookies();
	    if (cookies == null) return;
	    
	    // -------------------------
	    // Get Cookie
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	            if (cookie.getName().equals(name)) {
	            	cookie.setValue(value);
	            }
	        }
	    }
    }
    
    /**
     * @name switchCurrentAccessTokenInRequest(HttpServletRequest request, String name, String value)
     * @brief Switch the Current Access Token in the Request Object
     * @author Jonas Lim
     * @date 2024.02.24
     */
    public void switchCurrentAccessTokenInRequest(HttpServletRequest request, String newAccessToken) { 
		switchCurrentCookieValueInRequest(request, CookieName.ACCESS_TOKEN.getName(), newAccessToken);
    }
    
    /**
	 * @name setCookieValue(HttpServletRequest request, String name, String value)
	 * @brief Set Cookie Value
	 * @author Jonas Lim
	 * @date 2024.02.22
     */
    public void setCookieValue(HttpServletResponse response, String name, String value, int maxAge, String path) {
    	if (response == null) return;
    	if (!StringUtils.hasText(name) || value == null || !StringUtils.hasText(path)) return;
    	if (maxAge < 0) maxAge = 0;
        Cookie cookieAccessToken = new Cookie(name, value);
        cookieAccessToken.setMaxAge(maxAge);
        cookieAccessToken.setPath(path);
        response.addCookie(cookieAccessToken);
    }
    
    /**
	 * @name removeCookieValue(HttpServletResponse response, String name)
	 * @brief Remove Cookie Value
	 * @author Jonas Lim
	 * @date 2024.02.22
     */
    public void removeCookieValue(HttpServletResponse response, String name) {
    	setCookieValue(response, name, "", 0, "/");
    }
    
    /**
	 * @name setCookieValueAccessToken(HttpServletResponse response, String accessToken)
	 * @brief Set Cookie Value
	 * @author Jonas Lim
	 * @date 2024.02.22
     */
    public void setCookieValueAccessToken(HttpServletResponse response, String accessToken) {
		setCookieValue(response, RequestUtil.CookieName.ACCESS_TOKEN.getName(), accessToken, 3600, "/"); // TODO maxAge
    }
    
    /**
	 * @name setCookieValueRefreshToken(HttpServletResponse response, String refreshToken)
	 * @brief Set Cookie Value
	 * @author Jonas Lim
	 * @date 2024.02.22
     */
    public void setCookieValueRefreshToken(HttpServletResponse response, String refreshToken) {
		setCookieValue(response, RequestUtil.CookieName.REFRESH_TOKEN.getName(), refreshToken, 3600, "/"); // TODO maxAge
    }
    
    /**
	 * @name setCookieValueUsername(HttpServletResponse response, String username)
	 * @brief Set Cookie Value
	 * @author Jonas Lim
	 * @date 2024.02.22
     */
    public void setCookieValueUsername(HttpServletResponse response, String username) {
		setCookieValue(response, RequestUtil.CookieName.USERNAME.getName(), username, 3600, "/"); // TODO maxAge
    }
    
    /**
	 * @name obtainAuthorization(HttpServletRequest request, HttpServletResponse response, RequestUtil.AuthType authType, boolean autoRefreshToken, boolean validate)
	 * @brief Obtain Access Token From Header or Cookie or QueryString (Header first)
	 * @author Jonas Lim
	 * @date 2024.02.14
     */
    public String obtainAuthorization(HttpServletRequest request, HttpServletResponse response, RequestUtil.AuthType authType, boolean autoRefreshToken, boolean validate) {
    	return obtainAuthorization(request, response, authType, autoRefreshToken, validate, false);
    }
    
    /**
	 * @name validateRequestAuthorization(HttpServletRequest request, HttpServletResponse response, RequestUtil.AuthType authType)
	 * @brief Obtain Access Token From Header or Cookie or QueryString (Header first)
	 * @source every request (OncePerRequestFilter)
	 * @author Jonas Lim
	 * @date 2024.02.14
     */
    public String validateRequestAuthorization(HttpServletRequest request, HttpServletResponse response, RequestUtil.AuthType authType) {
    	return obtainAuthorization(request, response, authType, true, true, true);
    }
    
    /**
	 * @name obtainAuthorization(HttpServletRequest request, HttpServletResponse response, RequestUtil.AuthType authType, boolean autoRefreshToken, boolean validate, boolean authTemp)
	 * @brief (Internal) Obtain Access Token From Header or Cookie or QueryString (Header first)
	 * @author Jonas Lim
	 * @date 2024.02.14
     */
    private String obtainAuthorization(HttpServletRequest request, HttpServletResponse response, RequestUtil.AuthType authType, boolean autoRefreshToken, boolean validate, boolean authTemp) {
		// #1> Authorization Header
        String jwtTokenTemp = obtainAuthorizationFromHeader(request, authType);
        
        // #2> Cookie
        if (!StringUtils.hasText(jwtTokenTemp)) {
        	jwtTokenTemp = obtainAuthorizationFromCookie(request, authType);
        	if (autoRefreshToken) {
	        	refreshAccessTokenAndPutCookie(request, response, jwtTokenTemp, validate, authTemp); // check the expiration date and refresh access token 
	        	// TODO now only cookie method can automatically reissue access token with the fresh token -> Header, Query String later!
        	}
        }
        
        // #3> Get Query String (Resources)
        if (!StringUtils.hasText(jwtTokenTemp)) {
        	jwtTokenTemp = getQueryParameter(request, authType != null ? authType.getQueryStringName() : null);
        	if (autoRefreshToken) {
	        	refreshAccessTokenAndPutCookie(request, response, jwtTokenTemp, validate, authTemp); // check the expiration date and refresh access token 
	        	// TODO now only cookie method can automatically reissue access token with the fresh token -> Header, Query String later!
        	}
        }
        
        return jwtTokenTemp;
    }
    
    /**
	 * @name refreshAccessTokenAndPutCookie(HttpServletRequest request, HttpServletResponse response, String jwtToken, boolean validate)
	 * @brief (Internal) refresh Access Token And Put it in Cookie
	 * @author Jonas Lim
	 * @date 2024.02.14
     */
    private void refreshAccessTokenAndPutCookie(HttpServletRequest request, HttpServletResponse response, String jwtToken, boolean validate, boolean authTemp) {
    	if (request == null || !StringUtils.hasText(jwtToken)) return;
    	
        if (StringUtils.hasText(jwtToken)) { 
        	boolean isExpired = false;
        	try {
        		jwtTokenUtil.extractUsername(jwtToken); // If it's an old access token that is not expired
        		if (validate) {
        			UserDetails userDetails = jwtTokenUtil.validateAccessTokenWithRedis(jwtToken);
        			if (userDetails == null) {
        				isExpired = true;
        			} else {
	        			if (authTemp) {
	        				setTemporaryAuthentication(userDetails);
	        			}
        			}
        		}
			} catch (ExpiredJwtException e) {
				isExpired = true;
			}
        	
        	if (isExpired) {
        		String newAccessToken = null;
            	final String refreshToken = getCookieValue(request, CookieName.REFRESH_TOKEN.getName());
            	if (StringUtils.hasText(refreshToken)) {
            		// refresh token
            		newAccessToken = jwtTokenUtil.refreshAccessToken(refreshToken);
            		// swtich current cookie in the request object
            		switchCurrentAccessTokenInRequest(request, newAccessToken);
            		// response set cookie with the new access token
            		setCookieValueAccessToken(response, newAccessToken);
            	}
        		if (StringUtils.hasText(newAccessToken) && validate) {
        			UserDetails userDetails = jwtTokenUtil.validateAccessTokenWithRedis(newAccessToken);
        			if (userDetails != null) {
	        			if (authTemp) {
	        				setTemporaryAuthentication(userDetails);
	        			}
        			}
        		}
        	}
        }
    }
    
    /**
	 * @name setTemporaryAuthentication(UserDetails userDetails)
	 * @brief (Internal) Set Temporary Authentication
	 * @author Jonas Lim
	 * @date 2024.02.14
     */
    public void setTemporaryAuthentication(UserDetails userDetails) {
    	
    	if (userDetails == null) return;
    	
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        // Create and set SecurityContext
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(usernamePasswordAuthenticationToken);
        
        // Set SecurityContext in the current thread
        SecurityContextHolder.setContext(securityContext);
    }
    
    /**
	 * @name getRequestURL(HttpServletRequest request)
	 * @brief Get URL from HttpServletRequest
	 * @author Jonas Lim
	 * @date 2024.02.20
     */
    public String getRequestURL(HttpServletRequest request) {
    	if (request == null || request.getRequestURL() == null) return null;
        return request.getRequestURL().toString();
    }

    /**
	 * @name getQueryParameter(final HttpServletRequest request, final String key)
	 * @brief Get a query parameter
	 * @author Jonas Lim
	 * @date 2024.02.20
     */
    public String getQueryParameter(final HttpServletRequest request, final String key) {
        Map<String, Object> queryStrings = getQueryParametersMap(request);
        if (queryStrings != null
        		&& StringUtils.hasText(key)
        		&& queryStrings.get(key) instanceof String) {
        	return (String) queryStrings.get(key);
        }
        return null;
    }
    
    /**
	 * @name getQueryParametersMap(HttpServletRequest request)
	 * @brief Get query parameters as Map<String, Object>
	 * @author Jonas Lim
	 * @date 2024.02.20
     */
    public Map<String, Object> getQueryParametersMap(HttpServletRequest request) {
    	if (request == null) return null;
        String queryString = request.getQueryString();
        if (queryString != null && !queryString.isEmpty()) {
            return parseQueryString(queryString);
        }
        return null;
    }
    
    /**
	 * @name parseQueryString(String queryString)
	 * @brief Parse query string into Map<String, Object>
	 * @author Jonas Lim
	 * @date 2024.02.20
     */
    private Map<String, Object> parseQueryString(String queryString) {
        // Parse query string into Map<String, Object>
        Map<String, Object> queryParams = null;
        String[] keyValuePairs = queryString.split("&");
        if (keyValuePairs != null && keyValuePairs.length > 0) {
        	queryParams = new HashMap<>();
	        for (String pair : keyValuePairs) {
	        	if (pair == null) continue;
	            String[] keyValue = pair.split("=");
	        	if (keyValue == null || keyValue.length < 2) continue;
	            String key = keyValue[0];
	            String value = keyValue[1];
	            queryParams.put(key, value);
	        }
        }
        return queryParams;
    }
}
