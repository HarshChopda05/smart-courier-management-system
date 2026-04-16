package com.example.courier.management.Exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        try {
            APIError error = APIError.builder()
                    .timeStamp(LocalDateTime.now())
                    .status(403)
                    .error("Forbidden")
                    .message("You don’t have permission to access this resource")
                    .build();

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);

            new ObjectMapper().writeValue(response.getOutputStream(), error);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}