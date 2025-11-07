package com.app.shopsense.security.jwt;

import com.app.shopsense.services.user.UserService;
import com.app.shopsense.services.token.TokenBlacklistService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
/**
 * 🔒 JWT Authentication Filter
 * - Extracts and validates JWT tokens from incoming requests.
 * - Checks blacklist and token expiration.
 * - Sets authentication in the SecurityContext for authorized users.
 *
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final TokenBlacklistService blacklistService;

    public JwtAuthFilter(JwtUtil jwtUtil,
                                   UserService userService,
                                   TokenBlacklistService blacklistService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        this.blacklistService = blacklistService;
    }


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Extract token from Authorization header
            String token = extractTokenFromRequest(request);

            if (token != null) {
                //Validate token structure & extract claims
                String username = jwtUtil.getUsernameFromToken(token);
                String jti = jwtUtil.getJtiFromToken(token);
                List<String> roles=jwtUtil.getRolesFromToken(token);
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                //Check if token is blacklisted
                if (blacklistService.isBlacklisted(jti)) {
                    log.warn("Attempted to use blacklisted token: {}", jti);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\":\"Token has been revoked\"}");
                    return;
                }

                //Validate user authentication
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = userService.loadUserByUsername(username);

                    // Check if all tokens revoked (password change, etc.)
                    var user = userService.getUserByEmail(username);
                    if (blacklistService.areAllUserTokensBlacklisted(user.getId())) {
                        log.warn("Attempted to use token for user with all tokens revoked: {}", username);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write("{\"error\":\"All tokens revoked. Please login again.\"}");
                        return;
                    }

                    // Validate token signature and expiration
                    if (jwtUtil.validateToken(token, username)) {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        authorities
                                );
                        authentication.setDetails(
                                new WebAuthenticationDetailsSource().buildDetails(request)
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        log.debug("JWT authentication successful for user: {}", username);
                    } else {
                        log.warn("JWT validation failed for user: {}", username);
                    }
                }
            }
        // Handle validation errors
        } catch (JwtUtil.JwtValidationException e) {
            log.error("JWT validation error: {}", e.getMessage());
            // Don't set authentication, let it proceed as unauthenticated
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);

    }
    /**
     * Extracts Bearer token from the Authorization header.
     */
    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
