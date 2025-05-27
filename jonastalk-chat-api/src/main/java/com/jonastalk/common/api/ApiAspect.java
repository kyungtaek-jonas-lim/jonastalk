package com.jonastalk.common.api;

import java.util.LinkedHashMap;
import java.util.Map;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.jonastalk.chat.v1.api.AllValidationRequestURI;
import com.jonastalk.common.api.field.CommonParams;
import com.jonastalk.common.component.ValidationComponent;
import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.exception.CustomException;

import io.azam.ulidj.ULID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class ApiAspect {
	
	@Autowired
	ValidationComponent validationComponent;

    @Pointcut("execution(* com.jonastalk.chat.v1.controller.*Controller.*(..))"
//    		+ " || execution(* com.jonastalk.*.*Controller.*(..))"
    		)
    public void apiRequest() {
        // Pointcut expression to match all methods in controllers within com.jonastalk package
    }

    @Before("apiRequest()")
    public void beforeApiRequest(JoinPoint joinPoint) throws Exception {

        // ------------------------
        // Get Request Info
        // ------------------------ 
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String requestURI = null;
        if (requestAttributes != null && requestAttributes.getRequest() != null) {
            requestURI = requestAttributes.getRequest().getRequestURI();
        }
        String methodName		= null;
        String className		= null;
        Object[] proceedResult	= null;
        if (joinPoint.getSignature() != null) {
        	methodName = joinPoint.getSignature().getName();
        }
        if (joinPoint.getTarget() != null) {
        	className = joinPoint.getTarget().getClass().getSimpleName();
        }
        proceedResult = joinPoint.getArgs();
        log.debug("[{} - {}] \"{}\" - {}", className, methodName, requestURI, proceedResult);
        
        
        
        // ------------------------
        // Get Parameter
        // - proceedResult has a form that is set in Controllers with @RequestBody
        // ------------------------ 
        Map<String, Object> param = null;
        Map<String, Object> commonParam = null;
        Map<String, Object> dataParam = null;
        if (proceedResult != null && proceedResult.length > 0) {
        	for (int i = 0; i < proceedResult.length; i++) {
		        if(proceedResult[i] instanceof LinkedHashMap) {
		            param = (LinkedHashMap<String, Object>)proceedResult[i];
		            if (param == null 
		            		|| !(param.get(CommonParams.COMMON.getValue()) instanceof LinkedHashMap)
		            		|| !(param.get(CommonParams.DATA.getValue()) instanceof LinkedHashMap)
		            		) {
		            	continue;
		            }
		            commonParam = (LinkedHashMap<String, Object>) param.get(CommonParams.COMMON.getValue());
		            dataParam = (LinkedHashMap<String, Object>) param.get(CommonParams.DATA.getValue());
		            break;
		        }
        	}
        }
        
        
        
        // ------------------------
        // Validate `Common` Part
        // ------------------------
        
        
        // ------------------------
        // Validate `Data` Part
        // ------------------------
        // If Authorize can't be done, before this, it will be not meaningful.
        // It might have to be done in every single controller method if necessary.
        Map<String, AllValidationRequestURI> allValidationRequestURI = AllValidationRequestURI.getAllValidationRequestURI();
        AllValidationRequestURI.ConrollerParamType conrollerParamType = null;
        if (allValidationRequestURI.get(requestURI) != null) {
        	conrollerParamType = allValidationRequestURI.get(requestURI).getDataType();
        	if (AllValidationRequestURI.ConrollerParamType.MAP_COMMON_DATA.equals(conrollerParamType)) {
                if (param == null || commonParam == null) {
                	throw new CustomException(EnumErrorCode.ERR002, CommonParams.COMMON.getValue());
                }
                if (dataParam == null) {
                	throw new CustomException(EnumErrorCode.ERR002, CommonParams.DATA.getValue());
                }
            	validationComponent.validateApiRequestData(dataParam, allValidationRequestURI.get(requestURI).getRequestParam());
        	} else if (AllValidationRequestURI.ConrollerParamType.MAP.equals(conrollerParamType)) {
                if (param == null) {
                	throw new CustomException(EnumErrorCode.ERR002, CommonParams.COMMON.getValue());
                }
            	validationComponent.validateApiRequestData(param, allValidationRequestURI.get(requestURI).getRequestParam());
        	} else {
        		
        	}
        }
        
        
        // ------------------------
        // Put TransactionId (ULID)
        // ------------------------
		String transactionId = ULID.random() + ULID.random().substring(10);
		if (AllValidationRequestURI.ConrollerParamType.MAP_COMMON_DATA.equals(conrollerParamType)) {
			dataParam.put(CommonParams.TRANSACTION_ID.getValue(), transactionId);
    	} else if (AllValidationRequestURI.ConrollerParamType.MAP.equals(conrollerParamType)) {
    		param.put(CommonParams.TRANSACTION_ID.getValue(), transactionId);
    	} else {
    		
    	}
		
		/**
		 * @TODO Whether to put request data in DB
		 * @TODO if there's no `Data` part, how to put `transactionId`
		 */
    	
    	
        // ------------------------
        // Put Only `Data` Part
        // ------------------------
		if (AllValidationRequestURI.ConrollerParamType.MAP_COMMON_DATA.equals(conrollerParamType)) {
	        param.clear();
	        param.putAll(dataParam);
		}
		
    }
    
    
    

    @AfterThrowing(pointcut = "execution(* com.jonastalk.chat.v1.controller.*Controller.*(..))"
//    		+ " || execution(* com.jonastalk.*.*Controller.*(..))"
    		, throwing = "ex")
    public void callControllerException(Exception ex) throws Exception {
    	
    	// TODO When Refresh Access Token with Access Token instead of Refresh Token, an error occurs 
    	System.out.println(ex);
    }
}
