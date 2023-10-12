package org.spring.testspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.testspring.domain.Post;
import org.spring.testspring.repository.PostRepository;
import org.spring.testspring.requset.PostCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("PostController result test")
    void test() throws Exception{

//        PostCreate request = new PostCreate("글 제목","글 내용");
        PostCreate request = PostCreate.builder()
                .title("글 제목")
                .content("글 내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        System.out.println(json);

        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)

                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }
    @Test
    @DisplayName("post 요청시 title값은 필수다")
    void test2() throws Exception{

        PostCreate request = PostCreate.builder()
                .content("글 내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다.(errorController)"))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }


    @Test
    @DisplayName("post 요청시 db저장")
    void test3() throws Exception{
        //given
        PostCreate request = PostCreate.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content("{\"title\" : \"글 제목\" , \"content\" : \"글 내용\"}")
        )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L,postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("글 제목",post.getTitle());
        assertEquals("글 내용",post.getContent());

    }
    //게시글 단건 조회
}
