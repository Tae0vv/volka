//package com.project.volka.service.impl;
//
//
//import com.project.volka.dto.UserInfoDTO;
//import com.project.volka.entity.UserInfo;
//import com.project.volka.repository.UserRepository;
//import com.project.volka.service.interfaces.MailService;
//import com.project.volka.service.interfaces.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.modelmapper.ModelMapper;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//
//@Log4j2
//@Service
//@RequiredArgsConstructor
//public class MailServiceImpl implements MailService {
//
//    private final ModelMapper modelMapper;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//
//    @Override
//    public void sendMail() {
//
//        ArrayList<String> to = new ArrayList<>();
//        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
//        JavaMailSender.send(simpleMailMessage);
//    }
//}
//
