package com.jonastalk.common.component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Getter;

/**
 * @name EncryptionComponent.java
 * @brief Encryption Component
 * @author Jonas Lim
 * @date 2023.10.20
 */
@Component
public class EncryptionComponent {
	
	private static final String DEFAULT_SALT		= "JONASTALK";
	private static final String TRANSFORMATION		= "AES/CBC/PKCS5Padding";
    private static final String KEY					= "comprojecttestaes256query";
    private static final String IV					= KEY.substring(0, 16);

	private final Map<RsaPurpose, Map<RsaKeyType, Key>> rsaKey = new HashMap<>();

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    
    @Autowired
    @Lazy
    CommonUtilComponent utilComponent;

    @Autowired
    RsaConfigurationProperties rsaConfigurationProperties;
    
    @PostConstruct
    public void init() {
    	for (RsaPurpose rsaPurpose : RsaPurpose.values()) {
        	if (!readRsaKey(rsaPurpose)) {
        		generateRsaKey(rsaPurpose);
        	}
    	}
    }
    
    /**
     * @brief Encyption Encoding Type
     * @author Jonas Lim
     * @date 2023.10.20
     */
    @Getter
    public enum EncodingType {
    	UTF_8		("UTF-8")
    	;
    	private String value;
    	private EncodingType(String value) {
    		this.value = value;
    	}
    }
    
    /**
     * @brief Encyption Algorithm Type
     * @author Jonas Lim
     * @date 2023.10.20
     * @algorithemList SecretKeySpec - Blow
     */
    @Getter
    public enum AlgorithmType {
    	AES						("AES",					false)
    	,SHA256					("SHA-256",				false)
    	,SHA256_SALT			("SHA-256",				true)
    	,B_CRYPT_PASSWORD		("BCryptPassword",		false)
    	,B_CRYPT_PASSWORD_SALT	("BCryptPassword",		true)
    	;
    	private String value;
    	private boolean salt;
    	private AlgorithmType(String value, boolean salt) {
    		this.value = value;
    		this.salt = salt;
    	}
    }
    
    /**
     * @brief Rsa Action 
     * @author Jonas Lim
     * @date 2023.12.01
     */
    @Getter
    public enum RsaAction {
    	GENERATE	("generate")
    	,READ		("read")
    	;
    	private String value;
    	private RsaAction(String value) {
    		this.value = value;
    	}
    }
    
    /**
     * @brief Rsa Purpose
     * @author Jonas Lim
     * @date 2023.12.01
     */
    @Getter
    public enum RsaPurpose {
    	JWT			("jwt")
    	,PASSWORD	("password")
    	;
    	private String value;
    	private RsaPurpose(String value) {
    		this.value = value;
    	}
    	private static Map<String, RsaPurpose> map;
    	public static RsaPurpose getKey(String value) {
    		if (map == null) {
    			map = new HashMap<>();
    			for (RsaPurpose rsaAction : RsaPurpose.values()) {
    				map.put(rsaAction.getValue(), rsaAction);
    			}
    		}
    		return map.get(value);
    	}
    	private static String[] stringArray;
    	public static String[] getStringArray() {
    		if (stringArray != null) return stringArray;
    		List<String> list = new ArrayList<>();
    		for (RsaPurpose rsaPurpose : RsaPurpose.values()) {
    			list.add(rsaPurpose.getValue());
    		}
    		stringArray = list.toArray(new String[list.size()]);
    		return stringArray;
    	}
    }
    
    /**
     * @brief Rsa Key Type
     * @author Jonas Lim
     * @date 2023.12.01
     */
    @Getter
    public enum RsaKeyType {
    	PUBLIC_KEY		("publicKey")
    	,PRIVATE_KEY	("privateKey")
    	;
    	private String value;
    	private RsaKeyType(String value) {
    		this.value = value;
    	}
    }

