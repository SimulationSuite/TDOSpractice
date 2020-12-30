package org.tdos.tdospractice.controller;

import org.springframework.beans.factory.annotation.Autowired;



import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tdos.tdospractice.service.Impl.UserServiceImpl;
import org.tdos.tdospractice.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(value = "/test")
    public void test() {
        System.out.println(userService.findAll());;
    }
}

