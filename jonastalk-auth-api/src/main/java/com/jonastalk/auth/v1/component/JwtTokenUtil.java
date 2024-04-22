package com.jonastalk.auth.v1.component;

/**
 * @name JwtTokenUtil.java
 * @brief JWT Token Util Component
 * @author Jonas Lim
 * @date 2023.11.25
 */
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jonastalk.auth.v1.service.CustomUserDetailsService;
import com.jonastalk.common.component.CommonUtilComponent;
import com.jonastalk.common.component.EncryptionComponent;
import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.exception.CustomException;
import com.jonastalk.common.service.RedissonService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @name JwtTokenUtil.java
 * @brief JWT Token Util Component
 * @author Jonas Lim
 * @date 2023.11.25
 */
@Slf4j
@Component
public class JwtTokenUtil {
	
	public enum TokenType {
	    ACCESS,
	    REFRESH
	    ;
	}
	
	@Getter
	enum ClaimParams {
		TokenType	("tokenType")
		;
		private String value;
		private ClaimParams(String value) {
			this.value = value;
		}
	}
	
	@Lazy
	@Autowired
	EncryptionComponent encryptionComponent;
	
	@Lazy
	@Autowired
	CommonUtilComponent commonUtilComponent;
	
	@Autowired
	CustomUserDetailsService customUserDetailsService;
	
	@Lazy
	@Autowired
	RedissonService redissonService;

    @Value("${jwt.secret}")
    private String secret;	// only if RSA key doesn't exist

    @Value("${jwt.access-token.ttl-mins}")
    private Long accessTokenTtlMins;

    @Value("${jwt.refresh-token.ttl-mins}")
    private Long refreshTokenTtlMins;

    @Getter
    @Value("${jwt.refresh-token.refresh-limit}")
    private Long refreshTokenRefreshLimit;

    @Value("${jwt.redis.prefix.common}")
    private String redisPrefixCommon;
    
    @Value("${jwt.redis.prefix.access-token}")
    private String redisPrefixAccessToken;
    
    @Value("${jwt.redis.prefix.refresh-token}")
    private String redisPrefixRefreshToken;

    @Getter
    @Value("${jwt.redis.key.refresh.value.access-token}")
    private String redisKeyRefreshValueAccessToken;

    @Getter
    @Value("${jwt.redis.key.refresh.value.refresh-cnt}")
    private String redisKeyRefreshValueRefreshCnt;

