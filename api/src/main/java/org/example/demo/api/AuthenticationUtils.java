package org.example.demo.api;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public interface AuthenticationUtils {

    static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        return getCurrentUserId(authentication);
    }

    static Long getCurrentUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof Jwt) {
            Jwt jwt = (Jwt) principal;
            Number userId = (Number) jwt.getClaims().get("userId");
            if (userId == null) {
                return null;
            }
            return userId.longValue();
        }
        return null;
    }

}
