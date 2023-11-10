package org.spring.testspring.controller;
import lombok.RequiredArgsConstructor;
import org.spring.testspring.requset.Signup;
import org.spring.testspring.service.AuthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/auth/login")
    public String login(){
        return "로그인 페이지 테스트";
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup ){
        authService.signup(signup);
    }

}
