package com.aditi_final.bridgeskill_api.config;

import com.aditi_final.bridgeskill_api.security.CustomAccessDeniedHandler;
import com.aditi_final.bridgeskill_api.security.CustomAuthenticationEntryPoint;
import com.aditi_final.bridgeskill_api.security.CustomUserDetailsService;
import com.aditi_final.bridgeskill_api.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomUserDetailsService customUserDetailsService,
                          CustomAccessDeniedHandler customAccessDeniedHandler,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.customUserDetailsService = customUserDetailsService;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // public auth endpoints
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        .requestMatchers("/error").permitAll()

                        // public job browsing
                        .requestMatchers(HttpMethod.GET, "/api/jobs", "/api/jobs/*").permitAll()

                        // admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // student endpoints
                        .requestMatchers("/api/student/**").hasRole("STUDENT")

                        // client endpoints
                        .requestMatchers("/api/client/**").hasRole("CLIENT")

                        // job management
                        .requestMatchers(HttpMethod.POST, "/api/jobs").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.PUT, "/api/jobs/*").hasRole("CLIENT")
                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/*").hasRole("CLIENT")

                        // applications
                        .requestMatchers(HttpMethod.POST, "/api/applications").hasRole("STUDENT")
                        .requestMatchers(HttpMethod.GET, "/api/jobs/*/applications").hasAnyRole("CLIENT", "ADMIN")

                        // everything else must be authenticated
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
// Main goal of this file
//Main goal of file 2
//
//This file tells Spring Security:
//
//which routes are public
//which routes need login
//which role can access which endpoint
//what to do for 401 and 403
//use JWT instead of session login
//
//So this file answers:
//
//        “Which role can enter which API?”