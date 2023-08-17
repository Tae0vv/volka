package com.project.volka.service.impl;


import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.PromiseDTO;
import com.project.volka.dto.PromiseReqDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.Friend;
import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.PlanRepository;
import com.project.volka.repository.PromiseRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.PromiseService;
import com.project.volka.service.interfaces.SettingService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PromiseServiceImpl implements PromiseService {

    private final PromiseRepository promiseRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    @Override
    public void makePromise(UserInfo userInfo, HashMap<String, Object> promiseMap) {

        log.info(promiseMap);
        PromiseDTO promiseReqDTO = new PromiseDTO();
        PromiseDTO promiseResDTO = new PromiseDTO();

        if (promiseMap.containsKey("planStartDate")) {
            String dateString = promiseMap.get("planStartDate").toString();
            if (!dateString.isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.parse(dateString);
                promiseReqDTO.setPlanStartDate(localDateTime);
                promiseResDTO.setPlanStartDate(localDateTime);
            }
        }
        if (promiseMap.containsKey("planEndDate")) {
            String dateString = promiseMap.get("planEndDate").toString();
            if (!dateString.isEmpty()) {
                LocalDateTime localDateTime = LocalDateTime.parse(dateString);
                promiseReqDTO.setPlanEndDate(localDateTime);
                promiseResDTO.setPlanEndDate(localDateTime);
            }
        }
        if (promiseMap.containsKey("title")) {
            promiseReqDTO.setPlanTitle(promiseMap.get("title").toString());
            promiseResDTO.setPlanTitle(promiseMap.get("title").toString());
        }

        if (promiseMap.containsKey("content")) {
            promiseReqDTO.setPlanContent(promiseMap.get("content").toString());
            promiseResDTO.setPlanContent(promiseMap.get("content").toString());
        }

        if (promiseMap.containsKey("color")) {
            promiseReqDTO.setPlanColor(promiseMap.get("color").toString());
            promiseResDTO.setPlanColor(promiseMap.get("color").toString());
        }
        
        if (promiseMap.containsKey("friendName")) {
            UserInfo promiseRequestUser = userService.getUserInfo(promiseMap.get("friendName").toString());
            promiseReqDTO.setMainUser(promiseRequestUser);
            promiseResDTO.setMainUser(userInfo);
        }

        promiseReqDTO.setPromiseStatus(-1);
        promiseReqDTO.setTargetUser(userInfo);
        promiseResDTO.setPromiseStatus(0);
        promiseResDTO.setTargetUser(userInfo);

        Promise promiseReq = modelMapper.map(promiseReqDTO, Promise.class);
        Promise promiseRes = modelMapper.map(promiseResDTO, Promise.class);

        promiseRepository.save(promiseReq);
        promiseRepository.save(promiseRes);
    }


    @Override
    public List<PromiseReqDTO> getPromiseReqDTOList(UserInfo userInfo, int promiseStatus) {
        List<Promise> promises = promiseRepository.findByMainUserAndPromiseStatus(userInfo,promiseStatus);
        List<PromiseReqDTO> promiseReqDTOList = new ArrayList<>();

        for(Promise promise : promises){
            PromiseReqDTO promiseReqDTO = new PromiseReqDTO();
            promiseReqDTO.entityToDTO(promise);
            promiseReqDTOList.add(promiseReqDTO);
        }

        return promiseReqDTOList;
    }
}

