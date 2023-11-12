package org.spring.testspring.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.testspring.domain.Comment;
import org.spring.testspring.domain.Post;
import org.spring.testspring.domain.User;
import org.spring.testspring.repository.comment.CommentRepository;
import org.spring.testspring.repository.post.PostRepository;
import org.spring.testspring.repository.user.UserRepository;
import org.spring.testspring.requset.comment.CommentCreate;
import org.spring.testspring.requset.comment.CommentDelete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 작성 성공")
    void test1() throws Exception {
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

        CommentCreate comment = CommentCreate.builder()
                .author("이지창")
                .password("1234566")
                .content("댓글 내용 제한 최소 10글자!!!")
                .build();

        String json = objectMapper.writeValueAsString(comment);

        //expected
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/{postId}/comments",post.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        Assertions.assertEquals(1L,commentRepository.count());

        Comment saveComment = commentRepository.findAll().get(0);
        Assertions.assertEquals("이지창",saveComment.getAuthor());
        Assertions.assertNotEquals("1234566",saveComment.getPassword());
        Assertions.assertTrue(passwordEncoder.matches("1234566" , saveComment.getPassword()));
        Assertions.assertEquals("댓글 내용 제한 최소 10글자!!!",saveComment.getContent());


    }
    @Test
    @DisplayName("댓글 삭제 성공")
    void test2() throws Exception {
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

        String encode = passwordEncoder.encode("1234566");

        Comment comment = Comment.builder()
                .author("이지창")
                .password(encode)
                .content("댓글 내용 제한 최소 10글자!!!")
                .build();
        comment.setPost(post);
        commentRepository.save(comment);

        CommentDelete commentDelete = new CommentDelete("1234566");
        String json = objectMapper.writeValueAsString(commentDelete);

        // exepected
        mockMvc.perform(MockMvcRequestBuilders.post("/comment/{commentId}/delete",comment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }

}