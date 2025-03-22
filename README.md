# Jonastalk

## Author
- **KyungTaek Lim (Jonas Lim)**
- **Software Engineer**
- **Email:** kyungtaekjonaslim@gmail.com
- **LinkedIn:** [KyungTaek Jonas Lim](https://www.linkedin.com/in/kyungtaek-jonas-lim)
- **GitHub:** [kyungtaek-jonas-lim](https://github.com/kyungtaek-jonas-lim)

## Description
- Web Chat Application
- MSA
- Created at 2024.04.16
- System Design
	- ![System Design Diagram](https://raw.githubusercontent.com/kyungtaek-jonas-lim/jonastalk/main/system_design_jonastalk.png)

## Reference
- JAVA 11
- Spring Cloud
- Maven Project
- Ports
	- `jonastalk-api-gw`: 8443
	- `jonastalk-chat-api`: 8001
	- `jonastalk-auth-api`: 8002
	- `jonastalk-api-discovery`: 8999

## Installation
Follow these instructions to set up your development environment.

1. **Clone the repository:**

   ```bash
   git clone https://github.com/kyungtaek-jonas-lim/jonastalk.git
   cd jonastalk
   ```

2. **Setup Environment:**
	- Install Library
		```bash
		cd jonastalk-api-discovery
		./mvnw clean install
		./mvnw spring-boot:run

		cd jonastalk-api-gw
		./mvnw clean install
		./mvnw spring-boot:run

		cd jonastalk-auth-api
		./mvnw clean install
		./mvnw spring-boot:run

		cd jonastalk-chat-api
		./mvnw clean install
		./mvnw spring-boot:run
		```
	- Postman Import
		- `./Jonastalk.postman_collection.json`

## Library
- `jonastalk-auth-api`
	- Authentication & Authorization
	- Spring Security
	- JWT(Stateless) -> Keycloak(ongoing)
	- Encryption (RSA, Jasypt)
	- AOP (Auto Validation)
	- Log4j2
	- MariaDB (JPA, Hikari)
	- Redis (Redisson)
	- Lombok
	- Feign Client
	- Swagger

- `jonastalk-chat-api`
	- `Kafka` for reliable message delivery
	<!-- - `Redis` Pub/Sub for real-time notifications. -->

## APIs
	- 