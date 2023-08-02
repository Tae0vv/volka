package com.project.volka.service.interfaces;

import com.project.volka.dto.PasswordDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;

public interface SettingService {
    void changePw(PasswordDTO passwordDTO, @AuthenticationPrincipal User user) throws Exception;

}
