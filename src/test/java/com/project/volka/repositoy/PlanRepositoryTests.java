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
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.annotation.Commit;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@Log4j2
@Transactional
public class PlanRepositoryTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ModelMapper modelMapper;




    @Test
    public void findUserByUserIdTest() {
        String userId = "test";
        LocalDateTime now = LocalDateTime.now();
        PlanDTO planDTO = new PlanDTO();
        planDTO.setPlanStatus(0);
        planDTO.setPlanContent("내용");
        planDTO.setPlanTitle("제목");
        //planDTO.setUserNo(userId);
        UserInfo userInfo = userRepository.findById(userId).orElseThrow();
        planDTO.setPlanUserNo(userInfo);
        planDTO.setPlanStartDate(now);
        planDTO.setPlanEndDate(now.plusDays(3));

        Plan plan = modelMapper.map(planDTO, Plan.class);
        planRepository.save(plan);
        log.info(plan);
    }

    @Test
    public void getKeyword(){
        Optional<UserInfo> result = userRepository.getWithRoles("test");
        UserInfo userInfo = result.orElseThrow();

        String keyWord = userInfo.getUserKeyword();
        List<String> keyWordList = new ArrayList<>();
        log.info(keyWord);

        if (keyWord != null && !keyWord.isEmpty()) {
            keyWordList = List.of(keyWord.split("/"));
            log.info(keyWordList);
        }
    }

    @Test
    public void findByUserInfoTest(){
        UserInfo userInfo = userRepository.findById("test").orElseThrow();
        List<Plan> plans = planRepository.findByPlanUserNo(userInfo);
        log.info(plans);
    }

}
