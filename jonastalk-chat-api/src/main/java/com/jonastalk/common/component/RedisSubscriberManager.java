package com.jonastalk.common.component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
public class RedisSubscriberManager {
	
    /**
	 * @name RedisPubSubChannel
	 * @brief Redis Pub/Sub Channel
	 * @author Jonas Lim
	 * @date June 3, 2025
     */
    @Getter
    public enum RedisPubSubChannel {
    	CHAT					("chat:")
    	;
    	private String value;
    	private RedisPubSubChannel(String value) {
    		this.value = value;
    	}
    }

	@Lazy
    @Autowired
    private RedissonClient redissonClient;

    private final Map<String, Integer> listenerIds = new ConcurrentHashMap<>();

    public boolean subscribe(String channel, MessageListener<String> listener) {
        RTopic topic = redissonClient.getTopic(channel);
        int listenerId = topic.addListener(String.class, listener);
        listenerIds.put(channel, listenerId);
        return true;
    }

    public boolean unsubscribe(String channel) {
        RTopic topic = redissonClient.getTopic(channel);
        Integer listenerId = listenerIds.get(channel);
        if (listenerId != null) {
        	listenerIds.remove(channel);
            topic.removeListener(listenerId);
        }
        return true;
    }

    public Set<String> getChannels() {
    	return listenerIds.keySet();
    }
}