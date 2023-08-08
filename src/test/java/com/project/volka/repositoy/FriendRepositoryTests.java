package com.project.volka.repositoy;

import com.project.volka.dto.PlanDTO;
import com.project.volka.entity.Plan;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.PlanRepository;
import com.project.volka.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
@Transactional
public class FriendRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ModelMapper modelMapper;




    @Test
    public void findByUserInfoTest(){
        UserInfo userInfo = userRepository.findById("test").orElseThrow();
        List<Plan> plans = planRepository.findByPlanUserNo(userInfo);
        log.info(plans);
    }

}
