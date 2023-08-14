package com.project.volka.service.interfaces;

import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import org.springframework.security.core.userdetails.User;

import java.util.List;
import java.util.Map;

public interface UserService {



    void signup(UserInfoDTO userInfoDTO) throws Exception;
    void kakaoAddInfo(UserInfoDTO userInfoDTO);
    UserInfo updateUserInfo(UserSecurityDTO user);
    void addKeyword(UserInfo userInfo, Map<String,Object> keywordMap);
    void deleteKeyword(UserInfo userInfo, Map<String,Object> keywordMap);
    String getUserNickName(String id);
    UserInfo getUserInfo(String nickName);

}
