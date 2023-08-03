package com.project.volka.dto;

import lombok.Data;

@Data
public class UserInfoDTO {

    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    private String userNickName;
    private String userPhone;
    private int userStatus;
    private boolean userOn;
    private boolean userSocial;
    private String userKeyword;

}