    /**
     * @brief Encryption Function
     * @author Jonas Lim
     * @date 2023.10.20
     * @param text
     * @param algorithmType
     * @param encodingType
     * @return
     * @throws Exception
     */
    public String encrypt(String text, AlgorithmType algorithmType, EncodingType encodingType, String salt) throws Exception {
    	
    	// ------------------
    	// 1. Validation
    	if (text == null || algorithmType == null) return null;
    	if (algorithmType.isSalt() && !StringUtils.hasText(salt)) salt = EncryptionComponent.DEFAULT_SALT;
    	if (encodingType == null) encodingType = EncodingType.UTF_8;
    	if (algorithmType.isSalt()) {
    		text = salt + text;
    	}
    	String result = null;
    	
    	// ------------------
    	// AES
    	if (algorithmType.equals(AlgorithmType.AES)) {
	        Cipher cipher 					= Cipher.getInstance(TRANSFORMATION);
	        SecretKeySpec secretKeySpec 	= new SecretKeySpec(IV.getBytes(), algorithmType.getValue());
	        IvParameterSpec ivParamSpec 	= new IvParameterSpec(IV.getBytes());
	        
	        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParamSpec);
	
	        byte[] encrypted 				= cipher.doFinal(text.getBytes(encodingType.getValue()));
	        result = Base64.getEncoder().encodeToString(encrypted);
    	}
    	// ------------------
    	// SHA256 / SHA256 SALT
    	else if (algorithmType.equals(AlgorithmType.SHA256)
    			|| algorithmType.equals(AlgorithmType.SHA256_SALT)) {
    		MessageDigest digest = MessageDigest.getInstance(algorithmType.getValue());
    		byte[] encodedhash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
    		result = utilComponent.bytesToHex(encodedhash);
    	}
    	// ------------------
    	// B_CRYPT_PASSWORD / B_CRYPT_PASSWORD_SALT SALT
    	else if (algorithmType.equals(AlgorithmType.B_CRYPT_PASSWORD)
    			|| algorithmType.equals(AlgorithmType.B_CRYPT_PASSWORD_SALT)) {
    		result = bCryptPasswordEncoder.encode(text);
    	}
    	
