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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
@Log4j2
@RequiredArgsConstructor
public class UserStatusController {

    private final SimpMessagingTemplate messagingTemplate;
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
            log.info("변환전 " + friendMap);
            ObjectMapper serverToClient = new ObjectMapper();
            String userStatusJson = serverToClient.writeValueAsString(friendMap);
            log.info("변환후 : " + userStatusJson);
            // 새로운 사람이 접속할때마다 새로운 사람의 친구의 정보가 모두에게 넘어가는 문제발생
            // 이 handleUserStatus()는 보낸사람에게만 돌아가게 설정해야겠다.
            // 그리고 누군가가 새로 접속하거나 connect이 끊길때 접속한 사람들한테 각자의 친구리스트 보내줘야돼
            messagingTemplate.convertAndSend("/topic/onoff", userStatusJson);
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
            log.info("팅김 : " + disconnectedUserNickName);
            loginAllUsers.remove(disconnectedUserNickName);
        }
    }

}
