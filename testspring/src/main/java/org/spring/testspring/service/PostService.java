package org.spring.testspring.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.spring.testspring.domain.Post;
import org.spring.testspring.domain.PostEditor;
import org.spring.testspring.domain.User;
import org.spring.testspring.exception.PostNotFound;
import org.spring.testspring.exception.UserNotFound;
import org.spring.testspring.repository.post.PostRepository;
import org.spring.testspring.repository.user.UserRepository;
import org.spring.testspring.requset.post.PostCreate;
import org.spring.testspring.requset.post.PostEdit;
import org.spring.testspring.requset.post.PostSearch;
import org.spring.testspring.response.PostResponse;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void write(Long userId ,PostCreate postCreate){

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                .user(user)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
       postRepository.save(post);

    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

                return    PostResponse.builder()
                                .id(post.getId())
                                .title(post.getTitle())
                                .content(post.getContent())
                                .build();

    }

    public List<PostResponse> getList(PostSearch postSearch) {
//        Pageable pageable = PageRequest.of(page , 5,Sort.by(Sort.Direction.DESC,"id"));

       return postRepository.getList(postSearch).stream()
               .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, PostEdit postEdit){
       Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit(postEditor);
//        post.edit(postEdit.getTitle() != null ? postEdit.getTitle() : post.getTitle()
//                , postEdit.getContent() != null ? postEdit.getContent() : post.getContent());
    }

    public void delete(Long id){
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        postRepository.delete(post);
    }
}