	/**
	 * @name generateToken(UserDetails userDetails, TokenType tokenType)
	 * @brief Generate Token
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    public String generateToken(UserDetails userDetails, TokenType tokenType) {
    	if (userDetails == null || tokenType == null) return null;
        Map<String, Object> claims = new HashMap<>();
        claims.put(ClaimParams.TokenType.getValue(), tokenType.name()); // Indicate it's an access token
        return createToken(claims, userDetails.getUsername(), userDetails.getUsername(), tokenType);
    }

	/**
	 * @name createToken(Map<String, Object> claims, String username, String subject, TokenType tokenType)
	 * @brief Create Token (internally)
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    private String createToken(Map<String, Object> claims, String username, String subject, TokenType tokenType) {
    	
    	String token = null;
    	Date issueDate = commonUtilComponent.generateDate(Calendar.MINUTE, 0);
    	Date expirationDate = null;
    	if (TokenType.ACCESS.equals(tokenType)) {
    		expirationDate = commonUtilComponent.generateDate(Calendar.MINUTE, accessTokenTtlMins.intValue());
    	} else if (TokenType.REFRESH.equals(tokenType)) {
    		expirationDate = commonUtilComponent.generateDate(Calendar.MINUTE, refreshTokenTtlMins.intValue());
    	} else {
    		expirationDate = commonUtilComponent.generateDate(Calendar.MINUTE, accessTokenTtlMins.intValue());
    	}
    	
    	if (encryptionComponent.isExistsRsaKey(EncryptionComponent.RsaPurpose.JWT)) {
            PrivateKey privateKey = encryptionComponent.getRsaPrivateKey(EncryptionComponent.RsaPurpose.JWT);
            token = Jwts.builder().setClaims(claims).setSubject(subject)
                    .setIssuedAt(issueDate)
                    .setExpiration(expirationDate)
                    .signWith(SignatureAlgorithm.RS256, privateKey)
                    .compact();
    	} else {
	    	token = Jwts.builder().setClaims(claims).setSubject(subject)
	                .setIssuedAt(issueDate)
	                .setExpiration(expirationDate)
	                .signWith(SignatureAlgorithm.HS512, secret).compact();
    	}
    	log.debug("Created Token - username : {}, expiration : {}", username, expirationDate.toString());
    	return token;
    }

	/**
	 * @name validateToken(String token, UserDetails userDetails, TokenType expectedTokenType)
	 * @brief Validate Token
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    public Boolean validateToken(String token, UserDetails userDetails, TokenType expectedTokenType) {
    	if (!StringUtils.hasText(token) || userDetails == null || expectedTokenType == null) return false;
        final String username = extractUsername(token);
        if (!username.equals(userDetails.getUsername())) return false;
        try {
        	Claims claims = null;
        	String tokenType = null;
            if (encryptionComponent.isExistsRsaKey(EncryptionComponent.RsaPurpose.JWT)) {
                PublicKey publicKey = encryptionComponent.getRsaPublicKey(EncryptionComponent.RsaPurpose.JWT);
                claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
                tokenType = (String) claims.get("tokenType");
            } else {
            	claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                tokenType = (String) claims.get("tokenType");
            }
            // Validate the token type
            if (tokenType != null && tokenType.equals(expectedTokenType.name())) {
                return !isTokenExpired(token);
            }
        } catch (Exception e) {
            // Handle exception or return false based on your implementation
        }
        return false;
    }

	/**
	 * @name extractUsername(String token)
	 * @brief Extract username from the token
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

	/**
	 * @name extractExpiration(String token)
	 * @brief Extract expiration from the token
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

	/**
	 * @name extractClaim(String token, Function<Claims, T> claimsResolver)
	 * @brief Extract Claim from the token
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

	/**
	 * @name extractAllClaims(String token)
	 * @brief Extract All Claims from the token
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    private Claims extractAllClaims(String token) {
    	if (encryptionComponent.isExistsRsaKey(EncryptionComponent.RsaPurpose.JWT)) {
	        PublicKey publicKey = encryptionComponent.getRsaPublicKey(EncryptionComponent.RsaPurpose.JWT);
	        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token).getBody();
    	}
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

	/**
	 * @name isTokenExpired(String token)
	 * @brief See if the token is expired
	 * @author Jonas Lim
	 * @date 2023.11.25
	 */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

	/**
	 * @name putAccessTokenRefreshTokenIntoRedis(String accessToken, String refreshToken, int refreshCnt, boolean updateTtl)
	 * @brief Put Access Token and Refresh Token into Redis after issuing them (Scenario #1 - Issue Token API)
	 * @author Jonas Lim
	 * @date 2024.02.03
	 */
    public boolean putAccessTokenRefreshTokenIntoRedis(String accessToken, String refreshToken, int refreshCnt, boolean updateTtl) {
    	if (!StringUtils.hasText(accessToken) || !StringUtils.hasText(refreshToken)) return false;
    	
    	String refreshTokenKey = redisPrefixCommon + redisPrefixRefreshToken + refreshToken;
    	String accessTokenKey = redisPrefixCommon + redisPrefixAccessToken + accessToken;

    	Map<String, String> refreshTokenValue = new HashMap<>();
    	refreshTokenValue.put(this.getRedisKeyRefreshValueAccessToken(), accessToken);
    	refreshTokenValue.put(this.getRedisKeyRefreshValueRefreshCnt(), String.valueOf(refreshCnt));
    	
    	boolean result = true;
    	result = result && redissonService.putMapWithLock(refreshTokenKey, refreshTokenValue, updateTtl ? refreshTokenTtlMins * 60 : null);
    	result = result && redissonService.putDataWithLock(accessTokenKey, refreshToken, accessTokenTtlMins * 60);
		return result;
    }

