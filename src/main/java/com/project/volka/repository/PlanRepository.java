package com.project.volka.repository;

import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan,Long> {

}
