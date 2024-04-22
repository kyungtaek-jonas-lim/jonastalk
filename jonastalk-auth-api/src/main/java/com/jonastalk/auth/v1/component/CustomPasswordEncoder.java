package com.jonastalk.auth.v1.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jonastalk.common.component.EncryptionComponent;
import com.jonastalk.common.component.EncryptionComponent.EncodingType;

/**
 * @name CustomPasswordEncoder.java
 * @brief Customized Password Encoder
 * @process Encrypt
 * @author Jonas Lim
 * @date 2023.11.27
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

	@Autowired
	EncryptionComponent encryptionComponent;

    @Override
    public String encode(CharSequence rawPassword) {
    	if (rawPassword == null) return null;
    	final StringBuffer sb = new StringBuffer(rawPassword.length());
    	sb.append(rawPassword);
    	try {
    		return encryptionComponent.encrypt(sb.toString(), EncryptionComponent.AlgorithmType.B_CRYPT_PASSWORD, EncodingType.UTF_8, null);
    	} catch (Exception e) {
    		return null;
    	}
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
    	if (rawPassword == null) return false;
    	final StringBuffer sb = new StringBuffer(rawPassword.length());
    	sb.append(rawPassword);
    	try {
    		return encryptionComponent.match(sb.toString(), encodedPassword, EncryptionComponent.AlgorithmType.B_CRYPT_PASSWORD, EncodingType.UTF_8, null);
    	} catch (Exception e) {
    		return false;
    	}
    }
}
