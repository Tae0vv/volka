package com.project.volka.service.interfaces;

import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.security.dto.UserSecurityDTO;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface UserService {

    static class MidExistException extends Exception{

    }

    void signup(UserInfoDTO userInfoDTO) throws MidExistException;
    void kakaoAddInfo(UserInfoDTO userInfoDTO);

    UserInfo updateUserInfo(UserSecurityDTO user);
}
