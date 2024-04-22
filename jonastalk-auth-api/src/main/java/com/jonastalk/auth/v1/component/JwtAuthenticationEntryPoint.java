package com.jonastalk.auth.v1.component;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

/**
 * @name JwtAuthenticationEntryPoint.java
 * @brief Process After Failure Authentication
 * @process Process After Failure Authentication
 * @author Jonas Lim
 * @date 2023.11.27
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -7858869558953243875L;

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        // This method is called when a user tries to access a secured REST resource without proper authentication
        // You can customize the response sent back to the user in case of authentication failure

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
