package com.project.volka.service;

import com.project.volka.dto.FriendReqDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.FriendRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.UserService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Log4j2
public class FriendServiceTests {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FriendService friendService;

    @Test
    public void friendReqTest(){
        UserInfo userInfo = userRepository.findById("a").orElseThrow();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("nickName","c");
        try{
            friendService.requestFriendship(userInfo, hashMap);
        }catch (Exception e){
            log.info("없다");
        }
    }

    @Test
    public void accept(){
        FriendReqDTO friendReqDTO = new FriendReqDTO();
        friendReqDTO.setFriendNo(28L);
        friendReqDTO.setFriendNickName("volka");
        friendService.acceptFriendship(friendReqDTO);
    }

    @Test
    public void getTests(){
        UserInfo userInfo = userRepository.findById("volka").orElseThrow();
        List<FriendReqDTO> friendRequests = friendService.getFriendRequests(userInfo);
        log.info("인포");
        log.info(friendRequests);
    }


}
