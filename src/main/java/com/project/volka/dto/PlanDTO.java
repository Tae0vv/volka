package com.project.volka.dto;

import com.project.volka.entity.UserInfo;
import lombok.*;

import java.time.LocalDateTime;


@Data
public class PlanDTO {

    private Long planNo;
    //private String userNo;
    private UserInfo planUserNo;
    private String planTitle;
    private String planContent;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private LocalDateTime realStartDate;
    private LocalDateTime realEndDate;
    private int planStatus; //0대기 1진행중 2완료 3취소 4약속을보냈는데상대방이거절
}






