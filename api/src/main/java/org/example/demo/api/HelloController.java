package org.example.demo.api;


import org.example.demo.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {
    @Resource
    private UserService userService;

    @PreAuthorize("@userAuthMgr.checkAuthedUser()")
    @RequestMapping(value = "/pub/hello", produces = {"text/plain"}, method = RequestMethod.GET)
    public ResponseEntity<String> hello() {
        String result = this.userService.hello("akwei");
        return ResponseEntity.ok(result);
    }
}
