package com.project.volka.service.impl;


import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(UserInfoDTO userInfoDTO) throws MidExistException {
        String userId = userInfoDTO.getUserId();
        boolean exist = userRepository.existsById(userId);
        if(exist){
            throw new MidExistException();
        }
        UserInfo userInfo = modelMapper.map(userInfoDTO, UserInfo.class);
        userInfo.changePassword(passwordEncoder.encode(userInfoDTO.getUserPw()));
        log.info("=======================");
        log.info(userInfo);
        log.info(userInfo.getRoleSet());
        userRepository.save(userInfo);
    }

    @Override
    public void kakaoAddInfo(UserInfoDTO userInfoDTO) {
        String userId = userInfoDTO.getUserId();
        UserInfo userInfo = userRepository.findById(userInfoDTO.getUserId()).orElseThrow();
        userInfo.changeNickName(userInfoDTO.getUserNickName());
        userInfo.changePassword(passwordEncoder.encode(userInfoDTO.getUserPw()));
        userInfo.changeName(userInfoDTO.getUserName());
        userInfo.changePhone(userInfoDTO.getUserPhone());
        userRepository.save(userInfo);
    }


}

