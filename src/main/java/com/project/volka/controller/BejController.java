package com.project.volka.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Log4j2
@RequestMapping("bej")
public class BejController {

    @GetMapping("/main")
    public void hello(){
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/test")
    public void test(){

    }
}
