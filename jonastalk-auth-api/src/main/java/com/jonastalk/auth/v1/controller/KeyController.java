package com.jonastalk.auth.v1.controller;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonastalk.auth.v1.service.KeyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @name KeyController.java
 * @brief Key Controller
 * @author Jonas Lim
 * @date 2023.12.01
 */
@RestController
@RequestMapping("/v1/key")
@Api(tags = "Key API")
public class KeyController {
	
	@Autowired
	KeyService keyService;
    
	
    /**
	 * @name generateRsaKey(@RequestBody Map<String, Object> param)
	 * @brief Generate Rsa Key (Overwrite)
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @role ADMIN
     * @param param
     * @return
     */
    @PostMapping("/rsa/generate")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Generate Rsa Key (Overwrite)",
			notes = "Generate Rsa Key (Overwrite)")
		@ApiImplicitParams({
		@ApiImplicitParam(name = "param", value = "param", required = true, dataType = "Map", paramType = "body"
			,access = "ADMIN"
			,example = "{\r\n"
					+ "    \"common\": {},\r\n"
					+ "    \"data\": {\r\n"
					+ "        \"purpose\": \"password\"\r\n"
					+ "    }\r\n"
					+ "}")
		})
    public ResponseEntity<?> generateRsaKey(@RequestBody Map<String, Object> param) throws Exception {
        return ResponseEntity.ok(keyService.generateRsaKey(param));
    }
    
    
    /**
	 * @name readRsaKey(@RequestBody Map<String, Object> param)
	 * @brief Read Rsa Key Into Memory
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @role ADMIN
     * @param param
     * @return
     */
    @PostMapping("/rsa/read")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Read Rsa Key Into Memory",
			notes = "Read Rsa Key Into Memory")
		@ApiImplicitParams({
		@ApiImplicitParam(name = "param", value = "param", required = true, dataType = "Map", paramType = "body"
			,access = "ADMIN"
			,example = "{\r\n"
					+ "    \"common\": {},\r\n"
					+ "    \"data\": {\r\n"
					+ "        \"purpose\": \"password\"\r\n"
					+ "    }\r\n"
					+ "}")
		})
    public ResponseEntity<?> readRsaKey(@RequestBody Map<String, Object> param) throws Exception {
        return ResponseEntity.ok(keyService.readRsaKey(param));
    }


}