	/**
	 * @name getTokenInfoFromRedisByRefreshToken(String refreshToken)
	 * @brief Get Token Info by RefreshToken (Scenario #2 - Refresh Token API)
	 * @author Jonas Lim
	 * @date 2024.02.03
	 */
    public Map<String, String> getTokenInfoFromRedisByRefreshToken(String refreshToken) {
    	if (!StringUtils.hasText(refreshToken)) return null;
    	String collectionName = redisPrefixCommon + redisPrefixRefreshToken + refreshToken;
		return redissonService.getMapWithLock(collectionName);
    }

	/**
	 * @name getAccessTokenFromRedisByRefreshToken(String refreshToken)
	 * @brief Get Acess Token by RefreshToken (Scenario #3 - Authorization (OncePerRequestFilter))
	 * @author Jonas Lim
	 * @date 2024.02.03
	 */
    public String getAccessTokenFromRedisByRefreshToken(String refreshToken) {
    	if (!StringUtils.hasText(refreshToken)) return null;
    	String collectionName = redisPrefixCommon + redisPrefixRefreshToken + refreshToken;
		return redissonService.getDataFromMapWithLock(collectionName, this.getRedisKeyRefreshValueAccessToken());
    }

	/**
	 * @name getRefreshTokenFromRedisByAccessToken(String accessToken)
	 * @brief Get RefreshToken by AccessToken (Scenario #3 - Authorization (OncePerRequestFilter))
	 * @author Jonas Lim
	 * @date 2024.02.03
	 */
    public String getRefreshTokenFromRedisByAccessToken(String accessToken) {
    	if (!StringUtils.hasText(accessToken)) return null;
    	String key = redisPrefixCommon + redisPrefixAccessToken + accessToken;
		return redissonService.getDataWithLock(key);
    }

	/**
	 * @name deleteRefreshTokenFromRedisByAccessToken(String accessToken)
	 * @brief Delete Refresh Token From Redis (Scenario #2 - Refresh Token API, Scenario #4 - Logout)
	 * @author Jonas Lim
	 * @date 2024.02.03
	 */
    public boolean deleteRefreshTokenFromRedisByAccessToken(String accessToken) {
    	if (!StringUtils.hasText(accessToken)) return false;
    	String key = redisPrefixCommon + redisPrefixAccessToken + accessToken;
		return redissonService.deleteData(key);
    }

	/**
	 * @name deleteAccessTokenFromRedisByRefreshToken(String refreshToken)
	 * @brief Delete Access Token From Redis (Scenario #4 - Logout)
	 * @author Jonas Lim
	 * @date 2024.02.07
	 */
    public boolean deleteAccessTokenFromRedisByRefreshToken(String refreshToken) {
    	if (!StringUtils.hasText(refreshToken)) return false;
    	String collectionName = redisPrefixCommon + redisPrefixRefreshToken + refreshToken;
		return redissonService.removeMap(collectionName);
    }

	/**
	 * @name deleteRefreshTokenFromRedisByAccessToken(String accessToken
	 * @brief Delete Both Access Token And Refresh Token From Redis (Scenario #4 - Logout)
	 * @author Jonas Lim
	 * @date 2024.02.07
	 */
    public boolean deleteAccessTokenAndRefreshTokenFromRedisByAccessToken(String accessToken) {
    	if (!StringUtils.hasText(accessToken)) return false;
    	String refreshToken = getRefreshTokenFromRedisByAccessToken(accessToken);
    	
    	boolean result = true;
    	result &= deleteAccessTokenFromRedisByRefreshToken(refreshToken);
    	if (result) {
    		result &= deleteRefreshTokenFromRedisByAccessToken(accessToken);
    	}
    	return result;
    }

