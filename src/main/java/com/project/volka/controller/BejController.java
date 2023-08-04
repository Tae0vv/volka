package com.project.volka.controller;

import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.PlanService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("bej")
@RequiredArgsConstructor
public class BejController {

    private final UserService userService;
    private final PlanService planService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/main")
    public void hello(@AuthenticationPrincipal User user,Model model){
        log.info(user);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        List<Plan> planList = planService.getPlanList(userInfo);

        model.addAttribute("user", userInfo);
        model.addAttribute("planList", planList);
    }

    @PostMapping("/plan")
    @ResponseBody
    public ResponseEntity<?> planPost(@AuthenticationPrincipal User user,
                                      @RequestBody HashMap<String, Object> planMap) {

        log.info(planMap);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        planService.makePlan(userInfo,planMap);
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");

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

}
