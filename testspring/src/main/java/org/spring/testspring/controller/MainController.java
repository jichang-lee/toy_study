package org.spring.testspring.controller;

import org.spring.testspring.config.UserPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String main (){
        return "메인 페이지";
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public String user (@AuthenticationPrincipal UserPrincipal userPrincipal){
        return "사용자 페이지 ㅇㅅㅇ";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public String admin (){
        return "관리자 페이지 -ㅅ- ";
    }
}