	/**
	 * @name validateAccessTokenWithRedis(final String jwtToken)
	 * @brief Validate Access Token With Redis (Authorization)
	 * @author Jonas Lim
	 * @date 2024.02.21
	 */
    public UserDetails validateAccessTokenWithRedis(final String jwtToken) {
        if (StringUtils.hasText(jwtToken)) {
            String username = extractUsername(jwtToken);
            if (username != null) {
	            UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
            	
	            if (validateToken(jwtToken, userDetails, JwtTokenUtil.TokenType.ACCESS)) {
            	
	            	// ---------------------------------
		            // Check if Redis has it (Session Check)
	            	// ---------------------------------
	            	
	            	// -----------------
		            // Get its refresh token
		            String refreshToken = getRefreshTokenFromRedisByAccessToken(jwtToken);
		            if (StringUtils.hasText(refreshToken)) {

		            	// -----------------
			            // Check if it's the latest access Token
		            	String accessToken = getAccessTokenFromRedisByRefreshToken(refreshToken);
		            	if (jwtToken.equals(accessToken)) {
		            		return userDetails;
		            	}
		            }
		            
	            }
            }
        }
        return null;
    }

	/**
	 * @name refreshAccessToken(String refreshToken)
	 * @brief Refresh Access Token
	 * @author Jonas Lim
	 * @date 2024.02.22
	 */
	public String refreshAccessToken(String refreshToken) {
    	// ----------
    	// 2-1) Get and check the username from the refresh token
		String usernameFromToken = null;
		try {
			usernameFromToken = extractUsername(refreshToken);
		} catch (ExpiredJwtException e) {
			throw new CustomException(EnumErrorCode.ERR004);
		} catch (JwtException e) {
			throw new CustomException(EnumErrorCode.ERR005);
		}
		if (!StringUtils.hasText(usernameFromToken)) {
			throw new CustomException(EnumErrorCode.ERR009, usernameFromToken);
		}
		
		// ----------
		// 2-2) Check if the user exists
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(usernameFromToken);
		if (userDetails == null) {
			throw new CustomException(EnumErrorCode.ERR009, usernameFromToken);
		}
		
		// ----------
		// 2-3) Check access_token and refresh_token in Redis
		Map<String, String> tokenInfo = getTokenInfoFromRedisByRefreshToken(refreshToken);
		if (!(tokenInfo.get(getRedisKeyRefreshValueRefreshCnt()) instanceof String)
				|| !(tokenInfo.get(getRedisKeyRefreshValueAccessToken()) instanceof String)) {
			throw new CustomException(EnumErrorCode.ERR005);
		}
		int refreshCnt = Integer.valueOf(tokenInfo.get(getRedisKeyRefreshValueRefreshCnt()));
		if (refreshCnt >= getRefreshTokenRefreshLimit()) {
			throw new CustomException(EnumErrorCode.ERR010, "Reached to the refresh limit! Please, athenticate again.");
		}

    	// ----------
		// 2-4) Delete the previous access token in Redis
		if (!deleteRefreshTokenFromRedisByAccessToken((String) tokenInfo.get(getRedisKeyRefreshValueAccessToken()))) {
			throw new CustomException(EnumErrorCode.ERR001);
		}

    	// ----------
    	// 2-5) Issue a new access token
        String newAccessToken = null;
		if (validateToken(refreshToken, userDetails, TokenType.REFRESH)) {
			newAccessToken = generateToken(userDetails, JwtTokenUtil.TokenType.ACCESS);
		}

    	// ----------
    	// 2-6) Update the recent token info in Redis
		if (!putAccessTokenRefreshTokenIntoRedis(newAccessToken, refreshToken, ++refreshCnt, false)) {
			throw new CustomException(EnumErrorCode.ERR001);
		}
		return newAccessToken;
    }
}
