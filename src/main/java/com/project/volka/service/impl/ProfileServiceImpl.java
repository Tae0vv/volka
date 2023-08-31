package com.project.volka.service.impl;


import com.project.volka.dto.ChatDTO;
import com.project.volka.dto.ChatRoomDTO;
import com.project.volka.entity.Chat;
import com.project.volka.entity.ChatRoom;
import com.project.volka.entity.Profile;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.ChatRoomRepository;
import com.project.volka.repository.ProfileRepository;
import com.project.volka.repository.UserRepository;
import com.project.volka.service.interfaces.ChatRoomService;
import com.project.volka.service.interfaces.ProfileService;
import com.project.volka.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    @Override
    @Transactional
    public void regProfile(UserInfo userInfo, MultipartFile file) {
        try {
            Profile profile = Profile.builder()
                    .userId(userInfo.getUserId())
                    .build();

            String profileNo = String.valueOf(profileRepository.save(profile).getProfileNo());

            String originalFileName = file.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf("."));

            String fileName = profileNo + extension;

            Path dirPath = Paths.get("C:/volka/profile/");

            if (!Files.exists(dirPath)) {
                Files.createDirectories(dirPath);
            }

            Path fullPath = dirPath.resolve(fileName);

            profile.changeFilePath(String.valueOf(fullPath));
            profileRepository.save(profile);

            file.transferTo(fullPath.toFile());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Profile getProfile(UserInfo userInfo){
        return profileRepository.getProfile(userInfo.getUserId());
    }

}