    	return result;
    }

    /**
     * @brief Decryption Function
     * @author Jonas Lim
     * @date 2023.10.20
     * @param encryptedText
     * @param algorithmType
     * @param encodingType
     * @return
     * @throws Exception
     */
    public String decrypt(String encryptedText, AlgorithmType algorithmType, EncodingType encodingType, String salt) throws Exception {
    	
    	// ------------------
    	// 1.Validation
    	if (encryptedText == null || algorithmType == null) return null;
    	if (algorithmType.isSalt() && !StringUtils.hasText(salt)) salt = EncryptionComponent.DEFAULT_SALT;
    	if (encodingType == null) encodingType = EncodingType.UTF_8;
    	String result = null;
    	
    	// ------------------
    	// AES
    	if (algorithmType.equals(AlgorithmType.AES)) {
	        Cipher cipher					= Cipher.getInstance(TRANSFORMATION);
	        SecretKeySpec secretKeySpec 	= new SecretKeySpec(IV.getBytes(), algorithmType.getValue());
	        IvParameterSpec ivParamSpec 	= new IvParameterSpec(IV.getBytes());
	        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParamSpec);
	
	        byte[] decodedBytes 			= Base64.getDecoder().decode(encryptedText);
	        byte[] decrypted 				= cipher.doFinal(decodedBytes);
	        result = new String(decrypted, 	encodingType.getValue());
	        
    	}
    	if (algorithmType.isSalt() && StringUtils.hasText(result) && result.startsWith(salt)) {
    		result = result.substring(salt.length());
    	}
    	return result;
    }
    
    
    /**
     * @brief Match Function
     * @author Jonas Lim
     * @date 2023.11.27
     * @param text
     * @param encryptedText
     * @param algorithmType
     * @param encodingType
     * @return
     * @throws Exception
     */
    public boolean match(String text, String encryptedText, AlgorithmType algorithmType, EncodingType encodingType, String salt) throws Exception {
    	
    	// ------------------
    	// 1.Validation
    	if (text == null || encryptedText == null || algorithmType == null) return false;
    	if (algorithmType.isSalt() && !StringUtils.hasText(salt)) salt = EncryptionComponent.DEFAULT_SALT;
    	if (encodingType == null) encodingType = EncodingType.UTF_8;
    	boolean result = false;
    	
    	// ------------------
    	// B_CRYPT_PASSWORD
    	if (AlgorithmType.B_CRYPT_PASSWORD.equals(algorithmType)) {
    		result = bCryptPasswordEncoder.matches(text, encryptedText);
    	}
    	// ------------------
    	// B_CRYPT_PASSWORD_SALT
    	else if (AlgorithmType.B_CRYPT_PASSWORD_SALT.equals(algorithmType)) {
    		result = bCryptPasswordEncoder.matches(salt + text, encryptedText);
    	} else {
    		final String textTemp = encrypt(text, algorithmType, encodingType, salt);
    		result = (textTemp != null && textTemp.equals(encryptedText));
    	}
    	
    	return result;
    }
    
    
    /**
     * @name doRsaKey()
     * @brief Do RSA Key
     * @author Jonas Lim
     * @date 2023.12.01
     * @return
     */
    public boolean doRsaKey(RsaAction rsaAction, RsaPurpose rsaPurpose) {
    	if (RsaAction.GENERATE.equals(rsaAction)) {
    		return generateRsaKey(rsaPurpose);
    	} else if (RsaAction.READ.equals(rsaAction)) {
    		return readRsaKey(rsaPurpose);
    	} else {
    		return false;
    	}
    }
    
    
    /**
     * @name generateRsaKey()
     * @brief Generate RSA Key
     * @author Jonas Lim
     * @date 2023.11.27
     * @return
     */
    private boolean generateRsaKey(RsaPurpose rsaPurpose) {
    	if (rsaPurpose == null) return false;
    	try {
	        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	
	        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC");
	        generator.initialize(2048); // Specify key size
	        
	        // Generate keys
	        KeyPair keyPair = generator.generateKeyPair();
	        Map<RsaKeyType, Key> rsaKeyMap = new HashMap<>();
	        rsaKeyMap.put(RsaKeyType.PRIVATE_KEY, keyPair.getPrivate());
	        rsaKeyMap.put(RsaKeyType.PUBLIC_KEY, keyPair.getPublic());
	        rsaKey.put(rsaPurpose, rsaKeyMap);
	        
	        // Save keys
	        utilComponent.writeFile(rsaConfigurationProperties.getFilepath().get(rsaPurpose.getValue()).get(RsaKeyType.PRIVATE_KEY.getValue()), rsaKeyMap.get(RsaKeyType.PRIVATE_KEY).getEncoded());
	        utilComponent.writeFile(rsaConfigurationProperties.getFilepath().get(rsaPurpose.getValue()).get(RsaKeyType.PUBLIC_KEY.getValue()), rsaKeyMap.get(RsaKeyType.PUBLIC_KEY).getEncoded());
    	} catch (Exception e) {
	        rsaKey.put(rsaPurpose, null);
    	}
    	return isExistsRsaKey(rsaPurpose);
    }
    
    
    /**
     * @name readRsaKey()
     * @brief Read RSA Key For Jwt Token
     * @author Jonas Lim
     * @date 2023.11.28
     * @return
     */
    private boolean readRsaKey(RsaPurpose rsaPurpose) {
    	if (rsaPurpose == null) return false;
    	try {

		    // Read keys from files
	        Map<RsaKeyType, Key> rsaKeyMap = new HashMap<>();
	        rsaKeyMap.put(RsaKeyType.PRIVATE_KEY, readRsaPrivateKey(rsaConfigurationProperties.getFilepath().get(rsaPurpose.getValue()).get(RsaKeyType.PRIVATE_KEY.getValue())));
	        rsaKeyMap.put(RsaKeyType.PUBLIC_KEY, readRsaPublicKey(rsaConfigurationProperties.getFilepath().get(rsaPurpose.getValue()).get(RsaKeyType.PUBLIC_KEY.getValue())));
	        rsaKey.put(rsaPurpose, rsaKeyMap);
    	} catch (Exception e) {
	        rsaKey.put(rsaPurpose, null);
    	}
    	return isExistsRsaKey(rsaPurpose);
    }
    
    
