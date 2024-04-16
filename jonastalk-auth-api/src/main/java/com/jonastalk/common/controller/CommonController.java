package com.jonastalk.common.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jonastalk.common.service.RedissonService;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @name CommonController.java
 * @brief Common Controller
 * @author Jonas Lim
 * @date 2023.12.07
 */
@RestController
@RequestMapping("/common")
@ApiIgnore
public class CommonController {

	@Autowired
	RedissonService redissonService;

	@PostMapping("/test")
	public String test() {
		return "Success";
	}
	
	/**
	 * @name redis()
	 * @brief Test Redis
	 * @author Jonas Lim
	 * @date 2023.12.01
	 * @param param
     * @return
	 */
	@PostMapping("/redis")
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "This endpoint is ignored by Swagger")
	@ApiIgnore
    public Map<String, Object> redis(@RequestBody Map<String, Object> param) {

		String processType		= (String) param.get("processType");	// ["PUT", "GET", "REMOVE"]
		String dataType			= (String) param.get("dataType");	// ["STRING", "SET", "MAP", "SET_ITSELF", "MAP_ITSELF"]
		String collectionName	= (String) param.get("collectionName");
		String key				= (String) param.get("key");
		String value			= (String) param.get("value");
		Long ttlSec				= null;
		if (param.get("ttlSec") instanceof Integer) {
			ttlSec = Long.valueOf((Integer) param.get("ttlSec"));
		}
		String key1				= null;
		String value1			= null;
		String key2				= null;
		String value2			= null;
		if (param.get("key1") instanceof String) {
			key1 = (String) param.get("key1");
		}
		if (param.get("value1") instanceof String) {
			value1 = (String) param.get("value1");
		}
		if (param.get("key2") instanceof String) {
			key2 = (String) param.get("key2");
		}
		if (param.get("value2") instanceof String) {
			value2 = (String) param.get("value2");
		}
		
		boolean result = false;
		Object getValue = null;
		if ("PUT".equals(processType)) {
			if ("STRING".equals(dataType)) {
				result = redissonService.putDataWithLock(key, value, ttlSec);
			} else if ("SET".equals(dataType)) {
				result = redissonService.addToSetWithLock(collectionName, value, ttlSec);
			} else if ("MAP".equals(dataType)) {
				result = redissonService.putDataInMapWithLock(collectionName, key, value, ttlSec);
			} else if ("SET_ITSELF".equals(dataType)) {
				Set<String> set = new HashSet<>();
				set.add(value);
				if (value1 != null) set.add(value1);
				if (value2 != null) set.add(value2);
				result = redissonService.putSetWithLock(collectionName, set, ttlSec);
			} else if ("MAP_ITSELF".equals(dataType)) {
				Map<String, String> map = new HashMap<>();
				map.put(key, value);
				if (key1 != null) map.put(key1, value1);
				if (key2 != null) map.put(key2, value2);
				result = redissonService.putMapWithLock(collectionName, map, ttlSec);
			}
		} else if ("GET".equals(processType)) {
			if ("STRING".equals(dataType)) {
				getValue = redissonService.getDataWithLock(key);
			} else if ("SET".equals(dataType)) {
				getValue = redissonService.getDataFromSetWithLock(collectionName, value);
			} else if ("MAP".equals(dataType)) {
				getValue = redissonService.getDataFromMapWithLock(collectionName, key);
			} else if ("SET_ITSELF".equals(dataType)) {
				getValue = redissonService.getSetWithLock(collectionName);
			} else if ("MAP_ITSELF".equals(dataType)) {
				getValue = redissonService.getMapWithLock(collectionName);
			}
			result = (getValue != null);
		} else if ("REMOVE".equals(processType)) {
			if ("STRING".equals(dataType)) {
				result = redissonService.deleteData(key);
			} else if ("SET".equals(dataType)) {
				result = redissonService.removeDataFromSet(collectionName, value);
			} else if ("MAP".equals(dataType)) {
				result = redissonService.removeDataFromMap(collectionName, key);
			} else if ("SET_ITSELF".equals(dataType)) {
				result = redissonService.removeSet(collectionName);
			} else if ("MAP_ITSELF".equals(dataType)) {
				result = redissonService.removeMap(collectionName);
			}
		}
		
		Map<String, Object> response = new HashMap<>();
		if (getValue != null) {
			response.put("result", getValue);
		} else {
			response.put("result", result);
		}
		return response;
    }
}
