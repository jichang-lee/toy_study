package org.spring.testspring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.testspring.domain.User;
import org.spring.testspring.exception.InvalidRequest;
import org.spring.testspring.exception.InvalidSigningInformation;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.Login;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody User user){
        //json 아이디(email) & 비밀번호
         log.info("login ==>>>>>>" , user);

        //DB에서 조회
        User user1 = userRepository.findByEmailAndPassword(user.getEmail(), user.getPassword())
                .orElseThrow(InvalidSigningInformation::new);


        //토큰을 응답
        return user1;

    }

}
