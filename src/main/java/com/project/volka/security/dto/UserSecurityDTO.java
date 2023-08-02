package com.project.volka.security.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;


@Getter
@Setter
@ToString
public class UserSecurityDTO  extends User implements OAuth2User {

    private String userId;
    private String userPw;
    private String userName;
    private String userEmail;
    private String userNickName;
    private String userPhone;
    private int userStatus;
    private boolean userOn;
    private boolean userSocial;
    private Map<String,Object> props; //소셜 로그인 정보


    public UserSecurityDTO(String username, String password,String userName,
                           String userEmail, String userNickName, String userPhone,
                           int userStatus, boolean userOn, boolean userSocial,
                           Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = username;
        this.userPw = password;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userNickName = userNickName;
        this.userPhone = userPhone;
        this.userStatus = userStatus;
        this.userOn = userOn;
        this.userSocial = userSocial;
    }

    @Override
    public Map<String,Object> getAttributes(){
        return this.getProps();
    }

    @Override
    public String getName(){
        return this.userId;
    }

    public String getUserPw(){
        return this.userPw;
    }
}
