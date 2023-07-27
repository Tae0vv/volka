package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table(name = "FRIEND")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIEND_NO")
    private Long friendNo;

    @OneToOne
    @JoinColumn(name = "FRIEND_REQ_NO" ,nullable = false)
    private FriendReq friendReqNo;

    @Column(name = "FRIEND_REMOVE" ,nullable = false)
    private int friendRemove;
    // 0이면 정상 1이면 숨김 2이면 삭제
}
