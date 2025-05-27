package com.jonastalk.common.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import com.jonastalk.common.component.EncryptionComponent;

/**
 * @name JasyptConfig.java
 * @brief JasyptConfig
 * @author Jonas Lim
 * @date 2023.10.31
 */
@Configuration
public class JasyptConfig {

    @Value("${jasypt.encryptor.password}")
    private String password;
    
	@Autowired
	@Lazy
	EncryptionComponent encryptionComponent;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){
        
    	PooledPBEStringEncryptor encryptor 	= new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config 		= new SimpleStringPBEConfig();
        
        // --------------
        // AES decrypt
        try {
			password = encryptionComponent.decrypt(password, EncryptionComponent.AlgorithmType.AES, EncryptionComponent.EncodingType.UTF_8, null);
		} catch (Exception e) {
			e.printStackTrace();
		}

        // --------------
        // Set Encryption Config
        config.setPassword(password);
        config.setPoolSize("1");
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setStringOutputType("base64");
        config.setKeyObtentionIterations("1000");
        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        encryptor.setConfig(config);
        return encryptor;
    }
}