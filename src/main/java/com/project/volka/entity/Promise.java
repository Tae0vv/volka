package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"promiseReq","promiseBreakeUser"})
@Table
public class Promise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROMISE_NO")
    private Long promiseNo;

    @JoinColumn(name = "PROMISE_REQ")
    @OneToOne(fetch = FetchType.LAZY)
    private PromiseReq promiseReq;

    @Column(name = "PROMISE_STATUS")
    private int promiseStatus; //0대기 1진행중 2완료 3파토

    @Column(name = "PROMISE_BREAKE_REASON")
    private String promiseBreakeReason;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMISE_BREAKE_USER_ID", referencedColumnName = "USER_ID")
    private UserInfo promiseBreakeUser ;

}






