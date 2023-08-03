package com.project.volka.repository;

import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanRepository extends JpaRepository<Plan,Long> {

    List<Plan> findByPlanUserNo(UserInfo userInfo);
}
