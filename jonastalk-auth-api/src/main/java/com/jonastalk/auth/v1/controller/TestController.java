package com.jonastalk.auth.v1.controller;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonastalk.common.component.JonasTimeUtil;
import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.exception.CustomException;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @name TestController.java
 * @brief Test Controller
 * @author Jonas Lim
 * @date 2023.12.01
 */
@RestController
@RequestMapping("/test")
@ApiIgnore
public class TestController {
    
	/**
	 * @name getSecuredEndpoint(@RequestBody Map<String, Object> param)
	 * @brief Secured endpoint accessible only to authenticated users with a specific role
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @role ADMIN, USER
	 * @param param
     * @return
	 */
	@PostMapping("/secured")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	@ApiOperation(value = "This endpoint is ignored by Swagger")
	@ApiIgnore
    public ResponseEntity<String> getSecuredEndpoint(@RequestBody(required = false) Map<String, Object> param) {
        return ResponseEntity.ok("This is a secured endpoint accessible to users with ROLE_USER!");
    }

    
	/**
	 * @name getCurrentTime(@RequestBody Map<String, Object> param)
	 * @brief Get Current Time
	 * @author Jonas Lim
	 * @date 2023.12.05
	 * @param param
     * @return
	 */
    @PostMapping("/current-time")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "This endpoint is ignored by Swagger")
    @ApiIgnore
    public ResponseEntity<?> getCurrentTime(@RequestBody(required = false) Map<String, Object> param) {
    	
    	String format = null;
    	if (param != null && param.get("format") instanceof String) {
    		format = (String) param.get("format");
    	}
    	String timeZone = null;
    	if (param != null && param.get("timeZone") instanceof String) {
    		timeZone = (String) param.get("timeZone");
    	}
    	
    	if (!StringUtils.hasText(format)) {
    		format = "ISO8601";
    	}
    	if (!StringUtils.hasText(timeZone)) {
    		timeZone = "UTC";
    	}
		
		ZoneId zoneId = null;
		try {
			zoneId = ZoneId.of(timeZone);
		} catch (Exception e) {}
		if (zoneId == null) {
			throw new CustomException(EnumErrorCode.ERR002, "timeZone");
		}
		String currentDateTime	= null;
		if (format.trim().toUpperCase().equals("ISO8601")) {
			currentDateTime		= JonasTimeUtil.convertZonedDateTimeToISO8601String(JonasTimeUtil.getCurrentZonedDateTime(zoneId));
		} else {
			currentDateTime		= JonasTimeUtil.convertZonedDateTimeToString(JonasTimeUtil.getCurrentZonedDateTime(zoneId), format);
		}
		
		Map<String, Object> response = new HashMap<>();
		response.put("result", 								"success");
		response.put("currentDatetime", 					currentDateTime);
		response.put("timeZone", 							timeZone);
		response.put("format", 								format);
    	
		return ResponseEntity.ok(response);
    }

}
