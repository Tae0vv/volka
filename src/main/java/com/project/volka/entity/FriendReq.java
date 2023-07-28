package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"friendReqUserNo","friendResUserNo"})
@Table
public class FriendReq extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIEND_REQ_NO")
    private Long friendReqNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FRIEND_REQ_USER_NO" ,nullable = false, referencedColumnName = "USER_NO")
    private UserInfo friendReqUserNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FRIEND_RES_USER_NO" ,nullable = false, referencedColumnName = "USER_NO")
    private UserInfo friendResUserNo;

    @Column(name = "FRIEND_REQ_STATUS" ,nullable = false)
    private int friendReqStatus;// 0대기 1수락 2거절 수락시 friend 테이블에 두번의 로그
}
