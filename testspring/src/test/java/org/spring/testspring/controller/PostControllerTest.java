package org.spring.testspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    void clean() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("PostController result test")
    void test() throws Exception {

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
    void test2() throws Exception {

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
    void test3() throws Exception {
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
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("글 제목", post.getTitle());
        assertEquals("글 내용", post.getContent());

    }

    //게시글 단건 조회
    @Test
    @DisplayName("글 단건 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("글 내용")
                .build();
        postRepository.save(post);
        String json = objectMapper.writeValueAsString(post);


        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 단건 조회")
    void test5() throws Exception {
        //given
        Post post1 = Post.builder()
                .title("글 제목11111")
                .content("글 내용")
                .build();
        postRepository.save(post1);

        Post post2 = Post.builder()
                .title("글 제목22222")
                .content("글 내용")
                .build();
        postRepository.save(post2);


        //expected
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)))
                .andExpect(jsonPath("$.[0].id").value(post1.getId()))
                .andExpect(jsonPath("$.[0].title").value(post1.getTitle()))
                .andExpect(jsonPath("$.[0].content").value(post1.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("글 제목 " + i)
                        .content("글 내용 " + i)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&sort=id,desc")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(5)))
                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("글 제목 30"))
                .andExpect(jsonPath("$[0].content").value("글 내용 30"))
                .andDo(print());
    }



}