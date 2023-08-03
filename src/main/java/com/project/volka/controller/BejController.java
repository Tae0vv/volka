package com.project.volka.controller;

import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
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

import java.util.HashMap;

@Controller
@Log4j2
@RequestMapping("bej")
@RequiredArgsConstructor
public class BejController {

    private final UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/main")
    public void hello( @AuthenticationPrincipal User user,Model model){
        log.info(user);

        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        model.addAttribute("user", userInfo);
    }

    @PostMapping("/plan")
    @ResponseBody
    public ResponseEntity<?> planPost(@RequestBody HashMap<String, Object> map) {
        log.info("들어옴");
        log.info(map);

        // 클라이언트에게 보낼 데이터를 HashMap으로 구성
        HashMap<String, Object> responseData = new HashMap<>();
        responseData.put("status", "success");
        responseData.put("message", "요청이 성공적으로 처리되었습니다.");

        return ResponseEntity.ok(responseData); // 클라이언트에게 JSON 응답을 보냄
    }

}
