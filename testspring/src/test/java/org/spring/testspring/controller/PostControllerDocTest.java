package org.spring.testspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.spring.testspring.config.MasterMockUser;
import org.spring.testspring.domain.Post;
import org.spring.testspring.domain.User;
import org.spring.testspring.repository.post.PostRepository;
import org.spring.testspring.repository.user.UserRepository;
import org.spring.testspring.requset.post.PostCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(uriScheme = "https",uriHost = "v1.jichang.com",uriPort = 443)
@ExtendWith(RestDocumentationExtension.class)
public class PostControllerDocTest {

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
    @DisplayName("글 단건 조회")
    void test1() throws Exception {

        Post post = Post.builder()
                        .title("글 제목")
                        .content("글 내용").
                build();
        postRepository.save(post);


        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts/{postId}", 1L).
                        accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-inquiry", RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("postId").description("게시글 ID")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("id").description("게시글 ID"),
                                PayloadDocumentation.fieldWithPath("title").description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("content").description("게시글 내용")
                        )
                ));
    }
    @Test
    @DisplayName("글 여러개 조회")
    void test3() throws Exception {
        //given
        User user = User.builder()
                .email("jichang@naver.com")
                .password("1234")
                .name("이지창")
                .build();
        userRepository.save(user);

//        List<Post> posts = IntStream.range(0, 20)
//                .mapToObj(i -> Post.builder()
//                        .title("글 제목" + i)
//                        .content("글 내용" + i)
//                        .user(user)
//                        .build())
//                .collect(Collectors.toList());
//        postRepository.saveAll(posts);
        Post post = Post.builder()
                .title("123456789012345")
                .content("글 내용")
                .user(user)
                .build();
        postRepository.save(post);
        //expected
        mockMvc.perform(RestDocumentationRequestBuilders.get("/posts?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",Matchers.is(10)))
                .andExpect(jsonPath("$[0].title").value("글 제목19"))
                .andExpect(jsonPath("$[0].content").value("글 내용19"))
                .andDo(MockMvcResultHandlers.print())
                .andDo(document("posts-inquiry",RequestDocumentation.queryParameters(
                        RequestDocumentation.parameterWithName("page").description("페이지 번호"),
                        RequestDocumentation.parameterWithName("size").description("페이지 사이즈")
                ),
                        PayloadDocumentation.responseFields(
                                List.of(
                                PayloadDocumentation.fieldWithPath("id").description("게시글 ID"),
                                PayloadDocumentation.fieldWithPath("title").description("게시글 제목"),
                                PayloadDocumentation.fieldWithPath("content").description("게시글 내용")
                        ))
                ));

    }
    @Test
    @MasterMockUser
    @DisplayName("글 등록")
    void test2() throws Exception {

        PostCreate postCreate = PostCreate.builder()
                .title("글 내용")
                .content("글 제목")
                .build();

        String postJson = objectMapper.writeValueAsString(postCreate);

        mockMvc.perform(RestDocumentationRequestBuilders.post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(postJson)
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andDo(document("post-create",
                        PayloadDocumentation.requestFields(
                                PayloadDocumentation.fieldWithPath("title").description("게시글 제목")
                                                .attributes(Attributes.key("constraint").value("추가된 컬럼")),
                                PayloadDocumentation.fieldWithPath("content").description("게시글 내용").optional()
                        )
                ));
    }


}
