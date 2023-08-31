package com.project.volka.service;

import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.ChatService;
import com.project.volka.service.interfaces.ProfileService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@Log4j2
public class ProfileTests {

    @Autowired
    private ProfileService profileService;
    @Autowired
    private UserRepository userRepository;



    @Test
    public void getProfile(){

        UserInfo userInfo = userRepository.findById("kty2451@gmail.com").orElseThrow();
        log.info(profileService.getProfile(userInfo));
    }

}
