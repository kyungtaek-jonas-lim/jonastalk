//package com.jonastalk.common.api;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.core.MethodParameter;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.DisabledException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//import org.springframework.web.servlet.NoHandlerFoundException;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.jonastalk.common.api.field.CommonParams;
//import com.jonastalk.common.component.JonasTimeUtil;
//import com.jonastalk.common.consts.EnumErrorCode;
//import com.jonastalk.common.exception.CustomException;
//import com.jonastalk.common.exception.CustomExceptionNotFoundResponse;
//import com.jonastalk.common.exception.CustomExceptionResponse;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.JwtException;
//import lombok.extern.slf4j.Slf4j;
//
///**
// * @name CustomExceptionHandler.java
// * @brief CustomExceptionHandler
// * @author Jonas Lim
// * @date 2023.10.31
// */
//@Slf4j
//@RestControllerAdvice
//public class ApiRestConrollerAdvice implements ResponseBodyAdvice<Object> {
//	
//	
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ResponseEntity<?> noHandlerFoundHandle(NoHandlerFoundException e) {
//
//		// ---------------------------
//		// NOT FOUND (404) ERROR
//		// ---------------------------
//    	EnumErrorCode enumErrorCode = EnumErrorCode.ERR003;
//    	CustomExceptionNotFoundResponse response = new CustomExceptionNotFoundResponse(
//    			enumErrorCode.getStatus().value(),
//    			enumErrorCode.getErrorMessage(),
//    			enumErrorCode.name(),
//				e.getHeaders().toString(),
//				e.getHttpMethod().toString(),
//				e.getRequestURL().toString());
//        return ResponseEntity.status(enumErrorCode.getStatus()).body(response);
//    }
//	
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception e){
//    	e.printStackTrace();
//    	HttpStatus responseStatus = null;
//    	CustomExceptionResponse customExceptionResponse = null;
//
//		// ---------------------------
//    	// Custom Exception
//		// ---------------------------
//    	if (e instanceof CustomException) {
//    		CustomException customException = (CustomException)e;
//    		CustomExceptionResponse response = new CustomExceptionResponse(
//    				customException.getStatus().value(),
//    				customException.getErrorMessage(),
//    				customException.getErrorCode().name());
//    		responseStatus = customException.getStatus();
//    		customExceptionResponse = response;
//    	}
//		// ---------------------------
//    	// Common Exception
//		// ---------------------------
//    	else {
//    		EnumErrorCode enumErrorCode = null;
//        	if (e instanceof ExpiredJwtException) {
//            	enumErrorCode = EnumErrorCode.ERR004;
//        	}
//        	else if (e instanceof JwtException) {
//            	enumErrorCode = EnumErrorCode.ERR005;
//        	}
//        	else if (e instanceof AccessDeniedException) {
//            	enumErrorCode = EnumErrorCode.ERR006;
//        	}
//        	else if (e instanceof DisabledException) {
//            	enumErrorCode = EnumErrorCode.ERR007;
//        	}
//        	else if (e instanceof BadCredentialsException) {
//            	enumErrorCode = EnumErrorCode.ERR008;
//        	}
//        	else if (e instanceof UsernameNotFoundException) {
//            	enumErrorCode = EnumErrorCode.ERR009;
//        	}
//        	
//        	if (enumErrorCode != null) {
//	    		CustomExceptionResponse response = new CustomExceptionResponse(
//	    				enumErrorCode.getStatus().value(),
//	    				enumErrorCode.getErrorMessage(),
//	    				enumErrorCode.name());
//	    		responseStatus = enumErrorCode.getStatus();
//	    		customExceptionResponse = response;
//        	}
//        	else {
//    	    	CustomExceptionResponse response = new CustomExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), EnumErrorCode.ERR001.name());
//	    		responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
//	    		customExceptionResponse = response;
//	    		
//	    		log.error(response.toString());
//	    		/**
//	    		 * @TODO Add TransactionId on error log
//	    		 */
//        	}
//    	}
//    	
//    	
//		// ---------------------------
//    	// Response
//		// ---------------------------
//        return ResponseEntity.status(responseStatus).body(customExceptionResponse);
//    }
//    
//    public void handleExceptionsFromOncePerRequestFilter(Exception e, HttpServletResponse response) 
//            throws ServletException, IOException {
//    	CustomExceptionResponse exceptionResponse = null;
//    	if (e instanceof ExpiredJwtException) {
//    		exceptionResponse = new CustomExceptionResponse(
//    				EnumErrorCode.ERR004.getStatus().value(),
//    				EnumErrorCode.ERR004.getErrorMessage(),
//    				EnumErrorCode.ERR004.name());
//    		
//    	} else if (e instanceof JwtException) {
//    		exceptionResponse = new CustomExceptionResponse(
//    				EnumErrorCode.ERR005.getStatus().value(),
//    				EnumErrorCode.ERR005.getErrorMessage(),
//    				EnumErrorCode.ERR005.name());
//    		
//    	} else {
//    		exceptionResponse = new CustomExceptionResponse(
//    				EnumErrorCode.ERR001.getStatus().value(),
//    				EnumErrorCode.ERR001.getErrorMessage(),
//    				EnumErrorCode.ERR001.name());
//    	}
//		if (exceptionResponse != null) {
//			log.error("{} - {}", e.getClass().getName(), e.getMessage());
//
//        	// ----------------
//        	// Response Data - `Common` part
//	        ObjectMapper mapper = new ObjectMapper();
//	        Map<String, Object> commonResponseData = mapper.readValue(exceptionResponse.toString(), Map.class);
//        	
//        	// ----------------
//        	// Response Data - `Data` part
//        	Map<String, Object> dataResponseData = new HashMap<>();
//        	
//        	// ----------------
//        	// Response Data
//        	Map<String, Object> responseData = new HashMap<>();
//        	responseData.put(CommonParams.COMMON.getValue(), commonResponseData);
//        	responseData.put(CommonParams.DATA.getValue(), dataResponseData);
//        	JSONObject jsonObject = new JSONObject(responseData);
//        	
//        	// ----------------
//        	// Response Object
//			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//            response.setStatus(exceptionResponse.getStatus());
//            response.getWriter().write(jsonObject.toString());
//		}
//    }
//
//
//	@Override
//	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
//		return true; // Apply this advice to all controller methods
//	}
//
//
//	@Override
//	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
//			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
//			ServerHttpResponse response) {
//
//		// -------------------
//		// Success Response
//		// -------------------
//        if (body instanceof HashMap) {
//        	
//        	// ----------------
//        	// Response Data - `Common` part
//        	Map<String, Object> commonResponseData = new HashMap<>();
//    		commonResponseData.put(CommonParams.STATUS.getValue(), HttpStatus.OK.value());
//    		String currentDateTime	= null;
//    		currentDateTime	= JonasTimeUtil.convertZonedDateTimeToISO8601String(JonasTimeUtil.getCurrentZonedDateTime(null));
//    		commonResponseData.put(CommonParams.CURRENT_DATETIME.getValue(), currentDateTime);
//        	/**
//        	 * @TODO Put TransactionID
//        	 */
//        	
//        	// ----------------
//        	// Response Data - `Data` part
//        	Map<String, Object> dataResponseData = (HashMap<String, Object>) body;
//        	
//        	// ----------------
//        	// Response Data
//        	Map<String, Object> responseData = new HashMap<>();
//        	responseData.put(CommonParams.COMMON.getValue(), commonResponseData);
//        	responseData.put(CommonParams.DATA.getValue(), dataResponseData);
//        	log.debug(responseData.toString());
//            return responseData;
//            
//        }
//		// -------------------
//		// Error Response
//		// -------------------
//        else if (body instanceof CustomExceptionResponse) {
//        	
//        	// ----------------
//        	// Response Data - `Data` part
//        	Map<String, Object> dataResponseData = new HashMap<>();
//        	
//        	// ----------------
//        	// Response Data
//        	Map<String, Object> responseData = new HashMap<>();
//        	responseData.put(CommonParams.COMMON.getValue(), body);
//        	responseData.put(CommonParams.DATA.getValue(), dataResponseData);
//        	log.debug(responseData.toString());
//            return responseData;
//        }
//
//        // If it's not the type you want to modify, return the original body
//        return body;
//	}
//	
//	
//	
//}
