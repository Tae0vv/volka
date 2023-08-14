package com.project.volka.controller.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserStatusController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/status")
    public void handleUserStatus(String message) {
        log.info("받은 메세지 : " + message);

        // 처리 후 변경된 유저 상태 정보를 반환 (예: 온라인/오프라인 상태 등)
        // 온라인한 유저 중에서 온라인한 내 친구를 updatedStatusMessage /단위로 끊어서 보낸다.
        // 내 친구는 List<String> friends = friendService.getFriendsNickName(userinfo, 1);
        Map<String, String> userStatusMap = new HashMap<>();
        userStatusMap.put("nickName1", "on");
        userStatusMap.put("nickName2", "off");
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String userStatusJson = objectMapper.writeValueAsString(userStatusMap);
            messagingTemplate.convertAndSend("/topic/onoff", userStatusJson);
        }catch (Exception e){
            log.info(e);
        }
    }

}
