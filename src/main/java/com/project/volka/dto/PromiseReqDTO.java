package com.project.volka.dto;

import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
public class PromiseReqDTO {

    private Long promiseNo;
    private String mainUser;
    private String targetUser;
    private int promiseStatus;
    private String planTitle;
    private String planContent;
    private LocalDateTime planStartDate;
    private LocalDateTime planEndDate;
    private String planColor;
    private Long pairNo;



    public void entityToDTO(Promise promise){
        this.promiseNo = promise.getPromiseNo();
        this.mainUser = promise.getMainUser().getUserNickName();
        this.targetUser = promise.getTargetUser().getUserNickName();
        this.promiseStatus = promise.getPromiseStatus();
        this.planTitle = promise.getPlanTitle();
        this.planContent = promise.getPlanContent();
        this.planStartDate = promise.getPlanStartDate();
        this.planEndDate = promise.getPlanEndDate();
        this.planColor = promise.getPlanColor();
        this.pairNo = promise.getPairNo();

    }
}






