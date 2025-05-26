package com.jonastalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
@EnableAsync
@ComponentScan(value={"com.jonastalk"})
public class JonastalkChatApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JonastalkChatApiApplication.class, args);
	}

}
