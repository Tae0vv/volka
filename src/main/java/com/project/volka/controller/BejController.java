package com.project.volka.controller;

import com.project.volka.dto.*;
import com.project.volka.entity.*;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("bej")
@RequiredArgsConstructor
public class BejController {

    private final UserService userService;
    private final PlanService planService;
    private final FriendService friendService;
    private final PromiseService promiseService;
    private final ChatRoomService chatRoomService;
    private final ChatService chatService;
    private final ProfileService profileService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/main")
    public void bejMainGet(@AuthenticationPrincipal User user,Model model){
        log.info(user);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        List<PlanDTO> planList = planService.getPlanList(userInfo);
        List<String> friends = friendService.getFriendsNickName(userInfo, 1);
        List<FriendReqDTO> friendRequests = friendService.getFriendRequests(userInfo);
        List<PromiseReqDTO> promiseRequests = promiseService.getPromiseReqDTOList(userInfo, 0);
        List<ChatRoomDTO> chatRooms = chatRoomService.getChatRooms(userInfo.getUserId());
        List<ChatDTO> unreadChats = chatService.getUnreadChats(userInfo);
        List<ChatRoomDTO> unReadChatRooms = chatRoomService.getUnReadChatRooms(unreadChats);

        model.addAttribute("user", userInfo);
        model.addAttribute("planList", planList);
        model.addAttribute("friends", friends);
        model.addAttribute("friendRequests", friendRequests);
        model.addAttribute("promiseRequests", promiseRequests);
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("unreadChats", unreadChats);
        model.addAttribute("unReadChatRooms", unReadChatRooms);
    }

    @PostMapping("/plan")
    @ResponseBody
    public ResponseEntity<?> planPost(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, Object> planMap) {

        log.info(planMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        planService.makePlan(userInfo,planMap);
        List<PlanDTO> planList = planService.getPlanList(userInfo);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");
        responseData.put("list", planList);

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }
    @PutMapping("/plan")
    @ResponseBody
    public ResponseEntity<?> planPut(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, Object> planMap) {

        log.info(planMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        planService.modifyPlan(userInfo,planMap);
        List<PlanDTO> planList = planService.getPlanList(userInfo);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");
        responseData.put("list", planList);

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }
    @DeleteMapping("/plan")
    @ResponseBody
    public ResponseEntity<?> planDelete(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, Object> planMap) {

        log.info(planMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        planService.deletePlan(planMap);
        List<PlanDTO> planList = planService.getPlanList(userInfo);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");
        responseData.put("list", planList);

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }

    @PostMapping("/planList")
    @ResponseBody
    public ResponseEntity<?> planGet(@AuthenticationPrincipal User user,
                                     @RequestBody HashMap<String, Object> planMap) {

        log.info(planMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        planService.getPlanList(userInfo);
        List<PlanDTO> planList = planService.getPlanList(userInfo);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");
        responseData.put("list", planList);

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }

    @PostMapping("/keyword")
    @ResponseBody
    public ResponseEntity<?> keywordPost(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, Object> keywordMap) {

        log.info(keywordMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        userService.addKeyword(userInfo,keywordMap);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }

    @DeleteMapping("/keyword")
    @ResponseBody
    public ResponseEntity<?> keywordDelete(@AuthenticationPrincipal User user,
                                         @RequestBody HashMap<String, Object> keywordMap) {

        log.info(keywordMap);
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        userService.deleteKeyword(userInfo,keywordMap);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }

    @PutMapping("/date")
    @ResponseBody
    public ResponseEntity<?> datePut(@AuthenticationPrincipal User user,
                                     @RequestBody HashMap<String, Object> planMap) {

        log.info(planMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        planService.updatePlanDate(planMap);
        List<PlanDTO> planList = planService.getPlanList(userInfo);

        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");
        responseData.put("list", planList);
        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/schedule")
    public String scheduleGet(@AuthenticationPrincipal User user, Model model, @RequestParam String friend) {

        log.info("들어옴?");
        log.info(friend);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        UserInfo friendInfo = userService.getUserInfo(friend);
        List<String> friendsOfFriend = friendService.getFriendsNickName(friendInfo,1);
        boolean isFriend = false;

        for(String friendOfFriend : friendsOfFriend){
            if(userInfo.getUserNickName().equals(friendOfFriend)){
                isFriend = true;
                break;
            }
        }


        if (isFriend) {
            List<PlanDTO> planList = planService.getPlanList(friendInfo);

            model.addAttribute("user", friendInfo);
            model.addAttribute("planList", planList);
            return "bej/schedule";
        } else {
            return "bej/nofriend";
        }
    }
}
