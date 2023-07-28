package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FRIEND_NO")
    private Long friendNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_MAIN_NO" ,nullable = false)
    private UserInfo UserMainNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SUB_NO" ,nullable = false)
    private UserInfo UserSubNo;

    @Column(name = "FRIEND_REMOVE" ,nullable = false)
    private int friendRemove;
    // 0이면 정상 1이면 숨김 2이면 삭제

}
