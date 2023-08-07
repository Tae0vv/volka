package com.project.volka.service.impl;

import com.project.volka.dto.PlanDTO;
import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.PlanRepository;
import com.project.volka.service.interfaces.PlanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final ModelMapper modelMapper;

    @Override
    public void makePlan(UserInfo userInfo, Map<String, Object> planMap) {

        log.info(planMap);
        PlanDTO planDTO = new PlanDTO();


        if(planMap.containsKey("title")){
            planDTO.setPlanTitle(planMap.get("title").toString());
        }

        if(planMap.containsKey("content")){
            planDTO.setPlanContent(planMap.get("content").toString());

        }


        if(planMap.containsKey("color")){
            planDTO.setPlanColor(planMap.get("color").toString());
        }

        planDTO.setPlanStatus(0);
        planDTO.setPlanUserNo(userInfo);

        Plan plan = modelMapper.map(planDTO, Plan.class);

        if (planMap.containsKey("planStartDate")) {
            String dateString = planMap.get("planStartDate").toString();

            if (!dateString.isEmpty()) {
                LocalDateTime localDateTime;

                if (dateString.contains("Z")) {
                    String cutString = dateString.substring(0, dateString.length() - 5);
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    localDateTime = LocalDateTime.parse(cutString, formatter);
                    plan.changePlanStartDate(localDateTime);
                }else{
                    localDateTime = LocalDateTime.parse(dateString);
                    plan.changePlanStartDate(localDateTime);
                }


            }else{
                plan.changePlanStartDate(LocalDateTime.now());
            }
        }

        if (planMap.containsKey("planEndDate")) {
            String dateString = planMap.get("planEndDate").toString();
            if (!dateString.isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.parse(dateString);
                plan.changePlanEndDate(localDateTime);
            }
        }

        planRepository.save(plan);
    }

    @Override
    public void modifyPlan(UserInfo userInfo, Map<String, Object> planMap) {

        if (planMap.containsKey("planNo")) {

            String planNoStr =  planMap.get("planNo").toString();
            int planNo = Integer.parseInt(planNoStr);
            Long longPlanNo = (long) planNo;
            Plan plan = planRepository.findById(longPlanNo).orElseThrow();



            if (planMap.containsKey("planStartDate")) {
                String dateString = planMap.get("planStartDate").toString();
                if (!dateString.isEmpty()) {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateString);
                    plan.changePlanStartDate(localDateTime);
                }
            }

            if (planMap.containsKey("planEndDate")) {
                String dateString = planMap.get("planEndDate").toString();
                if (!dateString.isEmpty()) {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateString);
                    plan.changePlanEndDate(localDateTime);
                }
            }

            if (planMap.containsKey("realStartDate")) {
                String dateString = planMap.get("realStartDate").toString();
                if (!dateString.isEmpty()) {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateString);
                    plan.changeRealStartDate(localDateTime);
                }
            }

            if (planMap.containsKey("realEndDate")) {
                String dateString = planMap.get("realEndDate").toString();
                if (!dateString.isEmpty()) {
                    LocalDateTime localDateTime = LocalDateTime.parse(dateString);
                    plan.changeRealEndDate(localDateTime);
                }
            }

            if (planMap.containsKey("title")) {
                plan.changePlanTitle(planMap.get("title").toString());
            }

            if (planMap.containsKey("content")) {
                plan.changePlanContent(planMap.get("content").toString());
            }

            if (planMap.containsKey("status")) {
                String statusStr = planMap.get("status").toString();
                int status = Integer.parseInt(statusStr);
                plan.changePlanStatus(status);
            }


            planRepository.save(plan);
        }
    }

    @Override
    public void changeDate(UserInfo userInfo, Map<String, Object> planMap) {
        if (planMap.containsKey("planNo")) {
            log.info("서비스");
            log.info(planMap);
            String planNoStr = planMap.get("planNo").toString();

            int planNo = Integer.parseInt(planNoStr);
            Long longPlanNo = (long) planNo;
            Plan plan = planRepository.findById(longPlanNo).orElseThrow();


            if (planMap.containsKey("start")) {
                String dateString = planMap.get("start").toString();

                if (!dateString.isEmpty()) {
                    LocalDateTime localDateTime;

                    if (dateString.contains("Z")) {
                        String cutString = dateString.substring(0, dateString.length() - 5);
                        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                        localDateTime = LocalDateTime.parse(cutString, formatter);
                        plan.changePlanStartDate(localDateTime);
                    } else {
                        localDateTime = LocalDateTime.parse(dateString);
                        plan.changePlanStartDate(localDateTime);
                    }

                }
            }

            if (planMap.containsKey("end")) {
                String dateString = planMap.get("end").toString();


            }

            planRepository.save(plan);
        }
    }


    @Override
    public List<Plan> getPlanList(UserInfo userInfo) {

        return planRepository.findByPlanUserNo(userInfo);
    }


}
