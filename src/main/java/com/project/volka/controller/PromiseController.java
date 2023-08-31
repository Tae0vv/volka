package com.project.volka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.volka.dto.PlanDTO;
import com.project.volka.dto.PromiseReqDTO;
import com.project.volka.entity.Plan;
import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.PromiseRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.FriendService;
import com.project.volka.service.interfaces.PlanService;
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
    private final PlanService planService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/request")
    @ResponseBody
    public ResponseEntity<?> promisePost(@AuthenticationPrincipal User user,
                                         @RequestBody HashMap<String, Object> promiseMap) {

        String resUserNickName = promiseMap.get("friendName").toString();
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        UserInfo promiseResUser = userService.getUserInfo(resUserNickName);

        promiseService.makePromise(userInfo, promiseMap);

        List<PromiseReqDTO> promiseResDTOList = promiseService.getPromiseReqDTOList(promiseResUser, 0);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "약속을 요청했습니다.");

        ObjectMapper serverToClient = new ObjectMapper();
        serverToClient.registerModule(new JavaTimeModule());

        String promiseReqDTOListJson = "";
        try{
             promiseReqDTOListJson = serverToClient.writeValueAsString(promiseResDTOList);
        }catch (JsonProcessingException e){
            log.error(e);
        }
        log.info(promiseReqDTOListJson);
        simpMessagingTemplate.convertAndSend("/queue/promise/" +  promiseMap.get("friendName"),promiseReqDTOListJson);
        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }

    @PostMapping("/accept")
    @ResponseBody
    public ResponseEntity<?> promiseAccept(@AuthenticationPrincipal User user,
                                         @RequestBody PromiseReqDTO promiseReqDTO) {

        UserInfo promiseResUser = userService.updateUserInfo((UserSecurityDTO) user); //수락한 유저
        UserInfo promiseReqUser = userService.getUserInfo(promiseReqDTO.getTargetUser());
        
        promiseService.promiseAccept(promiseReqDTO);
        planService.makePromisePlan(promiseReqDTO);
        List<PlanDTO> promiseResUserPlans = planService.getPlanList(promiseResUser);
        List<PlanDTO> promiseReqUserPlans = planService.getPlanList(promiseReqUser);
        List<PromiseReqDTO> promiseResList =  promiseService.getPromiseReqDTOList(promiseResUser,0);
        List<PromiseReqDTO> promiseReqList = promiseService.getPromiseReqDTOList(promiseReqUser,0);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("plans", promiseResUserPlans);
        responseData.put("promiseRequests", promiseResList);

        HashMap<String, Object> waitUserData = new HashMap<>();
        waitUserData.put("plans",promiseReqUserPlans);
        waitUserData.put("promiseRequests",promiseReqList);

        String waitUserDataJson = "";

        ObjectMapper serverToClient = new ObjectMapper();
        serverToClient.registerModule(new JavaTimeModule());

        try{
            waitUserDataJson = serverToClient.writeValueAsString(waitUserData);
        }catch (JsonProcessingException e){
            log.error("parsingError : ");
            log.error(e);
        }

        simpMessagingTemplate.convertAndSend("/queue/agree/" +  promiseReqDTO.getTargetUser(),waitUserDataJson);
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/reject")
    @ResponseBody
    public ResponseEntity<?> promiseReject(@AuthenticationPrincipal User user,
                                         @RequestBody PromiseReqDTO promiseReqDTO) {

        UserInfo promiseResUser = userService.updateUserInfo((UserSecurityDTO) user); //수락한 유저

        promiseService.promiseReject(promiseReqDTO);
        List<PromiseReqDTO> promiseResList =  promiseService.getPromiseReqDTOList(promiseResUser,0);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("promiseRequests", promiseResList);

        return ResponseEntity.ok(responseData);
    }



}
