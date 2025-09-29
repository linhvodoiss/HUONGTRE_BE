package com.fpt.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpt.form.OtpLoginRequest;
import com.fpt.entity.User;
import com.fpt.service.IUserService;
import com.fpt.service.JWTTokenService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JWTOtpAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final IUserService userService;

    public JWTOtpAuthenticationFilter(String url,
                                      AuthenticationManager authManager,
                                      IUserService userService,
                                      AuthenticationFailureHandler failureHandler) {
        super(new AntPathRequestMatcher(url));
        setAuthenticationManager(authManager);
        this.userService = userService;
        setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            OtpLoginRequest otpRequest = objectMapper.readValue(request.getInputStream(), OtpLoginRequest.class);

            boolean valid = userService.validateOtp(otpRequest.getPhoneNumber(), otpRequest.getOtp());
            if (!valid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"OTP không hợp lệ hoặc đã hết hạn\"}");
                response.getWriter().flush();
                return null;
            }

            return new UsernamePasswordAuthenticationToken(
                    otpRequest.getPhoneNumber(),
                    null,
                    Collections.emptyList()
            );

        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid JSON format or missing fields\"}");
            response.getWriter().flush();
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult)
            throws IOException, ServletException {

        User user = userService.findUserByPhoneNumber(authResult.getName());
        JWTTokenService.addJWTTokenAndUserInfoToBody(response, user);
    }
}
