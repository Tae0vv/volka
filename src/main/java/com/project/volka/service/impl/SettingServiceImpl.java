package com.project.volka.service.impl;


import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.entity.UserRole;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.SettingService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;



    @Override
    public void changePw(PasswordDTO passwordDTO, User user) throws Exception {

        UserSecurityDTO userDTO = (UserSecurityDTO) user;
        UserInfo userInfo = userRepository.findById(userDTO.getUserId()).orElseThrow();

        if (passwordEncoder.matches(passwordDTO.getPassword(), userDTO.getUserPw())) {
            // 비밀번호 변경 로직
            userInfo.changePassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
            userRepository.save(userInfo);
        } else {
            throw new Exception("비밀번호가 일치하지 않습니다.");
        }
    }

    @Override
    public void changeInfo(UserInfoDTO userInfoDTO,User user) throws Exception {

        UserSecurityDTO userDTO = (UserSecurityDTO) user;
        UserInfo userInfo = userRepository.findById(userDTO.getUserId()).orElseThrow();

        userInfo.changeNickName(userInfoDTO.getUserNickName());
        userInfo.changeName(userInfoDTO.getUserName());
        userInfo.changePhone(userInfoDTO.getUserPhone());
        userRepository.save(userInfo);

    }

}