//    /**
//     * @name getRsaPrivateKey()
//     * @brief Generate RSA Private Key
//     * @author Jonas Lim
//     * @date 2023.11.27
//     * @return
//     */
//    public PrivateKey getRsaPrivateKey() {
//        try {
//            byte[] encodedPrivateKey = Base64.getDecoder().decode(rsaPrivateKeyBase64);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
//            return keyFactory.generatePrivate(keySpec);
//        } catch (Exception e) {
//            // Handle key parsing exception
//            return null;
//        }
//    }
//    
//    
//    /**
//     * @name getRsaPublicKey()
//     * @brief Generate RSA Public Key
//     * @author Jonas Lim
//     * @date 2023.11.27
//     * @return
//     */
//    public PublicKey getRsaPublicKey() {
//        try {
//            byte[] encodedPublicKey = Base64.getDecoder().decode(rsaPublicKeyBase64);
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedPublicKey);
//            return keyFactory.generatePublic(keySpec);
//        } catch (Exception e) {
//            // Handle key parsing exception
//            return null;
//        }
//    }
    
    
    /**
     * @name readRsaPrivateKey(String filePath)
     * @brief Read RSA Private Key
     * @author Jonas Lim
     * @date 2023.11.28
     * @param filePath
     * @return
     */
    private PrivateKey readRsaPrivateKey(String filePath) throws Exception {
        byte[] privateKeyBytes = utilComponent.readFile(filePath);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }
    
    
    /**
     * @name readRsaPublicKey(String filePath)
     * @brief Read RSA Public Key
     * @author Jonas Lim
     * @date 2023.11.28
     * @param filePath
     * @return
     */
    private PublicKey readRsaPublicKey(String filePath) throws Exception {
        byte[] publicKeyBytes = utilComponent.readFile(filePath);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }
    
    
    /**
     * @name isExistsRsaKey(RsaPurpose rsaPurpose)
     * @brief Exist Rsa Key
     * @author Jonas Lim
     * @date 2023.12.01
     * @param rsaPurpose
     * @return
     */
    public boolean isExistsRsaKey(RsaPurpose rsaPurpose) {
    	if (rsaPurpose == null) return false;
    	return (rsaKey.get(rsaPurpose) != null);
    }

    
    /**
     * @name getRsaKeyInternally(RsaPurpose rsaPurpose, RsaKeyType rsaKeyType)
     * @brief Get Rsa Key Internally
     * @author Jonas Lim
     * @date 2023.12.01
     * @param rsaPurpose
     * @param rsaKeyType
     * @return
     */
    private Key getRsaKeyInternally(RsaPurpose rsaPurpose, RsaKeyType rsaKeyType) {
    	if (rsaPurpose == null || rsaKeyType == null) return null;
    	return rsaKey.get(rsaPurpose).get(rsaKeyType);
    }
    
    
    /**
     * @name getRsaPrivateKey(RsaPurpose rsaPurpose)
     * @brief Get Rsa Private Key
     * @author Jonas Lim
     * @date 2023.12.01
     * @param rsaPurpose
     * @return
     */
    public PrivateKey getRsaPrivateKey(RsaPurpose rsaPurpose) {
    	if (rsaPurpose == null) return null;
    	return (PrivateKey) getRsaKeyInternally(rsaPurpose, RsaKeyType.PRIVATE_KEY);
    }
    
    
    /**
     * @name getRsaPublicKey(RsaPurpose rsaPurpose)
     * @brief Get Rsa Public Key
     * @author Jonas Lim
     * @date 2023.12.01
     * @param rsaPurpose
     * @return
     */
    public PublicKey getRsaPublicKey(RsaPurpose rsaPurpose) {
    	if (rsaPurpose == null) return null;
    	return (PublicKey) getRsaKeyInternally(rsaPurpose, RsaKeyType.PUBLIC_KEY);
    }
    
}


/**
 * @name RsaConfigurationProperties
 * @brief Get Rsa Configuration Properties
 * @author Jonas Lim
 * @date 2023.12.01
 * @return
 */
@Configuration
@Getter 
@ConfigurationProperties(prefix = "rsa") 
class RsaConfigurationProperties { 
	private final Map<String, Map<String, String>> filepath = new HashMap<>();
}


/**
 * SecretKeySpec - Algorithm List
 * https://docs.oracle.com/en/java/javase/11/docs/specs/security/standard-names.html
 * 
	Algorithm Name												Description
	AES															Parameters for use with the AES algorithm.
	Blowfish													Parameters for use with the Blowfish algorithm.
	ChaCha20-Poly1305											Parameters for use with the ChaCha20-Poly1305 algorithm, as defined in RFC 8103.
	DES															Parameters for use with the DES algorithm.
	DESede														Parameters for use with the DESede algorithm.
	DiffieHellman												Parameters for use with the DiffieHellman algorithm.
	DSA															Parameters for use with the Digital Signature Algorithm.
	EC															Parameters for use with the EC algorithm.
	OAEP														Parameters for use with the OAEP algorithm.
	PBEWith<digest>And<encryption> PBEWith<prf>And<encryption>	Parameters for use with PKCS #5 password-based encryption, where <digest> is a message digest, <prf> is a pseudo-random function, and <encryption> is an encryption algorithm. Examples: PBEWithMD5AndDES, and PBEWithHmacSHA256AndAES.
	PBE															Parameters for use with the PBE algorithm. This name should not be used, in preference to the more specific PBE-algorithm names previously listed.
	RC2															Parameters for use with the RC2 algorithm.
	RSASSA-PSS													Parameters for use with the RSASSA-PSS signature algorithm.
	XDH															Parameters for Diffie-Hellman key agreement with elliptic curves as defined in RFC 7748
	X25519														Parameters for Diffie-Hellman key agreement with Curve25519 as defined in RFC 7748
	X448														Parameters for Diffie-Hellman key agreement with Curve448 as defined in RFC 7748
*/