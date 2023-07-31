package com.project.volka.repositoy;

import com.project.volka.entity.UserInfo;
import com.project.volka.entity.UserRole;
import com.project.volka.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertUsers(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            UserInfo userInfo = UserInfo.builder()
                    .userId("user"+i)
                    .userPw(passwordEncoder.encode("1111"))
                    .userName("이름" + i)
                    .userNickName("nick"+i)
                    .userOn(false)
                    .userPhone("01011110"+i)
                    .userEmail("email"+i+"@aaa.bbb")
                    .userStatus(0)
                    .build();
            userInfo.addRole(UserRole.USER);

            if(i == 1){
                userInfo.addRole(UserRole.ADMIN);
            }
            userRepository.save(userInfo);
        });
    }

    @Test
    public void testRead(){
        Optional<UserInfo> result = userRepository.getWithRoles("user1");
        UserInfo userInfo = result.orElseThrow();

        log.info(userInfo);
        log.info(userInfo.getRoleSet());

        userInfo.getRoleSet().forEach(userRole->log.info(userRole.name()));
    }

}
