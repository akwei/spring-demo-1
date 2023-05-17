package org.example.demo.api;

import org.springframework.stereotype.Component;

@Component
public class UserAuthMgr {

    public boolean checkAuthedUser() {
        Long currentUserId = AuthenticationUtils.getCurrentUserId();
        return currentUserId != null;
    }

}