package com.jonastalk.common.service;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RBucket;
import org.redisson.api.RExpirable;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jonastalk.common.consts.EnumErrorCode;
import com.jonastalk.common.exception.CustomException;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @name RedissonService.java
 * @brief Redisson Service
 * @author Jonas Lim
 * @date 2023.12.12
 */
@Slf4j
@Service
public class RedissonService {

	@Lazy
    @Autowired
    private RedissonClient redissonClient;
    
    @Value("${spring.redis.lock.get}")
    private boolean redisLockGet;

    @Value("${spring.redis.lock.put}")
    private boolean redisLockPut;
    
    @Value("${spring.redis.lock.wait-sec}")
    private int redisLockWaitSec;

    @Value("${spring.redis.lock.lease-sec}")
    private int redisLeaseSec;
    
    private final String REDIS_DELIMITER = "::";
    private final String LOCK_NAME_PREFIX = "lock" + REDIS_DELIMITER;
    
    /**
	 * @name RedissonDataType
	 * @brief Redisson Data Type
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    @Getter
    public enum RedissonDataType {
    	STRING		("string")
    	,SET		("set")
    	,MAP		("map")
//    	,LIST		("list")		// not yet
//    	,SORTED_SET	("sortedSet")	// not yet
    	;
    	private String value;
    	private RedissonDataType(String value) {
    		this.value = value;
    	}
    }
    
    /**
	 * @name RedissonParam
	 * @brief Redisson Parameter
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    @Getter
    public enum RedissonParam {
    	LOCK_NAME				("lockName")
    	,KEY					("key")
    	,VALUE					("value")
    	,COLLECTION_NAME		("collectionName")
    	,COLLECTION				("collection")
    	;
    	private String value;
    	private RedissonParam(String value) {
    		this.value = value;
    	}
    }
    
    /**
	 * @name validatePutParam(RedissonDataType redissonDataType, String collectionName, String key, String value)
	 * @brief Validate Put Parameter
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    private void validatePutParam(RedissonDataType redissonDataType, String collectionName, String key, String value) {
    	
    	if (redissonDataType == null) {
			log.error("redissonDataType is `{}`", redissonDataType);
			throw new CustomException(EnumErrorCode.ERR001);
    	}
		if (value == null) {
			log.error("[{}] value is `{}`", redissonDataType.name(), value);
			throw new CustomException(EnumErrorCode.ERR001);
		}
		if (!redissonDataType.equals(RedissonDataType.SET)) {
			if (!StringUtils.hasText(key)) {
				log.error("[{}] key is `{}`", redissonDataType.name(), key);
				throw new CustomException(EnumErrorCode.ERR001);
			}
		}
    	if (redissonDataType.equals(RedissonDataType.SET) || redissonDataType.equals(RedissonDataType.MAP)) {
    		if (!StringUtils.hasText(collectionName)) {
				log.error("[{}] collectionName is `{}`", redissonDataType.name(), collectionName);
				throw new CustomException(EnumErrorCode.ERR001);
    		}
    	}
    }
    
    /**
	 * @name validateGetParam(RedissonDataType redissonDataType, String collectionName, String key, String value)
	 * @brief Validate Get Parameter
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    private void validateGetParam(RedissonDataType redissonDataType, String collectionName, String key, String value) {

    	if (redissonDataType == null) {
			log.error("redissonDataType is `{}`", redissonDataType);
			throw new CustomException(EnumErrorCode.ERR001);
    	}
		if (redissonDataType.equals(RedissonDataType.SET)) {
			if (!StringUtils.hasText(value)) {
				log.error("[{}] value is `{}`", redissonDataType.name(), value);
				throw new CustomException(EnumErrorCode.ERR001);
			}
		}
		if (!redissonDataType.equals(RedissonDataType.SET)) {
			if (!StringUtils.hasText(key)) {
				log.error("[{}] key is `{}`", redissonDataType.name(), key);
				throw new CustomException(EnumErrorCode.ERR001);
			}
		}
    	if (redissonDataType.equals(RedissonDataType.SET) || redissonDataType.equals(RedissonDataType.MAP)) {
    		if (!StringUtils.hasText(collectionName)) {
				log.error("[{}] collectionName is `{}`", redissonDataType.name(), collectionName);
				throw new CustomException(EnumErrorCode.ERR001);
    		}
    	}
    }
    
    /**
	 * @name validateRemoveParam(RedissonDataType redissonDataType, String collectionName, String key, String value)
	 * @brief Validate Remove Parameter
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    private void validateRemoveParam(RedissonDataType redissonDataType, String collectionName, String key, String value) {

    	if (redissonDataType == null) {
			log.error("redissonDataType is `{}`", redissonDataType);
			throw new CustomException(EnumErrorCode.ERR001);
    	}
		if (redissonDataType.equals(RedissonDataType.SET)) {
			if (!StringUtils.hasText(value)) {
				log.error("[{}] value is `{}`", redissonDataType.name(), value);
				throw new CustomException(EnumErrorCode.ERR001);
			}
		}
		if (!redissonDataType.equals(RedissonDataType.SET)) {
			if (!StringUtils.hasText(key)) {
				log.error("[{}] key is `{}`", redissonDataType.name(), key);
				throw new CustomException(EnumErrorCode.ERR001);
			}
		}
    	if (redissonDataType.equals(RedissonDataType.SET) || redissonDataType.equals(RedissonDataType.MAP)) {
    		if (!StringUtils.hasText(collectionName)) {
				log.error("[{}] collectionName is `{}`", redissonDataType.name(), collectionName);
				throw new CustomException(EnumErrorCode.ERR001);
    		}
    	}
    }
    
    /**
	 * @name validatePutCollectionParam(RedissonDataType redissonDataType, String collectionName, Object collection)
	 * @brief Validate Put Collection Parameter
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    private void validatePutCollectionParam(RedissonDataType redissonDataType, String collectionName, Object collection) {
    	
    	if (redissonDataType == null || RedissonDataType.STRING.equals(redissonDataType)) {
			log.error("redissonDataType is `{}`", redissonDataType);
			throw new CustomException(EnumErrorCode.ERR001);
    	}
    	if (redissonDataType.equals(RedissonDataType.SET) || redissonDataType.equals(RedissonDataType.MAP)) {
    		if (!StringUtils.hasText(collectionName)) {
				log.error("[{}] collectionName is `{}`", redissonDataType.name(), collectionName);
				throw new CustomException(EnumErrorCode.ERR001);
    		}
    	}
    	if ((redissonDataType.equals(RedissonDataType.SET) && !(collection instanceof Set))
    			|| (redissonDataType.equals(RedissonDataType.MAP) && !(collection instanceof Map))
    			) {
			log.error("[{}] collection is not `{}`", redissonDataType.name(), redissonDataType.name());
			throw new CustomException(EnumErrorCode.ERR001);
    	}
    }
    
    /**
	 * @name validateGetCollectionParam(RedissonDataType redissonDataType, String collectionName)
	 * @brief Validate Get Collection Parameter
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    private void validateGetCollectionParam(RedissonDataType redissonDataType, String collectionName) {

    	if (redissonDataType == null || RedissonDataType.STRING.equals(redissonDataType)) {
			log.error("redissonDataType is `{}`", redissonDataType);
			throw new CustomException(EnumErrorCode.ERR001);
    	}
    	if (redissonDataType.equals(RedissonDataType.SET) || redissonDataType.equals(RedissonDataType.MAP)) {
    		if (!StringUtils.hasText(collectionName)) {
				log.error("[{}] collectionName is `{}`", redissonDataType.name(), collectionName);
				throw new CustomException(EnumErrorCode.ERR001);
    		}
    	}
    }
    
    /**
	 * @name validateRemoveCollectionParam(RedissonDataType redissonDataType, String collectionName)
	 * @brief Validate Remove Collection Parameter
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    private void validateRemoveCollectionParam(RedissonDataType redissonDataType, String collectionName) {

    	if (redissonDataType == null || RedissonDataType.STRING.equals(redissonDataType)) {
			log.error("redissonDataType is `{}`", redissonDataType);
			throw new CustomException(EnumErrorCode.ERR001);
    	}
    	if (redissonDataType.equals(RedissonDataType.SET) || redissonDataType.equals(RedissonDataType.MAP)) {
    		if (!StringUtils.hasText(collectionName)) {
				log.error("[{}] collectionName is `{}`", redissonDataType.name(), collectionName);
				throw new CustomException(EnumErrorCode.ERR001);
    		}
    	}
    }
    
    /**
	 * @name getLockName(RedissonDataType redissonDataType, String collectionName, String key)
	 * @brief Get Lock Name
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    private String getLockName(RedissonDataType redissonDataType, String collectionName, String key) {
    	
    	if (RedissonDataType.STRING.equals(redissonDataType)) {
    		return LOCK_NAME_PREFIX + key;
    	} else if (RedissonDataType.SET.equals(redissonDataType)) {
    		return LOCK_NAME_PREFIX + collectionName;
    	} else if (RedissonDataType.MAP.equals(redissonDataType)) {
    		if (StringUtils.hasText(key)) {
    			return LOCK_NAME_PREFIX + collectionName + REDIS_DELIMITER + key;
    		} else {
    			return LOCK_NAME_PREFIX + collectionName;
    		}
    	}  

    	return null;
    }
    
    /**
	 * @name putDataWithLock(String key, String value, Long ttlSec)
	 * @brief Put String Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    public boolean putDataWithLock(String key, String value, Long ttlSec) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.STRING;
    	validatePutParam(redissonDataType, null, key, value);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	key += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, null, key));
    	param.put(RedissonParam.KEY.getValue(),					key);
    	param.put(RedissonParam.VALUE.getValue(),				value);
    	
    	// ----------------------
    	// 3. Put Data
    	// ----------------------
    	return putDataWithLock(redissonDataType, param, ttlSec);
    }
    
    /**
	 * @name getDataWithLock(String key)
	 * @brief Get String Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    public String getDataWithLock(String key) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.STRING;
    	validateGetParam(redissonDataType, null, key, null);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	key += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, null, key));
    	param.put(RedissonParam.KEY.getValue(),					key);
    	
    	// ----------------------
    	// 3. Get Data
    	// ----------------------
    	return getDataWithLock(redissonDataType, param);
    }
    
    /**
	 * @name deleteData(String key)
	 * @brief Delete String Data
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public boolean deleteData(String key) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.STRING;
    	validateRemoveParam(redissonDataType, null, key, null);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	key += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.KEY.getValue(),					key);
    	
    	// ----------------------
    	// 3. Delete Data
    	// ----------------------
    	return removeData(redissonDataType, param);
    }
    
    /**
	 * @name addToSetWithLock(String setName, String value, Long ttlSec)
	 * @brief Add Set Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    public boolean addToSetWithLock(String setName, String value, Long ttlSec) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.SET;
    	validatePutParam(redissonDataType, setName, null, value);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	setName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, setName, null));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		setName);
    	param.put(RedissonParam.VALUE.getValue(),				value);
    	
    	// ----------------------
    	// 3. Put Data
    	// ----------------------
    	return putDataWithLock(redissonDataType, param, ttlSec);
    }
    
    /**
	 * @name getDataFromSetWithLock(String setName, String value)
	 * @brief Get Set Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    public String getDataFromSetWithLock(String setName, String value) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.SET;
    	validateGetParam(redissonDataType, setName, null, value);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	setName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, setName, null));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		setName);
    	param.put(RedissonParam.VALUE.getValue(),				value);
    	
    	// ----------------------
    	// 3. Put Data
    	// ----------------------
    	return getDataWithLock(redissonDataType, param);
    }
    
    /**
	 * @name removeDataFromSet(String setName, String value)
	 * @brief Remove Set Data
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public boolean removeDataFromSet(String setName, String value) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.SET;
    	validateRemoveParam(redissonDataType, setName, null, value);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	setName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		setName);
    	param.put(RedissonParam.VALUE.getValue(),				value);
    	
    	// ----------------------
    	// 3. Remove Data
    	// ----------------------
    	return removeData(redissonDataType, param);
    }
    
    /**
	 * @name putSetWithLock(String setName, Object set, Long ttlSec)
	 * @brief Put Set
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public boolean putSetWithLock(String setName, Set<String> set, Long ttlSec) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.SET;
    	validatePutCollectionParam(redissonDataType, setName, set);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	setName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, Object> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, setName, ""));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		setName);
    	param.put(RedissonParam.COLLECTION.getValue(),			set);
    	
    	// ----------------------
    	// 3. Put Set
    	// ----------------------
    	return putCollectionWithLock(redissonDataType, param, ttlSec);
    }
    
    /**
	 * @name getSetWithLock(String setName)
	 * @brief Get Set Data
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public Set<String> getSetWithLock(String setName) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.SET;
    	validateGetCollectionParam(redissonDataType, setName);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	setName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, Object> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, setName, ""));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		setName);
    	
    	// ----------------------
    	// 3. Get Set
    	// ----------------------
    	Object collection = getCollectionWithLock(redissonDataType, param);
    	return collection instanceof Set ? (Set<String>) collection: null;
    }
    
    /**
	 * @name removeSet(String setName, String value)
	 * @brief Remove Set Data
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public boolean removeSet(String setName) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.SET;
    	validateRemoveCollectionParam(redissonDataType, setName);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	setName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		setName);
    	
    	// ----------------------
    	// 3. Remove Set
    	// ----------------------
    	return removeCollection(redissonDataType, param);
    }
    
    /**
	 * @name putDataInMapWithLock(String mapName, String key, String value, Long ttlSec)
	 * @brief Put Map Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    public boolean putDataInMapWithLock(String mapName, String key, String value, Long ttlSec) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.MAP;
    	validatePutParam(redissonDataType, mapName, key, value);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	mapName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, mapName, key));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		mapName);
    	param.put(RedissonParam.KEY.getValue(),					key);
    	param.put(RedissonParam.VALUE.getValue(),				value);
    	
    	// ----------------------
    	// 3. Put Data
    	// ----------------------
    	return putDataWithLock(redissonDataType, param, ttlSec);
    }
    
    /**
	 * @name getDataFromMapWithLock(String mapName, String key)
	 * @brief Get Map Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    public String getDataFromMapWithLock(String mapName, String key) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.MAP;
    	validateGetParam(redissonDataType, mapName, key, null);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	mapName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, mapName, key));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		mapName);
    	param.put(RedissonParam.KEY.getValue(),					key);
    	
    	// ----------------------
    	// 3. Get Data
    	// ----------------------
    	return getDataWithLock(redissonDataType, param);
    }
    
    /**
	 * @name removeDataFromMap(String mapName, String key)
	 * @brief Remove Map Data
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public boolean removeDataFromMap(String mapName, String key) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.MAP;
    	validateRemoveParam(redissonDataType, mapName, key, null);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	mapName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		mapName);
    	param.put(RedissonParam.KEY.getValue(),					key);
    	
    	// ----------------------
    	// 3. Remove Data
    	// ----------------------
    	return removeData(redissonDataType, param);
    }
    
    /**
	 * @name putMapWithLock(String mapName, Object map, Long ttlSec)
	 * @brief Put Map
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public boolean putMapWithLock(String mapName, Map<String, String> map, Long ttlSec) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.MAP;
    	validatePutCollectionParam(redissonDataType, mapName, map);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	mapName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, Object> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, mapName, ""));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		mapName);
    	param.put(RedissonParam.COLLECTION.getValue(),			map);
    	
    	// ----------------------
    	// 3. Put Map
    	// ----------------------
    	return putCollectionWithLock(redissonDataType, param, ttlSec);
    }
    
    /**
	 * @name getMapWithLock(String mapName)
	 * @brief Get Map
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public Map<String, String> getMapWithLock(String mapName) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.MAP;
    	validateGetCollectionParam(redissonDataType, mapName);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	mapName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, Object> param = new HashMap<>();
    	param.put(RedissonParam.LOCK_NAME.getValue(),			getLockName(redissonDataType, mapName, ""));
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		mapName);
    	
    	// ----------------------
    	// 3. Get Map
    	// ----------------------
    	Object collection = getCollectionWithLock(redissonDataType, param);
    	return collection instanceof Map ? (Map<String, String>) collection: null;
    }
    
    /**
	 * @name removeMap(String mapName)
	 * @brief Remove Map
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    public boolean removeMap(String mapName) {

    	// ----------------------
    	// 1. Validation
    	// ----------------------
    	final RedissonDataType redissonDataType = RedissonDataType.MAP;
    	validateRemoveCollectionParam(redissonDataType, mapName);
    	
    	// ----------------------
    	// 2. Make Parameters
    	// ----------------------
    	mapName += REDIS_DELIMITER + redissonDataType.getValue();
    	Map<String, String> param = new HashMap<>();
    	param.put(RedissonParam.COLLECTION_NAME.getValue(),		mapName);
    	
    	// ----------------------
    	// 3. Remove Map
    	// ----------------------
    	return removeCollection(redissonDataType, param);
    }
    
    /**
	 * @name putDataWithLock(RedissonDataType redissonDataType, Map<String, String> param, Long ttlSec)
	 * @brief Put Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    private boolean putDataWithLock(RedissonDataType redissonDataType, Map<String, String> param, Long ttlSec) {

    	// ----------------------
    	// 1. Put Data
    	// ----------------------
    	boolean result = false;
        RLock lock = null;
        if (redisLockPut) {
        	lock = redissonClient.getLock(param.get(RedissonParam.LOCK_NAME.getValue())); // Obtain the distributed lock
        }

        try {
            boolean isLocked = true;
            if (redisLockPut) {
            	isLocked = lock.tryLock(redisLockWaitSec, redisLeaseSec, TimeUnit.SECONDS); // Try acquiring the lock
            }
            
            if (isLocked) {
            	Duration ttlDuration = null;
            	if (ttlSec != null) {
            		ttlDuration = Duration.ofSeconds(ttlSec);
            		if (ttlSec <= 0) {
            			log.info("[{}] `ttlSec <= 0` - param : {}", redissonDataType, param);
            		}
            	}
            	RExpirable expirable = null;
            	if (redissonDataType.equals(RedissonDataType.STRING)) {
                    RBucket<String> bucket = redissonClient.getBucket(param.get(RedissonParam.KEY.getValue())); // Get Redis bucket
                    bucket.set(param.get(RedissonParam.VALUE.getValue())); // Set data from Redis bucket
//                    bucket.setAsync(param.get(RedissonParam.VALUE.getValue())); // Set data from Redis bucket
                    expirable = bucket;
            	}
            	else if (redissonDataType.equals(RedissonDataType.SET)) {
                    RSet<String> set = redissonClient.getSet(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Set
                    set.add(param.get(RedissonParam.VALUE.getValue())); // Add data to Redis Set
//                    set.addAsync(param.get(RedissonParam.VALUE.getValue())); // Add data to Redis Set
                    expirable = set;
            	}
            	else if (redissonDataType.equals(RedissonDataType.MAP)) {
                    RMap<String, String> map = redissonClient.getMap(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Map
                    map.put(param.get(RedissonParam.KEY.getValue()), param.get(RedissonParam.VALUE.getValue())); // Put data into Redis Map
//                    map.putAsync(param.get(RedissonParam.KEY.getValue()), param.get(RedissonParam.VALUE.getValue())); // Put data into Redis Map
                    expirable = map;
            	}
            	if (ttlDuration != null && expirable != null) {
            		expirable.expire(ttlDuration);
            	}
            	result = true;
            } else {
                log.error("Could not acquire lock! - `{}` / `{}`", redissonDataType, param);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (redisLockPut) {
            	lock.unlock(); // Release the lock
            }
        }
        return result;
    }
    
    /**
	 * @name getDataWithLock(RedissonDataType redissonDataType, Map<String, String> param)
	 * @brief Get Data
	 * @author Jonas Lim
	 * @date 2023.12.12
     */
    private String getDataWithLock(RedissonDataType redissonDataType, Map<String, String> param) {

    	// ----------------------
    	// 1. Get Data
    	// ----------------------
    	String result = null;
        RLock lock = null;
        if (redisLockGet) {
        	lock = redissonClient.getLock(param.get(RedissonParam.LOCK_NAME.getValue())); // Obtain the distributed lock
        }

        try {
            boolean isLocked = true;
            if (redisLockGet) {
            	isLocked = lock.tryLock(redisLockWaitSec, redisLeaseSec, TimeUnit.SECONDS); // Try acquiring the lock
            }

            if (isLocked) {
            	if (redissonDataType.equals(RedissonDataType.STRING)) {
                    RBucket<String> bucket = redissonClient.getBucket(param.get(RedissonParam.KEY.getValue())); // Get Redis bucket
                    result = bucket.get(); // Get data from Redis bucket
            	} else if (redissonDataType.equals(RedissonDataType.SET)) {

                    RSet<String> set = redissonClient.getSet(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Set
                    boolean isPresent = false;
                    if (set != null && set.isExists() && !set.isEmpty()) {
                        isPresent = set.contains(param.get(RedissonParam.VALUE.getValue())); // Check if value exists in Redis Set
                    }
                    if (isPresent) {
                    	result = param.get(RedissonParam.VALUE.getValue());	// if there's a value, return the value
                    }
            	} else if (redissonDataType.equals(RedissonDataType.MAP)) {
                    RMap<String, String> map = redissonClient.getMap(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Map
                    result = map.get(param.get(RedissonParam.KEY.getValue())); // Get data from Redis Map
            	}
                return result;
            } else {
                log.error("Could not acquire lock! - `{}` / `{}`", redissonDataType, param);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (redisLockGet) {
            	lock.unlock(); // Release the lock
            }
        }
        return result;
    }
    
    /**
	 * @name removeData(RedissonDataType redissonDataType, Map<String, String> param)
	 * @brief Remove Data
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    private boolean removeData(RedissonDataType redissonDataType, Map<String, String> param) {

    	// ----------------------
    	// 1. Delete Data
    	// ----------------------
    	boolean result = false;
//        RLock lock = null;
//        if (redisLockRemove) {
//        	lock = redissonClient.getLock(param.get(RedissonParam.LOCK_NAME.getValue())); // Obtain the distributed lock
//        }

//        try {
//            boolean isLocked = true;
//            if (redisLockRemove) {
//            	isLocked = lock.tryLock(redisLockWaitSec, redisLeaseSec, TimeUnit.SECONDS); // Try acquiring the lock
//            }
            
//            if (isLocked) {
//            	Duration ttlDuration = null;
//            	if (ttlSec != null) {
//            		ttlDuration = Duration.ofSeconds(ttlSec);
//            		if (ttlSec <= 0) {
//            			log.info("[{}] `ttlSec <= 0` - param : {}", redissonDataType, param);
//            		}
//            	}
            	if (redissonDataType.equals(RedissonDataType.STRING)) {
                    RBucket<String> bucket = redissonClient.getBucket(param.get(RedissonParam.KEY.getValue())); // Get Redis bucket
                    bucket.delete(); // Delete data from Redis bucket
//                    bucket.deleteAsync(); // Delete data from Redis bucket
            	}
            	else if (redissonDataType.equals(RedissonDataType.SET)) {
                    RSet<String> set = redissonClient.getSet(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Set
                    set.remove(param.get(RedissonParam.VALUE.getValue())); // Remove data from Redis Set
//                    set.removeAsync(param.get(RedissonParam.VALUE.getValue())); // Remove data from Redis Set
            	}
            	else if (redissonDataType.equals(RedissonDataType.MAP)) {
                    RMap<String, String> map = redissonClient.getMap(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Map
                    map.remove(param.get(RedissonParam.KEY.getValue())); // Remove data from Redis Map
//                    map.removeAsync(param.get(RedissonParam.KEY.getValue())); // Remove data from Redis Map
            	}
            	result = true;
//            } else {
//                log.error("Could not acquire lock! - `{}` / `{}`", redissonDataType, param);
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        } finally {
//            if (redisLockRemove) {
//            	lock.unlock(); // Release the lock
//            }
//        }
        return result;
    }
    
    /**
	 * @name putCollectionWithLock(RedissonDataType redissonDataType, Map<String, Object> param, Long ttlSec)
	 * @brief Put Collection
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    private boolean putCollectionWithLock(RedissonDataType redissonDataType, Map<String, Object> param, Long ttlSec) {

    	// ----------------------
    	// 0. Validation
    	// ----------------------
    	if (!redissonDataType.equals(RedissonDataType.SET) && !redissonDataType.equals(RedissonDataType.MAP)) {
    		return false;
    	}

    	// ----------------------
    	// 1. Put Data
    	// ----------------------
    	boolean result = false;
        RLock lock = null;
        if (redisLockPut) {
        	lock = redissonClient.getLock((String) param.get(RedissonParam.LOCK_NAME.getValue())); // Obtain the distributed lock
        }

        try {
            boolean isLocked = true;
            if (redisLockPut) {
            	isLocked = lock.tryLock(redisLockWaitSec, redisLeaseSec, TimeUnit.SECONDS); // Try acquiring the lock
            }
            
            if (isLocked) {
            	Duration ttlDuration = null;
            	if (ttlSec != null) {
            		ttlDuration = Duration.ofSeconds(ttlSec);
            		if (ttlSec <= 0) {
            			log.info("[{}] `ttlSec <= 0` - param : {}", redissonDataType, param);
            		}
            	}
            	RExpirable expirable = null;
            	if (redissonDataType.equals(RedissonDataType.SET)) {
                    RSet<String> set = redissonClient.getSet((String) param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Set
                    set.addAll((Set<String>) param.get(RedissonParam.COLLECTION.getValue())); // Put Set
//                    set.addAllAsync((Set<String>) param.get(RedissonParam.COLLECTION.getValue())); // Put Set
                    expirable = set;
            	}
            	else if (redissonDataType.equals(RedissonDataType.MAP)) {
                    RMap<String, String> map = redissonClient.getMap((String) param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Map
                    map.putAll((Map<String, String>) param.get(RedissonParam.COLLECTION.getValue())); // Put Map
//                    map.putAllAsync((Map<String, String>) param.get(RedissonParam.COLLECTION_NAME.getValue())); // Put Map
                    expirable = map;
            	}
            	if (ttlDuration != null && expirable != null) {
            		expirable.expire(ttlDuration);
            	}
            	result = true;
            } else {
                log.error("Could not acquire lock! - `{}` / `{}`", redissonDataType, param);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (redisLockPut) {
            	lock.unlock(); // Release the lock
            }
        }
        return result;
    }
    
    /**
	 * @name getCollectionWithLock(RedissonDataType redissonDataType, Map<String, String> param)
	 * @brief Get Collection
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    private Object getCollectionWithLock(RedissonDataType redissonDataType, Map<String, Object> param) {

    	// ----------------------
    	// 0. Validation
    	// ----------------------
    	if (!redissonDataType.equals(RedissonDataType.SET) && !redissonDataType.equals(RedissonDataType.MAP)) {
    		return null;
    	}

    	// ----------------------
    	// 1. Get Data
    	// ----------------------
    	Object result = null;
        RLock lock = null;
        if (redisLockGet) {
        	lock = redissonClient.getLock((String) param.get(RedissonParam.LOCK_NAME.getValue())); // Obtain the distributed lock
        }

        try {
            boolean isLocked = true;
            if (redisLockGet) {
            	isLocked = lock.tryLock(redisLockWaitSec, redisLeaseSec, TimeUnit.SECONDS); // Try acquiring the lock
            }

            if (isLocked) {
            	if (redissonDataType.equals(RedissonDataType.SET)) {
                    RSet<String> set = redissonClient.getSet((String) param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Set
                    result = set.readAll(); // Get Set
//                    result = set.readAllAsync(); // Get Set
            	} else if (redissonDataType.equals(RedissonDataType.MAP)) {
                    RMap<String, String> map = redissonClient.getMap((String) param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Map
                    result = map.readAllMap(); // Get Map
//                    result = map.readAllMapAsync(); // Get Map
            	}
                return result;
            } else {
                log.error("Could not acquire lock! - `{}` / `{}`", redissonDataType, param);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (redisLockGet) {
            	lock.unlock(); // Release the lock
            }
        }
        return result;
    }
    
    /**
	 * @name removeCollection(RedissonDataType redissonDataType, Map<String, String> param)
	 * @brief Remove Collection
	 * @author Jonas Lim
	 * @date 2024.02.02
     */
    private boolean removeCollection(RedissonDataType redissonDataType, Map<String, String> param) {

    	// ----------------------
    	// 0. Validation
    	// ----------------------
    	if (!redissonDataType.equals(RedissonDataType.SET) && !redissonDataType.equals(RedissonDataType.MAP)) {
    		return false;
    	}

    	// ----------------------
    	// 1. Delete Data
    	// ----------------------
    	boolean result = false;
//        RLock lock = null;
//        if (redisLockRemove) {
//        	lock = redissonClient.getLock(param.get(RedissonParam.LOCK_NAME.getValue())); // Obtain the distributed lock
//        }

//        try {
//            boolean isLocked = true;
//            if (redisLockRemove) {
//            	isLocked = lock.tryLock(redisLockWaitSec, redisLeaseSec, TimeUnit.SECONDS); // Try acquiring the lock
//            }
            
//            if (isLocked) {
//            	Duration ttlDuration = null;
//            	if (ttlSec != null) {
//            		ttlDuration = Duration.ofSeconds(ttlSec);
//            		if (ttlSec <= 0) {
//            			log.info("[{}] `ttlSec <= 0` - param : {}", redissonDataType, param);
//            		}
//            	}
            	if (redissonDataType.equals(RedissonDataType.SET)) {
                    RSet<String> set = redissonClient.getSet(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Set
                    set.delete(); // Remove Set
//                    set.deleteAsync(); // Remove Set
            	}
            	else if (redissonDataType.equals(RedissonDataType.MAP)) {
                    RMap<String, String> map = redissonClient.getMap(param.get(RedissonParam.COLLECTION_NAME.getValue())); // Get Redis Map
                    map.delete(); // Remove Map
//                    map.deleteAsync(); // Remove Map
            	}
            	result = true;
//            } else {
//                log.error("Could not acquire lock! - `{}` / `{}`", redissonDataType, param);
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        } finally {
//            if (redisLockRemove) {
//            	lock.unlock(); // Release the lock
//            }
//        }
        return result;
    }
}

