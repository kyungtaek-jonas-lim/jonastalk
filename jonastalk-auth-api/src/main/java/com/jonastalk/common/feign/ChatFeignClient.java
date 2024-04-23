package com.jonastalk.common.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "jonastalk-chat-api", url = "http://localhost:8001")
public interface ChatFeignClient {
    
    @PostMapping("/common/test")
    ResponseEntity<String> chatTest();
    
}
