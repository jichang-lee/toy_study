package org.spring.testspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.testspring.domain.Post;
import org.spring.testspring.repository.PostRepository;
import org.spring.testspring.requset.PostCreate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("글 작성")
    void test1 (){
        // given
        PostCreate postCreate = PostCreate.builder()
                .title("글 제목")
                .content("글 내용")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L,postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("글 제목",post.getTitle());
        assertEquals("글 내용",post.getContent());
    }

    @Test
    @DisplayName("id 조회")
    void test2()throws Exception{
        // given
        Post post=Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        //when
        Post findPost = postService.get(post.getId());
        //then
        assertNotNull(post);
        assertEquals("글 제목",post.getTitle());
        assertEquals("글 내용",post.getContent());

    }


}