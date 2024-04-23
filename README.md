# Jonastalk

## Author
	> KyungTaek Lim (Jonas Lim)
	> Software Engineer
	> https://www.linkedin.com/in/kyungtaek-jonas-lim-software-engineer

## Description
	> Web Chat Application
	> MSA
	> Created at 2024.04.16

## Reference
	> JAVA 11
	> Spring Cloud
	> Maven Project
	> Port : 8443

## Last Update
	[2024-04-23]  auth - API, Feign Client
		> jonastalk-api-gw - v.0.0.1
		> jonastalk-discovery - v.0.0.1
		> jonastalk-auth-api - v.0.0.6 - API, Feign Client
			** API
				- /v1/account/username/exist
				- /v1/token/access/generate
				- /v1/token/access/refresh
				- /v1/token/access/validate
				- /v1/key/rsa/generate
				- /v1/key/rsa/read
				- /test/chatTest
		> jonastalk-chat-api - v.0.0.1


## jonastalk-auth-api
	> Authentication & Authorization
	> Spring Security
	> JWT(Stateless) -> Keycloak(ongoing)
	> Encryption (RSA, Jasypt)
	> AOP (Auto Validation)
	> Log4j2
	> MariaDB (JPA, Hikari)
	> Redis (Redisson)
	> Lombok
	> Feign Client
	> Swagger

## jonastalk-chat-api
	> Chat