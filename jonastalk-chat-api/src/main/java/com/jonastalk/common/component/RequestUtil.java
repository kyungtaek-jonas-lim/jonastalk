package com.jonastalk.common.component;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

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
