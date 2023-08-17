package com.project.volka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.volka.dto.PromiseReqDTO;
import com.project.volka.entity.Plan;
import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.PromiseService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("promise")
@RequiredArgsConstructor
public class PromiseController {

    private final UserService userService;
    private final FriendService friendService;
    private final PromiseService promiseService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/request")
    @ResponseBody
    public ResponseEntity<?> promisePost(@AuthenticationPrincipal User user,
                                         @RequestBody HashMap<String, Object> promiseMap) {

        log.info(promiseMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        promiseService.makePromise(userInfo, promiseMap);
        // promise 가져오기
        List<PromiseReqDTO> promiseReqDTOList = promiseService.getPromiseReqDTOList(userInfo, 0);
        //객체를 던지는게 맞다 그래야 약속보기를 눌렀을때 약속을 보낸 내용을 확인할 수가 있음
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "약속을 요청했습니다.");
        log.info(promiseReqDTOList);
        //파싱에러났음
        ObjectMapper serverToClient = new ObjectMapper();
        String promiseReqDTOListJson = "";
        try{
             promiseReqDTOListJson = serverToClient.writeValueAsString(promiseReqDTOList);
        }catch (JsonProcessingException e){
            log.error("parsingError : ");
            log.error(e);
        }

        simpMessagingTemplate.convertAndSend("/queue/promise/" +  promiseMap.get("friendName"),promiseReqDTOListJson);
        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }


}
