package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"promiseReqUserNo","promiseResUserNo"})
@Table
public class PromiseReq extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROMISE_REQ_NO")
    private Long promiseReqNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMISE_REQ_USER_ID" ,nullable = false, referencedColumnName ="USER_ID")
    private UserInfo promiseReqUserNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PROMISE_RES_USER_ID" ,nullable = false, referencedColumnName ="USER_ID")
    private UserInfo promiseResUserNo;

    @Column(name = "PROMISE_REQ_STATUS" ,nullable = false)
    private int promiseReqStatus; //0대기 1수락 2거절 1이면 promise table에 log

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAN_NO" ,nullable = false, referencedColumnName ="PLAN_NO")
    private Plan planNo;
}
