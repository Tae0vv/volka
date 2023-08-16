package com.project.volka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("friend")
public class FriendController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final FriendService friendService;
    private final UserService userService;


    @PostMapping("request")
    @ResponseBody
    public ResponseEntity<?> friendRequest(@AuthenticationPrincipal User user,
                                        @RequestBody HashMap<String,String> friendMap) {

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        HashMap<String, Object> responseData = new HashMap<>();

        try{
            friendService.requestFriendship(userInfo,friendMap);

            responseData.put("status", "success");
            responseData.put("message", "친구요청이 성공적으로 처리되었습니다.");
            UserInfo resUser = userService.getUserInfo(friendMap.get("nickName"));
            List<String> friendRequests = friendService.getFriendsNickName(resUser, 0);
            ObjectMapper serverToClient = new ObjectMapper();
            String friendRequestsJson = serverToClient.writeValueAsString(friendRequests);
            log.info("보내기전 : " + friendRequestsJson);
            simpMessagingTemplate.convertAndSend("/queue/req/" +  friendMap.get("nickName"),friendRequestsJson);

        }catch (Exception e){
            responseData.put("status", "fail");
            responseData.put("message", "해당 닉네임을 가진 유저가 없습니다.");
        }

        return ResponseEntity.ok(responseData);
    }

    @PostMapping("accept")
    @ResponseBody
    public ResponseEntity<?> friendAccept(@AuthenticationPrincipal User user,
                                        @RequestBody HashMap<String,String> friendMap) throws JsonProcessingException {

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        HashMap<String, Object> responseData = new HashMap<>();
        friendService.acceptFriendship(userInfo,friendMap);
        List<String> friends = friendService.getFriendsNickName(userInfo, 1);
        List<String> friendRequests = friendService.getFriendsNickName(userInfo, 0);

        UserInfo waitUser = userService.getUserInfo(friendMap.get("nickName"));
        List<String> waitUserFriends = friendService.getFriendsNickName(waitUser, 1);
        ObjectMapper serverToClient = new ObjectMapper();
        String waitUserFriendsJson = serverToClient.writeValueAsString(waitUserFriends);
        log.info("보내기전 : " + waitUserFriendsJson);

        simpMessagingTemplate.convertAndSend("/queue/accept/" +  friendMap.get("nickName"),waitUserFriendsJson);
        responseData.put("friends",friends);
        responseData.put("friendRequests",friendRequests);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("reject")
    @ResponseBody
    public ResponseEntity<?> friendReject(@AuthenticationPrincipal User user,
                                        @RequestBody HashMap<String,String> friendMap) {

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        HashMap<String, Object> responseData = new HashMap<>();
        friendService.rejectFriendship(userInfo,friendMap);
        List<String> friendRequests = friendService.getFriendsNickName(userInfo, 0);
        responseData.put("friendRequests",friendRequests);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("hide")
    @ResponseBody
    public ResponseEntity<?> friendHide(@AuthenticationPrincipal User user,
                                        @RequestBody HashMap<String,String> friendMap) {

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        HashMap<String, Object> responseData = new HashMap<>();
        friendService.hideFriendship(userInfo,friendMap);
        List<String> friends = friendService.getFriendsNickName(userInfo, 1);
        responseData.put("friends",friends);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("block")
    @ResponseBody
    public ResponseEntity<?> friendBlock(@AuthenticationPrincipal User user,
                                        @RequestBody HashMap<String,String> friendMap) {

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        HashMap<String, Object> responseData = new HashMap<>();
        friendService.blockFriendship(userInfo,friendMap);
        List<String> friends = friendService.getFriendsNickName(userInfo, 1);
        responseData.put("friends",friends);
        return ResponseEntity.ok(responseData);
    }
}
