package com.project.volka.service;

import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@Log4j2
public class UserServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void addColorTest(){
        UserInfo userInfo = userRepository.findById("test").orElseThrow();
        Map map = new HashMap<String, Object>();
        map.put("color", "rgb(60, 141, 188)");
        map.put("title", "test");

        userService.addKeyword(userInfo,map);
    }
}
