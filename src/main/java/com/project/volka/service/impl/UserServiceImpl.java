package com.project.volka.service.impl;


import com.project.volka.dto.PasswordDTO;
import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.entity.UserRole;
import com.project.volka.repository.UserRepository;
import com.project.volka.security.dto.UserSecurityDTO;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(UserInfoDTO userInfoDTO) throws Exception {
        String userId = userInfoDTO.getUserId();
        boolean exist = userRepository.existsById(userId);

        if(exist){
            throw new Exception();
        }

        UserInfo userInfo = modelMapper.map(userInfoDTO, UserInfo.class);
        userInfo.changePassword(passwordEncoder.encode(userInfoDTO.getUserPw()));
        userInfo.addRole(UserRole.USER);
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

    @Override
    public UserInfo updateUserInfo(UserSecurityDTO user) {
        return userRepository.findById(user.getUserId()).orElseThrow();
    }

    @Override
    public void addKeyword(UserInfo userInfo, Map<String,Object> keywordMap) {
        String keywords = userInfo.getUserKeyword() == null ? "" : userInfo.getUserKeyword();
        String keyWordTitle = "";
        String keyWordColor = "";
        String addKeyword = "";

        if(keywordMap.containsKey("title") && keywordMap.containsKey("color")){
            keyWordTitle = (String) keywordMap.get("title");
            keyWordColor = (String) keywordMap.get("color");
            addKeyword = keyWordTitle + "=" + keyWordColor + "/";
        }

        keywords += addKeyword;
        userInfo.changeKeyword(keywords);
        userRepository.save(userInfo);
    }

    @Override
    public void deleteKeyword(UserInfo userInfo, Map<String, Object> keywordMap) {
        String keywords = userInfo.getUserKeyword();
        List<String> keywordList = new ArrayList<>(Arrays.asList(keywords.split("/")));

        String deleteKeyword = "";
        String deleteColor = "";
        String deleteKeywords = "";
        String changeKeyword = "";


        if(keywordMap.containsKey("keyword") && keywordMap.containsKey("color")){

            deleteKeyword = (String) keywordMap.get("keyword");
            deleteColor = (String) keywordMap.get("color");
            deleteKeywords = deleteKeyword + "=" + deleteColor;

            for(int i = 0; i < keywordList.size(); i++){

                if(keywordList.get(i).equals(deleteKeywords)){
                    keywordList.remove(i);
                    break;
                }
            }

            for(int i = 0; i < keywordList.size(); i++){
                changeKeyword += keywordList.get(i)+"/";
            }
            userInfo.changeKeyword(changeKeyword);
        }

        userRepository.save(userInfo);
    }

    @Override
    public String getUserNickName(String id) {
        UserInfo userInfo = userRepository.findById(id).orElseThrow();
        return userInfo.getUserNickName();
    }

    @Override
    public UserInfo getUserInfo(String nickName) {
        return userRepository.findByUserNickName(nickName);
    }
}

