package com.project.volka.service;

import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.PromiseService;
import com.project.volka.service.interfaces.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
@Log4j2
public class PromiseServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendService friendService;
    @Autowired
    private PromiseService promiseService;

    @Test
    public void getPromiseList(){
    }


}
