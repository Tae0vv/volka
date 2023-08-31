package com.project.volka.controller;

import com.project.volka.entity.Profile;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.ProfileService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Log4j2
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final ProfileService profileService;

    @PostMapping("upload")
    public ResponseEntity<?> uploadFile(@AuthenticationPrincipal User user, @RequestParam("file") MultipartFile file) {
        UserInfo userInfo = userService.updateUserInfo((UserSecurityDTO) user);
        profileService.regProfile(userInfo, file);
        return ResponseEntity.ok().body("파일이 성공적으로 업로드 되었습니다.");
    }

    @GetMapping("image/{nickName}")
    public ResponseEntity<Resource> getProfileImage(@PathVariable String nickName) {
        log.info("들어옴?");
        log.info(nickName);
        UserInfo userInfo = userService.getUserInfo(nickName);
        Profile profile = profileService.getProfile(userInfo);
        Resource resource = null;
        if(profile != null){
            log.info("프로필있음");
            try{
                resource = new UrlResource("file:" + profile.getFilePath());
            }catch (Exception e){
                log.error(e);
            }
            return ResponseEntity.ok()
                    .body(resource);
        }else {
            log.info("프로필없음");
            try {
                resource = new ClassPathResource("/static/dist/img/volka.jpg");
            } catch (Exception e) {
                throw new RuntimeException("Error loading default image", e);
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        }

    }



}
