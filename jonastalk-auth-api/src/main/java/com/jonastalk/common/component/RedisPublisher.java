package com.jonastalk.common.component;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * @name RedisPublisher.java
 * @brief Redis Publisher
 * @author Jonas Lim
 * @date June 3, 2025
 */
@Component
public class RedisPublisher {

	@Lazy
    @Autowired
    private RedissonClient redissonClient;

    public void publish(String channel, String message) {
        RTopic topic = redissonClient.getTopic(channel);
        topic.publish(message);
    }
}