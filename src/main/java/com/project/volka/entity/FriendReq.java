package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "FRIEND_REQ")
public class FriendReq extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIEND_REQ_NO")
    private Long friendReqNo;

    @ManyToOne
    @JoinColumn(name = "FRIEND_REQ_USER_NO" ,nullable = false)
    private UserInfo friendSendUserNo;

    @ManyToOne
    @JoinColumn(name = "FRIEND_REC_USER_NO" ,nullable = false)
    private UserInfo friendReceiveUserNo;

    @Column(name = "FRIEND_REQ_STATUS" ,nullable = false)
    private int friendReqStatus;
}
