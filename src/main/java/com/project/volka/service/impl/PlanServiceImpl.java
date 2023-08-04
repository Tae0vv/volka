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

        PlanDTO planDTO = new PlanDTO();


        if(planMap.containsKey("planStartDate")){
            String dateString = (String) planMap.get("planStartDate");
            Instant instant = Instant.parse(dateString);
            LocalDateTime localDateTime = instant.atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime(); //시간 변환 후
            planDTO.setPlanStartDate(localDateTime);
        }

        if(planMap.containsKey("title")){
            planDTO.setPlanTitle((String) planMap.get("title"));
        }

        if(planMap.containsKey("content")){
            planDTO.setPlanContent((String) planMap.get("content"));

        }
        if(planMap.containsKey("planEndDate")){
            LocalDateTime localDateTime = (LocalDateTime) planMap.get("planEndDate");
            planDTO.setPlanEndDate(localDateTime);
        }
        if(planMap.containsKey("realStartDate")){
            LocalDateTime localDateTime = (LocalDateTime) planMap.get("realStartDate");
            planDTO.setRealStartDate(localDateTime);
        }
        if(planMap.containsKey("realEndDate")){
            LocalDateTime localDateTime = (LocalDateTime) planMap.get("realEndDate");
            planDTO.setRealEndDate(localDateTime);
        }
        if(planMap.containsKey("color")){
            planDTO.setPlanColor((String) planMap.get("color"));
        }

        planDTO.setPlanStatus(0);
        planDTO.setPlanUserNo(userInfo);

        Plan plan = modelMapper.map(planDTO, Plan.class);
        log.info(plan);
        planRepository.save(plan);
    }

    @Override
    public List<Plan> getPlanList(UserInfo userInfo) {

        return planRepository.findByPlanUserNo(userInfo);
    }


}
