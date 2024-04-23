package com.jonastalk.auth.v1.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jonastalk.auth.v1.api.field.account.AccountUsernameExistRequest;
import com.jonastalk.auth.v1.api.field.account.AccountUsernameExistResponse;
import com.jonastalk.auth.v1.api.field.token.TokenAccessGenerateRequest;
import com.jonastalk.auth.v1.api.field.token.TokenAccessGenerateResponse;
import com.jonastalk.auth.v1.api.field.token.TokenAccessRefreshRequest;
import com.jonastalk.auth.v1.api.field.token.TokenAccessRefreshResponse;
import com.jonastalk.auth.v1.api.field.token.TokenAccessValidateRequest;
import com.jonastalk.auth.v1.api.field.token.TokenAccessValidateResponse;
import com.jonastalk.auth.v1.component.JwtTokenUtil;
import com.jonastalk.auth.v1.entity.AuthUserAccountListEntity;
import com.jonastalk.common.component.EncryptionComponent;
import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.exception.CustomException;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

@Service
public class AccountService {

    @Autowired
    private AuthenticationManager authenticationManager;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

	@Autowired
	EncryptionComponent encryptionComponent;


	/**
	 * @name generateAccessToken(@Map<String, Object> param)
	 * @brief Generate Access Token
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> generateAccessToken(Map<String, Object> param) throws Exception {
	   	// ----------------------
    	// 1. Get Params
    	// ----------------------
    	final String username = (String) param.get(TokenAccessGenerateRequest.USERNAME.getName());
    	final String password = (String) param.get(TokenAccessGenerateRequest.PASSWORD.getName());
    	
    	// TODO decode password
//    	encryptionComponent.decrypt(password, EncryptionComponent.AlgorithmType.RSA, EncryptionComponent.EncodingType.UTF_8, null);
    	
    	
    	// ----------------------
    	// 2. Biz Logic
    	// ----------------------

		// --------------
		// 2-1) Authenticate
    	authenticate(username, password);

		// --------------
		// 2-2) Issue Tokens
		final UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		final String accessToken = jwtTokenUtil.generateToken(userDetails, JwtTokenUtil.TokenType.ACCESS);
		final String refreshToken = jwtTokenUtil.generateToken(userDetails, JwtTokenUtil.TokenType.REFRESH);
		
		// --------------
		// 2-3) Put Token Info into Redis
		jwtTokenUtil.putAccessTokenRefreshTokenIntoRedis(accessToken, refreshToken, 0, true);
		
		
    	// ----------------------
    	// 3. Response
    	// ----------------------
    	Map<String, Object> responseData = new HashMap<>();
    	responseData.put(TokenAccessGenerateResponse.ACCESS_TOKEN.getName(), accessToken);
    	responseData.put(TokenAccessGenerateResponse.REFRESH_TOKEN.getName(), refreshToken);
		return responseData;
	}


    /**
	 * @name validateAccessToken(@Map<String, Object> param)
	 * @brief Validate Access Token
	 * @author Jonas Lim
	 * @date 2024.02.21
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> validateAccessToken(Map<String, Object> param) throws Exception {
	   	// ----------------------
    	// 1. Get Params
    	// ----------------------
    	final String username = (String) param.get(TokenAccessValidateRequest.USERNAME.getName());
    	final String accessToken = (String) param.get(TokenAccessValidateRequest.ACCESS_TOKEN.getName());
    	
    	
    	// ----------------------
    	// 2. Biz Logic
    	// ----------------------

		// --------------
		// 2-1) Validate
    	boolean validation = false;
        String usernameFromAccessToken = jwtTokenUtil.extractUsername(accessToken);
        if (username.equals(usernameFromAccessToken)) {
        	try {
        		validation = (jwtTokenUtil.validateAccessTokenWithRedis(accessToken) != null);
			} catch (ExpiredJwtException e) {
				throw new CustomException(EnumErrorCode.ERR004);
			} catch (JwtException e) {
				throw new CustomException(EnumErrorCode.ERR005);
			}
        }
		
    	// ----------------------
    	// 3. Response
    	// ----------------------
    	Map<String, Object> response = new HashMap<>();
    	response.put(TokenAccessValidateResponse.VALIDATION.getName(), validation);
		return response;
	}
    
    
    /**
	 * @name refreshAccessToken(Map<String, Object> param)
	 * @brief Generate Refresh Token
	 * @author Jonas Lim
	 * @date 2023.12.01
     * @param request
     * @param param
     * @return
     */
	 public Map<String, Object> refreshAccessToken(Map<String, Object> param) throws Exception {
	    	
	    	// ----------------------
	    	// 1. Get Params
	    	// ----------------------
//	    	final String username = (String) param.get(TokenAccessRefreshRequest.USERNAME.getName());
	    	final String refreshToken = (String) param.get(TokenAccessRefreshRequest.REFRESH_TOKEN.getName());
	    	
	    	
	    	// ----------------------
	    	// 2. Biz Logic
	    	// ----------------------
	    	final String newAccessToken = jwtTokenUtil.refreshAccessToken(refreshToken);
	    	
	    	
	    	// ----------------------
	    	// 2. Biz Logic
	    	// ----------------------
	    	
//	    	// ----------
//	    	// 2-1) Get and check the username from the refresh token
//			String usernameFromToken = null;
//			try {
//				usernameFromToken = jwtTokenUtil.extractUsername(refreshToken);
//				if (!username.equals(usernameFromToken)) {
//					throw new CustomException(EnumErrorCode.ERR005);
//				}
//			} catch (ExpiredJwtException e) {
//				throw new CustomException(EnumErrorCode.ERR004);
//			} catch (JwtException e) {
//				throw new CustomException(EnumErrorCode.ERR005);
//			}
//			
//	    	// ----------
//	    	// 2-2) Check if the user exists
//	        UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
//			if (userDetails == null) {
//	            throw new CustomException(EnumErrorCode.ERR009, username);
//			}
//			
//			// ----------
//			// 2-3) Check access_token and refresh_token in Redis
//			Map<String, String> tokenInfo = jwtTokenUtil.getTokenInfoFromRedisByRefreshToken(refreshToken);
//			if (!(tokenInfo.get(jwtTokenUtil.getRedisKeyRefreshValueRefreshCnt()) instanceof String)
//					|| !(tokenInfo.get(jwtTokenUtil.getRedisKeyRefreshValueAccessToken()) instanceof String)) {
//				throw new CustomException(EnumErrorCode.ERR005);
//			}
//			int refreshCnt = Integer.valueOf(tokenInfo.get(jwtTokenUtil.getRedisKeyRefreshValueRefreshCnt()));
//			if (refreshCnt >= jwtTokenUtil.getRefreshTokenRefreshLimit()) {
//				throw new CustomException(EnumErrorCode.ERR010, "Reached to the refresh limit! Please, athenticate again.");
//			}
//
//	    	// ----------
//			// 2-4) Delete the previous access token in Redis
//			if (!jwtTokenUtil.deleteRefreshTokenFromRedisByAccessToken((String) tokenInfo.get(jwtTokenUtil.getRedisKeyRefreshValueAccessToken()))) {
//				throw new CustomException(EnumErrorCode.ERR001);
//			}
//
//	    	// ----------
//	    	// 2-5) Issue a new access token
//	        String newAccessToken = null;
//			if (jwtTokenUtil.validateToken(refreshToken, userDetails, TokenType.REFRESH)) {
//				newAccessToken = jwtTokenUtil.generateToken(userDetails, JwtTokenUtil.TokenType.ACCESS);
//			}
//
//	    	// ----------
//	    	// 2-6) Update the recent token info in Redis
//			if (!jwtTokenUtil.putAccessTokenRefreshTokenIntoRedis(newAccessToken, refreshToken, ++refreshCnt, false)) {
//				throw new CustomException(EnumErrorCode.ERR001);
//			}
//			
//			
	    	// ----------------------
	    	// 3. Response
	    	// ----------------------
	        if (newAccessToken != null) {
	        	Map<String, Object> response = new HashMap<>();
	        	response.put(TokenAccessRefreshResponse.ACCESS_TOKEN.getName(), newAccessToken);
	        	return response;
	        } else {
				throw new CustomException(EnumErrorCode.ERR005);
			}
		}

    
	/**
	 * @name authenticate(String username, String password) 
	 * @brief Authenticate
	 * @author Jonas Lim
	 * @date 2023.12.01
     * @param username
     * @param password
     * @throws Exception
	 */
    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new CustomException(EnumErrorCode.ERR007);
        } catch (BadCredentialsException e) {
            throw new CustomException(EnumErrorCode.ERR008);
        } catch (UsernameNotFoundException e) {
            throw new CustomException(EnumErrorCode.ERR009, username);
        }
    }
    
	
	/**
	 * @name getExistenceUsername(Map<String, Object> param)
	 * @brief Get Existence Username
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @param param
	 * @return
	 * @throws Exception
	 */
    public Map<String, Object> getExistenceUsername(Map<String, Object> param) throws Exception {
    	
    	// ----------------------
    	// 1. Get Params
    	// ----------------------
    	final String username = (String) param.get(AccountUsernameExistRequest.USERNAME.getName());
    	
    	
    	// ----------------------
    	// 2. Biz Logic
    	// ----------------------
    	if (!StringUtils.hasText(username)) throw new CustomException(EnumErrorCode.ERR002, "`username` is empty!");
    	AuthUserAccountListEntity authUserAccountListEntity = customUserDetailsService.fetchAccountListByUsername(username);
		
		
    	// ----------------------
    	// 3. Response
    	// ----------------------
    	Map<String, Object> response = new HashMap<>();
    	response.put(AccountUsernameExistResponse.USERNAME.getName(), username);
    	response.put(AccountUsernameExistResponse.EXIST.getName(), authUserAccountListEntity != null);
		return response;
    }

}
