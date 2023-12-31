package com.project.volka.controller.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.volka.entity.UserInfo;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserStatusController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ConcurrentHashMap<String, String> loginAllUsers = new ConcurrentHashMap<>(); // 스레드에 안전한 자료구조
    private final UserService userService;
    private final FriendService friendService;

    @MessageMapping("/status")
    public void handleUserStatus(String message) {
        log.info("받은 메세지 : " + message);
        Map<String, String> friendMap = new LinkedHashMap<>();

        try{
            ObjectMapper clientToServer = new ObjectMapper();
            JsonNode jsonNode = clientToServer.readTree(message);
            String nickName = jsonNode.get("nickName").asText();
            String status = jsonNode.get("status").asText();
            loginAllUsers.put(nickName, status);
            log.info("접속중인 전체 유저는 : " + loginAllUsers);
            UserInfo userInfo = userService.getUserInfo(nickName);
            log.info("유저인포 : " + userInfo);
            List<String> friends = friendService.getFriendsNickName(userInfo, 1);
            log.info("friends : " + friends);

            for (String friend : friends) {
                friendMap.put(friend, "off");
            }

            for (Map.Entry<String, String> entry : loginAllUsers.entrySet()) {
                String userNickName = entry.getKey();
                if (friendMap.containsKey(userNickName)) {
                    friendMap.put(userNickName, "on");
                }
            }

            ObjectMapper serverToClient = new ObjectMapper();
            String userStatusJson = serverToClient.writeValueAsString(friendMap);
            String senderDestination = "/queue/onoff/" + nickName;

            log.info("전송 준비: {}, 내용: {}", senderDestination, userStatusJson);
            simpMessagingTemplate.convertAndSend(senderDestination, userStatusJson);
            simpMessagingTemplate.convertAndSend("/topic/onoff", message);
        }catch (Exception e){
            log.info(e);
        }
    }



    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        if (headerAccessor.getUser() != null) {
            String disconnectedUser = headerAccessor.getUser().getName();
            String disconnectedUserNickName = userService.getUserNickName(disconnectedUser);
            loginAllUsers.remove(disconnectedUserNickName);

            Map<String, String> disconnectedUserStatus = new HashMap<>();
            disconnectedUserStatus.put("nickName", disconnectedUserNickName);
            disconnectedUserStatus.put("status", "off");

            try {
                // Map을 JSON 문자열로 변환
                ObjectMapper objectMapper = new ObjectMapper();
                String userStatusJson = objectMapper.writeValueAsString(disconnectedUserStatus);

                // 상태 업데이트를 위해 메시지를 보냄
                simpMessagingTemplate.convertAndSend("/topic/onoff", userStatusJson);
            } catch (Exception e) {
                log.info(e);
            }
        }
    }


}
