package com.project.volka.dto;

import com.project.volka.entity.UserInfo;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class PromiseDTO {

    private Long promiseNo;
    private UserInfo mainUser;
    private UserInfo targetUser;
    private int promiseStatus;
    private String planTitle;
    private String planContent;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private String planColor;
    private Long pairNo;
}






