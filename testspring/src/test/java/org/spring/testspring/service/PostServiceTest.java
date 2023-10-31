package org.spring.testspring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.spring.testspring.domain.Post;
import org.spring.testspring.exception.PostNotFound;
import org.spring.testspring.repository.PostRepository;
import org.spring.testspring.requset.PostCreate;
import org.spring.testspring.requset.PostEdit;
import org.spring.testspring.requset.PostSearch;
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
        List<Post> requestPosts = IntStream.range(0,20)
                        .mapToObj(i->{
                         return    Post.builder()
                                    .title("글 제목 " + i)
                                    .content("글 내용 " + i)
                                    .build();
                        }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

       PostSearch postSearch= PostSearch.builder()
                                        .page(1)
                                        .build();

        //when
        List<PostResponse> postList = postService.getList(postSearch);

        //then
        assertEquals(10L,postList.size());
        assertEquals("글 제목 19",postList.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test5 ()throws Exception{
        // given
           Post post= Post.builder()
                    .title("글 제목")
                    .content("글 내용")
                    .build();
            postRepository.save(post);

       PostEdit postEdit= PostEdit.builder()
                        .title("글 제목 수정")
                        .content(post.getContent())
                        .build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("지정한 게시글이 존재하지 않는다"+post.getId()));
        assertEquals("글 제목 수정",changePost.getTitle());
        assertEquals("글 내용",changePost.getContent());




    }
    @Test
    @DisplayName("글 제목 수정시 null 로 보낼 때")
    void test6 ()throws Exception{
        // given
           Post post= Post.builder()
                    .title("글 제목")
                    .content("글 내용")
                    .build();
            postRepository.save(post);

       PostEdit postEdit= PostEdit.builder()
                        .title(null)
                        .content("글 내용 수정")
                        .build();

        //when
        postService.edit(post.getId(),postEdit);

        //then
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(()-> new RuntimeException("지정한 게시글이 존재하지 않는다"+post.getId()));
        assertEquals("글 제목",changePost.getTitle());
        assertEquals("글 내용 수정",changePost.getContent());

    }
    @Test
    @DisplayName("글 삭제")
    void test7 ()throws Exception{
        // given
        Post post= Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertEquals(0,postRepository.count());

    }

    @Test
    @DisplayName("id 조회 실패 케이스(예외처리 테스트)")
    void test8()throws Exception{
        // given
        Post post=Post.builder()
                .title("글 제목")
                .content("글 내용")
                .build();
        postRepository.save(post);

        //expected
            assertThrows(PostNotFound.class, () -> {
                postService.get(post.getId() + 1L);
        });
//        Assertions.assertThrows(NullPointerException.class,()->{
//            postService.get(post.getId() + 1L);
//        },"잘못된 예외처리 입니다.");
//        Assertions.assertEquals("존재하지 않는 게시글 입니다.",e.getMessage());



    }


}