package com.project.volka.service.interfaces;

import com.project.volka.dto.PlanDTO;
import com.project.volka.dto.PromiseReqDTO;
import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface PlanService {

    void makePromisePlan(PromiseReqDTO promiseReqDTO);
    void makePlan(UserInfo userInfo, Map<String,Object> planMap);
    void modifyPlan(UserInfo userInfo, Map<String,Object> planMap);
    void updatePlanDate(Map<String, Object> planMap);

    void deletePlan(Map<String, Object> planMap);
    List<PlanDTO> getPlanList(UserInfo userInfo);
}
