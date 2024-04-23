package com.jonastalk.auth.v1.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jonastalk.auth.v1.api.field.key.KeyRsaGenerateRequest;
import com.jonastalk.auth.v1.api.field.key.KeyRsaGenerateResponse;
import com.jonastalk.auth.v1.api.field.key.KeyRsaReadRequest;
import com.jonastalk.auth.v1.api.field.key.KeyRsaReadResponse;
import com.jonastalk.common.component.EncryptionComponent;
import com.jonastalk.common.component.EncryptionComponent.RsaAction;
import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.exception.CustomException;

@Service
public class KeyService {
	
	@Autowired
	EncryptionComponent encryptionComponent;
    
	
    /**
	 * @name generateRsaKey(Map<String, Object> param)
	 * @brief Generate Rsa Key (Overwrite)
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @role ADMIN
     * @param param
     * @return
     */
    public Map<String, Object> generateRsaKey(Map<String, Object> param) throws Exception {
    	
    	// ----------------------
    	// 1. Get Params
    	// ----------------------
    	final String purpose = (String) param.get(KeyRsaGenerateRequest.PURPOSE.getName());
    	
    	
    	// ----------------------
    	// 2. Biz Logic
    	// ----------------------
    	final boolean result = encryptionComponent.doRsaKey(RsaAction.GENERATE, EncryptionComponent.RsaPurpose.getKey(purpose));
		
		
    	// ----------------------
    	// 3. Response
    	// ----------------------
    	if (result) {
        	Map<String, Object> response = new HashMap<>();
        	response.put(KeyRsaGenerateResponse.SUCCESS.getName(), result);
	        return response;
	    } else {
            throw new CustomException(EnumErrorCode.ERR001);
	    }
    }
    
    
    /**
	 * @name readRsaKey(Map<String, Object> param)
	 * @brief Read Rsa Key Into Memory
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @role ADMIN
     * @param param
     * @return
     */
    public Map<String, Object> readRsaKey(Map<String, Object> param) throws Exception {
    	
    	// ----------------------
    	// 1. Get Params
    	// ----------------------
    	final String purpose = (String) param.get(KeyRsaReadRequest.PURPOSE.getName());
    	
    	
    	// ----------------------
    	// 2. Biz Logic
    	// ----------------------
    	final boolean result = encryptionComponent.doRsaKey(RsaAction.READ, EncryptionComponent.RsaPurpose.getKey(purpose));
		
		
    	// ----------------------
    	// 3. Response
    	// ----------------------
    	if (result) {
        	Map<String, Object> response = new HashMap<>();
        	response.put(KeyRsaReadResponse.SUCCESS.getName(), result);
	        return response;
	    } else {
            throw new CustomException(EnumErrorCode.ERR001);
	    }
    }

}
