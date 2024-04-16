package com.jonastalk.common.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @name RedissonConfig.java
 * @brief Redisson Configuration
 * @author Jonas Lim
 * @date 2023.12.12
 */
@Configuration
public class RedissonConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;
    
//    @Value("${spring.redis.password}")
//    private String password;
    
    @Value("${spring.redis.client-name}")
    private String clientName;
    
    @Value("${spring.redis.netty-threads}")
    private int nettyThreads;
    
    @Value("${spring.redis.idle-connection-timeout}")
    private int idleConnectionTimeout;
    
    @Value("${spring.redis.connect-timeout}")
    private int connectTimeout;
    
    @Value("${spring.redis.timeout}")
    private int timeout;
    
    @Value("${spring.redis.retry-attempts}")
    private int retryAttempts;
    
    @Value("${spring.redis.retry-interval}")
    private int retryInterval;
    
    @Value("${spring.redis.subscriptions-per-connection}")
    private int subscriptionsPerConnection;
    
    @Value("${spring.redis.subscription-connection-minimum-idle-size}")
    private int subscriptionConnectionMinimumIdleSize;
    
    @Value("${spring.redis.subscription-connection-pool-size}")
    private int subscriptionConnectionPoolSize;
    
    @Value("${spring.redis.connection-minimum-idle-size}")
    private int connectionMinimumIdleSize;
    
    @Value("${spring.redis.connection-pool-size}")
    private int connectionPoolSize;
    
    @Value("${spring.redis.database}")
    private int database;
    
    @Value("${spring.redis.dns-monitoring-interval}")
    private int dnsMonitoringInterval;
    
    @Value("${spring.redis.keep-alive}")
    private boolean keepAlive;
    
    @Value("${spring.redis.thread-cnt}")
    private int threadCnt;

    private static final String REDISSON_HOST_PREFIX = "redis://";

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config
//        	.setCodec(new StringCodec())
          	.setCodec(StringCodec.INSTANCE)
            .setNettyThreads(nettyThreads)
        	.useSingleServer()
        	.setAddress(REDISSON_HOST_PREFIX + host + ":" + port) // Set Redis server address
//        	.setPassword(password)
        	.setClientName(clientName)
        	.setIdleConnectionTimeout(idleConnectionTimeout)
        	.setConnectTimeout(connectTimeout)
        	.setTimeout(timeout)
        	.setRetryAttempts(retryAttempts)
        	.setRetryInterval(retryInterval)
        	.setSubscriptionsPerConnection(subscriptionsPerConnection)
        	.setSubscriptionConnectionMinimumIdleSize(subscriptionConnectionMinimumIdleSize)
        	.setSubscriptionConnectionPoolSize(subscriptionConnectionPoolSize)
        	.setConnectionMinimumIdleSize(connectionMinimumIdleSize)
        	.setConnectionPoolSize(connectionPoolSize)
        	.setDatabase(database)
        	.setDnsMonitoringInterval(dnsMonitoringInterval)
//        	.setDnsMonitoringInterval(-1) // Eable When DNS Check Error
            .setConnectionPoolSize(100) // Set connection pool size
            .setConnectionMinimumIdleSize(10) // Set minimum idle connections
            .setKeepAlive(keepAlive)
        	;
        
		config.setThreads(threadCnt); // Number of threads for Redisson executor
        
        return Redisson.create(config);
    }
}
