package com.project.volka.controller;

import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("friend")
public class FriendController {

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
            responseData.put("message", "요청이 성공적으로 처리되었습니다.");

        }catch (Exception e){
            responseData.put("status", "fail");
            responseData.put("message", "해당 닉네임을 가진 유저가 없습니다.");
        }

        return ResponseEntity.ok(responseData);
    }
}
