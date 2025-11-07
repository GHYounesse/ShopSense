package com.app.shopsense.services;


import com.app.shopsense.doas.repositories.user.UserRepository;
import com.app.shopsense.doas.entities.user.User;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Security service for authorization checks in the application
 */
@Service("securityService")
public class SecurityService {


    private static final Logger log = org.slf4j.LoggerFactory.getLogger(SecurityService.class);
    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isUserOrAdmin(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Check if user is authenticated
        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            log.debug("Access denied: User is not authenticated");
            return false;
        }

        // Check if user has ADMIN role
        boolean isAdmin = authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isAdmin) {
            log.debug("Access granted: User is ADMIN");
            return true;
        }

        // Check if the authenticated user matches the requested userId
        String emailUser = authentication.getName();
        User user = userRepository.findByEmail(emailUser)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Long authenticatedUserId=user.getId();
        System.out.println("authentificatedUserid: " + authenticatedUserId+" userId :"+userId);

        boolean isOwner = authenticatedUserId != null
                && authenticatedUserId.equals(userId);

        if (isOwner) {
            log.debug("Access granted: User {} is accessing their own resource", authenticatedUserId);
        } else {
            log.warn("Access denied: User {} attempted to access resource for user {}",
                    authenticatedUserId, userId);
        }

        return isOwner;
    }

    /**
     * Get the current authenticated user ID
     *
     * @return Current user ID or null if not authenticated
     */
    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        try {
            return Long.parseLong(authentication.getName());
        } catch (NumberFormatException e) {
            log.error("Failed to parse user ID from authentication: {}", authentication.getName());
            return null;
        }
    }

    /**
     * Check if current user has ADMIN role
     *
     * @return true if user has ADMIN role, false otherwise
     */
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        return authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }

    /**
     * Check if current user has a specific role
     *
     * @param role The role to check (e.g., "ADMIN", "USER")
     * @return true if user has the role, false otherwise
     */
    public boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        String roleWithPrefix = role.startsWith("ROLE_") ? role : "ROLE_" + role;

        return authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority(roleWithPrefix));
    }

    /**
     * Get the username of the currently authenticated user
     *
     * @return Username or null if not authenticated
     */
    public String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        return authentication.getName();
    }
}
