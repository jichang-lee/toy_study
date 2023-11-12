package org.spring.testspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.spring.testspring.config.MasterMockUser;
import org.spring.testspring.domain.Post;
import org.spring.testspring.domain.User;
import org.spring.testspring.repository.PostRepository;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.PostCreate;
import org.spring.testspring.requset.PostEdit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
//    @MasterMockUser(name = "jichangLee" , email = "jichang@naver.com",password = "1234")
    @MasterMockUser
    @DisplayName("글 작성 성공")
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
                        .content(json))
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
        User user = User.builder()
                .email("jichang@naver.com")
                .password("1234")
                .name("이지창")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("123456789012345")
                .content("글 내용")
                .user(user)
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value(post.getContent()))
                .andDo(print());
    }



    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {

        //given
        User user = User.builder()
                .email("jichang@naver.com")
                .password("1234")
                .name("이지창")
                .build();
        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("글 제목 " + i)
                        .content("글 내용 " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
//                .andExpect(jsonPath("$[0].id").value(20))
                .andExpect(jsonPath("$[0].title").value("글 제목 19"))
                .andExpect(jsonPath("$[0].content").value("글 내용 19"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다")
    void test6() throws Exception {

        //given

        User user = User.builder()
                .email("jichang@naver.com")
                .password("1234")
                .name("이지창")
                .build();
        userRepository.save(user);

        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> Post.builder()
                        .title("글 제목 " + i)
                        .content("글 내용 " + i)
                        .user(user)
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
//                .andExpect(jsonPath("$[0].id").value(30))
                .andExpect(jsonPath("$[0].title").value("글 제목 19"))
                .andExpect(jsonPath("$[0].content").value("글 내용 19"))
                .andDo(print());
    }

    @Test
    @MasterMockUser
    @DisplayName("게시글 수정")
    void test7() throws Exception {

        User user = userRepository.findAll().get(0);
        //given
        Post post= Post.builder()
                .title("글 제목")
                .content("글 내용")
                .user(user)
                .build();
        postRepository.save(post);

        PostEdit postEdit= PostEdit.builder()
                .title("글 제목 수정")
                .content(post.getContent())
                .build();


        //expected
        mockMvc.perform(patch("/posts/{postId}" , post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @MasterMockUser
    @DisplayName("게시글 삭제")
    void test8() throws Exception {

        User user = userRepository.findAll().get(0);

        //given
        Post post= Post.builder()
                .title("글 제목")
                .content("글 내용")
                .user(user)
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}",post.getId())
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 (예외처리 테스트)")
    void test9() throws Exception{
        //expected
        mockMvc.perform(get("/posts/{postId}",2L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }




}