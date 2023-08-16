package com.project.volka.service.impl;


import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.Promise;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.PromiseService;
import com.project.volka.service.interfaces.SettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Log4j2
@Service
@RequiredArgsConstructor
public class PromiseServiceImpl implements PromiseService {

    @Override
    public Promise makePromise(UserInfo userInfo, HashMap<String, Object> promiseInfo) {
        
        return null;
    }
}

