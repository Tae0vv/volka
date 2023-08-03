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
public class Plan extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAN_NO")
    private Long planNo;

    @JoinColumn(name = "USER_ID" ,nullable = false, referencedColumnName = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private UserInfo planUserNo;

    @Column(name = "PLAN_TITLE" ,nullable = false)
    private String planTitle;

    @Column(name = "PLAN_CONTENT",length = 5000)
    private String planContent;

    @Column(name = "PLAN_START_DATE",nullable = false)
    private LocalDateTime planStartDate;

    @Column(name = "PLAN_END_DATE",nullable = false)
    private LocalDateTime planEndDate;

    @Column(name = "REAL_START_DATE")
    private LocalDateTime realStartDate;

    @Column(name = "REAL_END_DATE")
    private LocalDateTime realEndDate;

    @Column(name = "PLAN_STATUS",nullable = false)
    private int planStatus; //0대기 1진행중 2완료 3취소 4약속을보냈는데상대방이거절


    public void changePlanTitle(String planTitle) {
    }

    public void changePlanContent(String planContent) {
        this.planContent = planContent;
    }

    public void changePlanStartDate(LocalDateTime planStartDate) {
        this.planStartDate = planStartDate;
    }

    public void changePlanEndDate(LocalDateTime planEndDate) {
        this.planEndDate = planEndDate;
    }

    public void changeRealStartDate(LocalDateTime realStartDate) {
        this.realStartDate = realStartDate;
    }

    public void changeRealEndDate(LocalDateTime realEndDate) {
        this.realEndDate = realEndDate;
    }

    public void changePlanStatus(int planStatus) {
        this.planStatus = planStatus;
    }
}






