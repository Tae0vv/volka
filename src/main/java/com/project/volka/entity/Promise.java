package com.project.volka.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Table
public class Promise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROMISE_NO")
    private Long promiseNo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "MAIN_USER_ID" ,nullable = false, referencedColumnName = "USER_ID")
    private UserInfo mainUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "TARGET_USER_ID" ,nullable = false, referencedColumnName = "USER_ID")
    private UserInfo targetUser;

    @Column(name = "PROMISE_STATUS" ,nullable = false)
    private int promiseStatus; // -1:건사람 0:대기 1:수락 2:거절

    @Column(name = "PLAN_TITLE" ,nullable = false)
    private String planTitle;

    @Column(name = "PLAN_CONTENT",length = 5000)
    private String planContent;

    @Column(name = "PLAN_START_DATE",nullable = false)
    private LocalDateTime planStartDate;

    @Column(name = "PLAN_END_DATE")
    private LocalDateTime planEndDate;

    @Column(name = "PLAN_COLOR")
    private String planColor;

    @Column(name = "PAIR_NO")
    private Long pairNo;

    public void acceptPromise() {
        this.promiseStatus = 1;
    }

    public void rejectPromise() {
        this.promiseStatus = 2;
    }

    public void regPair(Long pairNo){
        this.pairNo = pairNo;
    }
}






