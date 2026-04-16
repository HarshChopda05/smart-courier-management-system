package com.example.courier.management.Security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        log.info("incoming request: {}" + request.getRequestURI());

        try {

            String requestURI = request.getRequestURI();
            //Skip public endpoints
            if (requestURI.startsWith("/courier/auth" )|| requestURI.startsWith("/public")){
                filterChain.doFilter(request, response);
                return;
            }

            //Authorization
            final String requestTokenHeader = request.getHeader("Authorization");
            //Bearer 2352345235sdfrsfgsdfsdf
            log.info("Request Header Token: " + requestTokenHeader);

            String username = null;
            String token = null;

            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                //Fetching original token after header

                token = requestTokenHeader.substring(7); //After Bearer, is token
                try {
                    username = jwtHelper.getUsernameFromToken(token);

                } catch (IllegalArgumentException e) {
                    throw new RuntimeException("Unable to get JWT Token!");
                } catch (ExpiredJwtException e) {
                    throw new RuntimeException("JWT token has expired");
                } catch (MalformedJwtException e) {
                    throw new RuntimeException("Invalid JWT Token");
                }
            }

            // If username found and no authentication yet
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //fetch userdetails from user name
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtHelper.validateToken(token, userDetails)) {
                    //set the authentication
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                    System.out.println("Authorities from JWT: " + userDetails.getAuthorities());
                } else {
                    throw new RuntimeException("Invalid JWT Token");
                }
            }
            filterChain.doFilter(request, response);
        }catch (Exception e){

            //send exception to GlobalExceptionHandler
            handlerExceptionResolver.resolveException(request, response, null,e);
        }
    }
}
