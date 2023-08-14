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
@Table(name = "USER_INFO")
@ToString
public class UserInfo extends BaseEntity{

    @Id
    @Column(length = 20, nullable = false, name = "USER_ID")
    private String userId;

    @Column(nullable = false, name = "USER_PW")
    private String userPw;

    @Column(length = 20, nullable = false, name = "USER_NAME")
    private String userName;

    @Column(length = 2000, nullable = false, name = "USER_EMAIL", unique = true)
    private String userEmail;

    @Column(length = 20, nullable = false, name = "USER_NICK_NAME", unique = true)
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

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserRole> roleSet = new HashSet<>();

    @Column(name = "USER_Keyword")
    private String userKeyword;


    public void changePassword(String userPw){
        this.userPw = userPw;
    }

    public void changeStatus(int userStatus){
        this.userStatus = userStatus;
    }

    public void changeNickName(String userNickName){
        this.userNickName = userNickName;
    }

    public void changeName(String userName) {this.userName = userName; }
    public void changePhone(String userPhone) {this.userPhone = userPhone; }

    public void addRole(UserRole userRole) {
        this.roleSet.add(userRole);
    }

    public void clearRoles(){
        this.roleSet.clear();
    }

    public void changeSocial(boolean userSocial){
        this.userSocial = userSocial;
    }

    public void changeKeyword(String userKeyword) {
        this.userKeyword = userKeyword;
    }
}
