package com.project.volka.service.interfaces;

import com.project.volka.dto.UserInfoDTO;

public interface UserService {
    static class MidExistException extends Exception{

    }

    void signup(UserInfoDTO userInfoDTO) throws MidExistException;
    void kakaoAddInfo(UserInfoDTO userInfoDTO);
}
