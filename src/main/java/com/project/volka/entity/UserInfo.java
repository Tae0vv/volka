package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "USER_INFO")
public class UserInfo extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_NO")
    private Long userNo;

    @Column(length = 20, nullable = false, name = "USER_ID")
    private String userId;

    @Column(length = 20, nullable = false, name = "USER_PW")
    private String userPw;

    @Column(length = 200, nullable = false, name = "USER_ADDRESS")
    private String userAddress;

    @Column(length = 20, nullable = false, name = "USER_NAME")
    private String userName;

    @Column(length = 20, nullable = false, name = "USER_NICK_NAME")
    private String userNickName;

    @Column(length = 20, nullable = false, name = "USER_PHONE")
    private String userPhone;

    @Column(nullable = false, name = "USER_STATUS")
    private int userStatus;
    // 0 회원 1 탈퇴 2 벤

    @Column(nullable = false, name = "USER_ON")
    private boolean userOn;

    public void change(String userPw, String userAddress, String userName, String userPhone, char userStatus, boolean userOn) {
        this.userPw = userPw;
        this.userAddress = userAddress;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userStatus = userStatus;
        this.userOn = userOn;
    }
}
