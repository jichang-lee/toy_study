package org.spring.testspring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.spring.testspring.domain.User;
import org.spring.testspring.exception.AlreadyExistsEmailException;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.Signup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.rmi.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입 성공")
    void test1(){

        //given
        Signup signup = Signup.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();

        //when
        authService.signup(signup);

        //then
        Assertions.assertEquals(1,userRepository.count());

        User user = userRepository.findAll().iterator().next();
        Assertions.assertEquals("jichang@naver.com",user.getEmail());
        Assertions.assertNotEquals("1234",user.getPassword()); //<- 개선 방법 찾아보기
        Assertions.assertEquals("이지창",user.getName());

    }
    @Test
    @DisplayName("회원가입시 중복 이메일 예외")
    void test2(){

        User saveUser = User.builder()
                .name("이름")
                .email("jichang@naver.com")
                .password("1234")
                .build();
        userRepository.save(saveUser);


        //given
        Signup signup = Signup.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();
        //expected
        Assertions.assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));


    }

}