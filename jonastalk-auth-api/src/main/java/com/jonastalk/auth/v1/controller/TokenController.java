package com.jonastalk.auth.v1.controller;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonastalk.auth.v1.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @name TokenController.java
 * @brief Token Controller
 * @author Jonas Lim
 * @date 2023.12.01
 */
@RestController
@RequestMapping("/v1/token")
@Api(tags = "Token API")
public class TokenController {
	
	@Autowired
	AccountService accountService;
	
	
	/**
	 * @name generateAccessToken(@RequestBody Map<String, Object> param)
	 * @brief Generate Access Token
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @param param
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/access/generate")
//  @PreAuthorize("permitAll()")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Generate Access Token",
			notes = "Generate Access Token")
		@ApiImplicitParams({
		@ApiImplicitParam(name = "param", value = "param", required = true, dataType = "Map", paramType = "body"
			,access = "ALL"
			,example = "{\r\n"
					+ "    \"common\": {},\r\n"
					+ "    \"data\": {\r\n"
					+ "        \"username\": \"username\",\r\n"
					+ "        \"password\": \"password\"\r\n"
					+ "    }\r\n"
					+ "}")
		})
	public ResponseEntity<?> generateAccessToken(@RequestBody Map<String, Object> param) throws Exception {
    	return ResponseEntity.ok(accountService.generateAccessToken(param));
	}
	
	
	/**
	 * @name validateAccessToken(@RequestBody Map<String, Object> param)
	 * @brief Validate Access Token
	 * @author Jonas Lim
	 * @date 2024.02.21
	 * @param param
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/access/validate")
//  @PreAuthorize("permitAll()")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Generate Access Token",
			notes = "Generate Access Token")
		@ApiImplicitParams({
		@ApiImplicitParam(name = "param", value = "param", required = true, dataType = "Map", paramType = "body"
			,access = "ALL"
			,example = "{\r\n"
					+ "    \"common\": {},\r\n"
					+ "    \"data\": {\r\n"
//					+ "        \"username\": \"username\",\r\n"
					+ "        \"accessToken\": \"eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwidG9rZW5UeXBlIjoiQUNDRVNTIiwiZXhwIjoxNzA4NDgxNDc4LCJpYXQiOjE3MDg0ODA4Nzh9.awad8_wvPb2h27UeiB0m18B2Z03lVYZAD3Qy286Ft3X1oLUGvVUS6mJ61-h90YXeHn4uiZF6WNuTCre4FGJx6JeQuRx53S74jnPbCANpokeZyVOtoB5s_v-riTM7LcTKSjpm4PhOY_MitP14D4F6nhdmZD7M6z1qTQhQlPlymk9LFNnS8328cmLhex9JxAgGd4BrFgeUHrkQs0f-t7VOSJEDrXVITmlldTjNB5sEZ-nkt-UvTkJZGmr6basuki6z6reR3mCYjT6nxJ3ke6nkybyzoMI1ndE534b6a0xOeBQrsexuB6viRaHnA-KzQVmV3PNcz3XMEcFW9GADCqYZFw\"\r\n"
					+ "    }\r\n"
					+ "}")
		})
	public ResponseEntity<?> validateAccessToken(@RequestBody Map<String, Object> param) throws Exception {
    	return ResponseEntity.ok(accountService.validateAccessToken(param));
	}
    
    
    /**
	 * @name refreshAccessToken(@RequestBody Map<String, Object> param)
	 * @brief Generate Refresh Token
	 * @author Jonas Lim
	 * @date 2023.12.01
     * @param request
     * @param param
     * @return
     */
	@PostMapping("/access/refresh")
//    @PreAuthorize("permitAll()")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Generate Refresh Token",
			notes = "Generate Refresh Token")
		@ApiImplicitParams({
		@ApiImplicitParam(name = "param", value = "param", required = true, dataType = "Map", paramType = "body"
			,access = "ALL"
			,example = "{\r\n"
					+ "    \"common\": {},\r\n"
					+ "    \"data\": {\r\n"
					+ "        \"refreshToken\": \"eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJyb290IiwidG9rZW5UeXBlIjoiUkVGUkVTSCIsImV4cCI6MTcwMTg2OTIyOCwiaWF0IjoxNzAxODY4NjI4fQ.ZC0m3PMGQO-GwbxTySOJsFMSAp3W8CGORs5BIVIOCJWPHMWoRym26-qfC96BRAVTtwsqfYcbJ2gkaXnKaVbiW7iWKvn6BTeX7Fuc9NCvcnArUKIm9pvswm4A_3UjrF9UL5FvXIr-zOtr1EiSr9s8Ma56mIIEZQmzRn-tQPo6JM8BjyreKt3zPtHsm6W4oq5uxZ87o-2mhjlsMigsIxqgQ6SR1v2mDnl3YNdYcGhpDyYfc_IFcaQOyV5XkjThNzLDY8z2DU9x2fy6MmFuxD5l_0uE-bBmryt7q0Ul4Caemft7w_Qettiyas3GwWYK-0nARIHGSThoXe61Bh9v_5ufNw\",\r\n"
//					+ "        \"username\": \"username\"\r\n"
					+ "    }\r\n"
					+ "}")
		})
	 public ResponseEntity<?> refreshAccessToken(@RequestBody Map<String, Object> param) throws Exception {
    	return ResponseEntity.ok(accountService.refreshAccessToken(param));
	}


}
