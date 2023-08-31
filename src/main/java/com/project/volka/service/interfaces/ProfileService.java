package com.project.volka.service.interfaces;


import com.project.volka.entity.Profile;
import com.project.volka.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;


public interface ProfileService {

    void regProfile(UserInfo userInfo, MultipartFile file);
    Profile getProfile(UserInfo userInfo);

}
