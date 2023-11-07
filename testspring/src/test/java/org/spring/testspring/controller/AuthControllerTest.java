package org.spring.testspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.testspring.domain.Session;
import org.spring.testspring.domain.User;
import org.spring.testspring.repository.SessionRepository;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.Login;
import org.spring.testspring.requset.Signup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTst {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception{

        User user = User.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("jichang@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성")
    void test2() throws Exception{

        User user = User.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("jichang@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))

                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath())
                .andDo(MockMvcResultHandlers.print());


        Assertions.assertEquals(1L, user.getSession().size());

    }

    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void test3() throws Exception{

        User user = User.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("jichang@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken",Matchers.notNullValue()))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지로 접속 /foo")
    void test4 () throws  Exception{
        User user = User.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);


        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization",session.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 요청시 권한이 필요한 페이지에 접속할 수 없다")
    void test5 () throws  Exception{
        User user = User.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();
        Session session = user.addSession();
        userRepository.save(user);


        mockMvc.perform(MockMvcRequestBuilders.get("/foo")
                        .header("Authorization",session.getAccessToken()+"-O")
                        .contentType(MediaType.APPLICATION_JSON))

                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("회원가입")
    void test6 () throws  Exception{
        Signup signup = Signup.builder()
                .name("이지창")
                .email("jichang@naver.com")
                .password("1234")
                .build();


        mockMvc.perform(MockMvcRequestBuilders.post("/auth/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }



}