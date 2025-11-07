package com.app.shopsense.configs;



import com.app.shopsense.security.jwt.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity //Allows @PreAuthorize/@Secured at method level
@EnableJpaAuditing
public class SecurityConfig {

    private final JwtAuthFilter jwtFilter;

    public SecurityConfig(JwtAuthFilter jwtFilter) { this.jwtFilter = jwtFilter; }

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/v1/auth/**",
            "/api/v1/account/verify-email-change",
            "/api/v1/content/public/**"
    };
    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    @Bean
    public SecurityFilterChain filterChain(org.springframework.security.config.annotation.web.builders.HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                                .contentTypeOptions(Customizer.withDefaults())
                                .frameOptions(frame -> frame.deny())
                                .httpStrictTransportSecurity(hsts -> hsts.disable())
                                .contentSecurityPolicy(csp -> csp
                                                .policyDirectives(
                                                        "default-src 'self' http://localhost:4200; " +
                                                                "script-src 'self' http://localhost:4200 'unsafe-inline' 'unsafe-eval'; " +
                                                                "style-src 'self' http://localhost:4200 'unsafe-inline'; " +
                                                                "connect-src 'self' http://localhost:4200 http://localhost:8080; " +
                                                                "img-src 'self' data: http://localhost:4200;"
                                                )
                                )
                                .referrerPolicy(referrer -> referrer
                                        .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.NO_REFERRER)
                                )
                                .addHeaderWriter((request, response) ->
                                        response.setHeader("Permissions-Policy",
                                                "geolocation=(), microphone=(), camera=(), fullscreen=(self)")
                                )
                                .cacheControl(Customizer.withDefaults())
                );



        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList(allowedOrigins));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "X-XSRF-TOKEN", "Accept"
        ));
        config.setAllowCredentials(true);
        config.setMaxAge(Duration.ofHours(1));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);
        return source;
    }

}
