package com.project.volka.service.interfaces;

import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;

import java.util.List;
import java.util.Map;

public interface PlanService {

    void makePlan(UserInfo userInfo, Map<String,Object> planMap);
    void modifyPlan(UserInfo userInfo, Map<String,Object> planMap);
    void changeDate(UserInfo userInfo, Map<String,Object> planMap);
    List<Plan> getPlanList(UserInfo userInfo);
}
