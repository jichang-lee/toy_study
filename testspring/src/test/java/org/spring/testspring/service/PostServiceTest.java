package org.spring.testspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.testspring.domain.Post;
import org.spring.testspring.repository.PostRepository;
import org.spring.testspring.requset.PostCreate;
import org.spring.testspring.response.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;

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
        PostResponse postResponse = postService.get(post.getId());

        //then
        assertNotNull(post);
        assertEquals(1L,postResponse.getId());
        assertEquals("글 제목",postResponse.getTitle());
        assertEquals("글 내용",postResponse.getContent());

    }
//    @Test
//    @DisplayName("글 여려개 조회") -> Page 변경 예정
//    void test3 ()throws Exception{
//        // given
//        Post.builder()
//                .title("글 제목")
//                .content("글 내용")
//                .build();
//        postRepository.saveAll(List.of(
//                Post.builder()
//                        .title("글 제목")
//                        .content("글 내용")
//                        .build(),
//                Post.builder()
//                        .title("글 제목")
//                        .content("글 내용")
//                        .build()
//        ));
//
//        //when
//        List<PostResponse> postList = postService.getList();
//
//        //then
//        assertEquals(2L,postList.size());

//    }

    @Test
    @DisplayName("글 1page 조회")
    void test4 ()throws Exception{
        // given
        List<Post> requestPosts = IntStream.range(1,31)
                        .mapToObj(i->{
                         return    Post.builder()
                                    .title("글 제목 " + i)
                                    .content("글 내용 " + i)
                                    .build();
                        }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0,5, DESC,"id");

        //when
        List<PostResponse> postList = postService.getList(pageable);

        //then
        assertEquals(5L,postList.size());
        assertEquals("글 제목 30",postList.get(0).getTitle());
        assertEquals("글 제목 26",postList.get(4).getTitle());



    }


}