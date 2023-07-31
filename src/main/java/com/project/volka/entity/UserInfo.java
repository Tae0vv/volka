package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "USER_INFO")
public class UserInfo extends BaseEntity{

    @Id
    @Column(length = 20, nullable = false, name = "USER_ID")
    private String userId;

    @Column(nullable = false, name = "USER_PW")
    private String userPw;

    @Column(length = 20, nullable = false, name = "USER_NAME")
    private String userName;

    @Column(length = 2000, nullable = false, name = "USER_EMAIL")
    private String userEmail;

    @Column(length = 20, nullable = false, name = "USER_NICK_NAME")
    private String userNickName;

    @Column(length = 20, nullable = false, name = "USER_PHONE")
    private String userPhone;

    @Column(nullable = false, name = "USER_STATUS")
    private int userStatus;
    // 0 회원 1 탈퇴 2 벤

    @Column(nullable = false, name = "USER_ON")
    private boolean userOn;

    @Column(nullable = false, name = "USER_SOCIAL")
    private boolean userSocial;

    @ElementCollection
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>();


    public void changePassword(String userPw){
        this.userPw = userPw;
    }

    public void changeStatus(int userStatus){
        this.userStatus = userStatus;
    }

    public void changeNickName(String userNickName){
        this.userNickName = userNickName;
    }

    public void addRole(UserRole userRole) {
        this.roleSet.add(userRole);
    }

    public void clearRoles(){
        this.roleSet.clear();
    }

    public void changeSocial(boolean userSocial){
        this.userSocial = userSocial;
    }

}
