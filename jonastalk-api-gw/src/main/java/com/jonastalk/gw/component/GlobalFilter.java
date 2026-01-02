package com.jonastalk.gw.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    private final WebClient webClient;
    private final List<String> authServiceBaseUrls = new ArrayList<>();

    public GlobalFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClient = webClientBuilder.baseUrl("http://localhost:8002/").build(); // Auth Service Base Url
        
		/**
		 * @TODO In Config Files
		 */
        authServiceBaseUrls.add("http://localhost:8002/");
        authServiceBaseUrls.add("http://localhost:8443/auth");
    }
    
    @Override
    public GatewayFilter apply(Config config) {
    	

        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String requestUrl = request.getURI().toString();

            // -------------------------
            // 1. Authorization Validation Exception
            boolean skipAuthorization = false;
            for (String authServiceBaseUrl: authServiceBaseUrls) {
            	if (requestUrl.startsWith(authServiceBaseUrl)) {
            		skipAuthorization = true;
            		break;
            	}
            }
            if (skipAuthorization) {
                log.info("Skipping authorization for request: {}", requestUrl);
                log.info("Global Filter Message: {}", config.getMessage());
                if (config.isShowPreLogger()) {
                    log.info("Global Filter Start: request id -> {}", request.getId());
                }
                return chain.filter(exchange).then(Mono.fromRunnable(() -> {
                    if (config.isShowPostLogger()) {
                        log.info("Global Filter End: response code -> {}", response.getStatusCode());
                    }
                }));
            }

            // -------------------------
            // 3. Authorization Validation
            String upgradeHeader = request.getHeaders().getFirst("Upgrade");
            boolean isWebSocket = "websocket".equalsIgnoreCase(upgradeHeader);
            
            String token = null;
            if (isWebSocket) { // For WebSocket, Get Access Token from Query String
                MultiValueMap<String, String> queryParams = request.getQueryParams();
                token = queryParams.getFirst("accessToken");
                if (token == null || token.isEmpty()) {
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
            } else { // Get Access Token from Header
                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    log.warn("Missing or invalid Authorization header");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return response.setComplete();
                }
                token = authHeader.substring(7);
            }
            
            // Request Body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("common", new HashMap<>());
            Map<String, Object> data = new HashMap<>();
            data.put("accessToken", token);
            requestBody.put("data", data);

            return webClient.post()
                .uri("/v1/token/access/validate") // Token Validate API uri
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatus::isError, r -> {
                    log.warn("Invalid token response from Auth service");
                    return Mono.error(new RuntimeException("Invalid token"));
                })
                .bodyToMono(ValidateResponse.class)
                .flatMap(responsePayload -> {
                    ValidateResponse.ResponseData responseData = responsePayload.getData();
                    if (responseData == null || !responseData.isValidation()) {
                        return Mono.error(new RuntimeException("Invalid token"));
                    }
                    String username = responseData.getUsername();
                    String roles = String.join(",", responseData.getRoles());

                    ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Id", username)
                        .header("X-User-Roles", roles)
                        .build();

                    ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(mutatedRequest)
                        .build();

                    return chain.filter(mutatedExchange);
                });
        };
    }

    @Data
    public static class Config {
        private String message;
        private boolean showPreLogger;
        private boolean showPostLogger;
    }

    @Data
    public static class ValidateResponse {
        private Common common;
        
        @JsonProperty("data")
        private ResponseData data;

        @Data
        public static class Common {
            private int status;
            private String currentDatetime;
        }

        @Data
        public static class ResponseData {
            private List<String> roles;
            private boolean validation;
            private String username;
        }
    }
}