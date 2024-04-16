package com.jonastalk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableCaching
@EnableDiscoveryClient
@EnableScheduling
@EnableAsync
@ComponentScan(value={"com.jonastalk"})
public class JonastalkAuthApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(JonastalkAuthApiApplication.class, args);
	}

}
