package com.example.courier.management.Configuration;

import com.example.courier.management.Security.JwtAuthenticationEntryPoint;
import com.example.courier.management.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.example.courier.management.Models.Type.RoleType.*;
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        //Public
                        .requestMatchers("/courier/auth/**", "/public/**").permitAll()

                        //ADMIN (Role-based)
                        .requestMatchers("/courier/admin/**").hasRole(ADMIN.name())
                        //MANAGER
                        .requestMatchers("/courier/manager/**").hasAnyRole(MANAGER.name(), ADMIN.name())

                        //AGENT
                        .requestMatchers("/courier/agent/**").hasAnyRole(AGENT.name(), ADMIN.name())

                        //CUSTOMER + PERMISSIONS
                        .requestMatchers("/courier/customer/orders/my/**")
                        .hasAuthority("VIEW_OWN_ORDERS")

                        .requestMatchers("/courier/customer/orders/**")
                        .hasAuthority("PLACE_ORDER")

                        .requestMatchers("/courier/customer/tracking/**")
                        .hasAuthority("TRACK_PACKAGE")

                        //fallback
                        .anyRequest().authenticated()
                )

                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(((request, response, accessDeniedException) -> {
                            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException); //This would be handles Global Exceptions
                        }))
                )
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

               // .formLogin(Customizer.withDefaults()); //Provie byDefault Login page for Login
        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }

}
