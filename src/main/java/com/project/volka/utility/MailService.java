package com.project.volka.utility;

import com.project.volka.dto.UserInfoDTO;
import com.project.volka.entity.UserInfo;
import com.project.volka.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void sendId(UserInfoDTO userInfoDTO){
        SimpleMailMessage message = new SimpleMailMessage();
        String email = userInfoDTO.getUserEmail();
        message.setSubject("VOLKA ID를 잊으셨나요?");
        message.setTo(email);

        Optional<UserInfo> result = userRepository.findByUserEmail(email);

        if(result.isEmpty()){
            message.setText("해당 메일로 VOLKA를 가입한적이 없습니다.");
        }else{
            UserInfo userInfo = result.get();
            message.setText("해당 메일로 가입한 VOLKA의 ID는 "+ userInfo.getUserId() +" 입니다.");
        }

        javaMailSender.send(message);
    }

    public void sendTempPw(UserInfoDTO userInfoDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        String email = userInfoDTO.getUserEmail();
        message.setSubject("VOLKA Password를 잊으셨나요?");
        message.setTo(email);

        Optional<UserInfo> result = userRepository.findByUserEmail(email);

        if (result.isEmpty()) {
            message.setText("해당 메일로 VOLKA를 가입한 적이 없습니다.");
        } else {
            UserInfo userInfo = result.get();

            // 임시 비밀번호 생성
            String tempPassword = createTempPassword();
            log.info(tempPassword);
            userInfo.changePassword(passwordEncoder.encode(tempPassword));

            userRepository.save(userInfo);

            // 이메일로 임시 비밀번호 전송
            message.setText("해당 메일로 가입한 VOLKA의 임시PW는 " + tempPassword + " 입니다. 로그인 후 비밀번호 변경을 하시기 바랍니다.");
        }

        javaMailSender.send(message);
    }

    private String createTempPassword() {
        // 임시 비밀번호 생성 로직 (예: 랜덤 문자열 생성 또는 UUID 활용 등)
        return UUID.randomUUID().toString().replace("-","").substring(0,10).toLowerCase();
    }

}


