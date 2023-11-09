package org.spring.testspring.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.spring.testspring.crypto.PasswordEncoder;
import org.spring.testspring.domain.User;
import org.spring.testspring.exception.AlreadyExistsEmailException;
import org.spring.testspring.exception.InvalidSigningInformation;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.Login;
import org.spring.testspring.requset.Signup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.rmi.AlreadyBoundException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입 성공")
    void test1(){


        Signup signup = Signup.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();

        authService.signup(signup);

        Assertions.assertEquals(1,userRepository.count());

        User user = userRepository.findAll().iterator().next();

        Assertions.assertEquals("jichang@naver.com",user.getEmail());
//        Assertions.assertNotEquals("1234",user.getPassword()); //<- 개선 방법 찾아보기
        Assertions.assertTrue(passwordEncoder.matches("1234", user.getPassword())); //after
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
    @Test
    @DisplayName("로그인 성공")
    void test3(){


        String encrypt = passwordEncoder.encrypt("1234");
        //given
        User saveUser = User.builder()
                .name("이름")
                .email("jichang@naver.com")
                .password(encrypt)
                .build();
        userRepository.save(saveUser);

        //when
        Login login = Login.builder()
                .email("jichang@naver.com")
                .password("1234")
                .build();

        Long userId = authService.signIn(login);

        //then
        Assertions.assertNotNull(userId);
    }
    @Test
    @DisplayName("로그인 패스워드 오류")
    void test4(){

        //given

        String encrypt = passwordEncoder.encrypt("1234");
        //given
        User saveUser = User.builder()
                .name("이름")
                .email("jichang@naver.com")
                .password(encrypt)
                .build();
        userRepository.save(saveUser);
        //expected
        Login login = Login.builder()
                .email("jichang@naver.com")
                .password("567")
                .build();
        Assertions.assertThrows(InvalidSigningInformation.class ,
                ()-> authService.signIn(login));




    }

}