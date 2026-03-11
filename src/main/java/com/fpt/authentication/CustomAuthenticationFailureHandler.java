package com.fpt.authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {

        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "Username and password is incorrect.";

        response.setStatus(status);
        response.setContentType("application/json; charset=UTF-8");

        Map<String, Object> error = new HashMap<>();
        error.put("code", status);
        error.put("message", message);

        response.getWriter().write(objectMapper.writeValueAsString(error));
    }
}
