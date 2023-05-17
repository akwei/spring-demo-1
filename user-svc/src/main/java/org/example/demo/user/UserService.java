package org.example.demo.user;

import org.springframework.stereotype.Component;

@Component
public class UserService {

    public String hello(String name) {
        return "hello " + name;
    }
}
