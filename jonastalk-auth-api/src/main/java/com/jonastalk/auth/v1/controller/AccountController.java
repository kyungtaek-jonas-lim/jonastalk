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
 * @name AccountController.java
 * @brief Account Controller
 * @author Jonas Lim
 * @date 2023.12.01
 */
@RestController
@RequestMapping("/v1/account")
@Api(tags = "Account API")
public class AccountController {
	
	@Autowired
	AccountService accountService;
	
	/**
	 * @name getExistenceUsername(@RequestBody Map<String, Object> param)
	 * @brief Get Existence Username
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @param param
	 * @return
	 * @throws Exception
	 */
    @PostMapping("/username/exist")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get Username Existance Status",
    				notes = "Additional notes about the endpoint")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "param", value = "Username", required = true, dataType = "String", paramType = "body"
    			,access = "ALL"
    			,example = "{\r\n"
    					+ "    \"common\": {\r\n"
    					+ "\r\n"
    					+ "    },\r\n"
    					+ "    \"data\" : {\r\n"
    					+ "        \"username\" : \"username\"\r\n"
    					+ "    }\r\n"
    					+ "}")
    })
//    @ApiResponses(value = {
//            @ApiResponse(code = 200, message = "Successfully retrieved user", examples = @io.swagger.annotations.Example(value = {
//                    @ExampleProperty(value = "{\r\n"
//                    		+ "    \"common\": {\r\n"
//                    		+ "        \"status\": 200,\r\n"
//                    		+ "        \"currentDatetime\": \"2023-12-06T13:10:07.035620900Z\"\r\n"
//                    		+ "    },\r\n"
//                    		+ "    \"data\": {\r\n"
//                    		+ "        \"exist\": true,\r\n"
//                    		+ "        \"username\": \"username\"\r\n"
//                    		+ "    }\r\n"
//                    		+ "}")
//            })),
//            @ApiResponse(code = 401, message = "User not found"),
//            @ApiResponse(code = 500, message = "Internal server error", response = CustomExceptionResponse.class, examples = @io.swagger.annotations.Example(value = {
//                    @ExampleProperty(value = "{'errorCode': 'SERVER_ERROR', 'message': 'Internal server error occurred'}")
//            }))
//    })
    public ResponseEntity<?> getExistenceUsername(@RequestBody Map<String, Object> param) throws Exception {
		return ResponseEntity.ok(accountService.getExistenceUsername(param));
    }

}
