package com.jonastalk.common.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "jonastalk-auth-api", url = "http://localhost:8002")
public interface AuthFeignClient {
    
    @PostMapping("/common/test")
    ResponseEntity<String> chatTest();
    
    
    @PostMapping("/v1/token/access/validate")
    ResponseEntity<?> validateAccessToken(@RequestBody Map<String, Object> param);

}
