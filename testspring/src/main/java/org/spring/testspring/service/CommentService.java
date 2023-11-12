package org.spring.testspring.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.spring.testspring.domain.Comment;
import org.spring.testspring.domain.Post;
import org.spring.testspring.exception.CommentNotFound;
import org.spring.testspring.exception.InvalidPassword;
import org.spring.testspring.exception.PostNotFound;
import org.spring.testspring.repository.comment.CommentRepository;
import org.spring.testspring.repository.post.PostRepository;
import org.spring.testspring.requset.comment.CommentCreate;
import org.spring.testspring.requset.comment.CommentDelete;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void write(Long postId, CommentCreate create) {
        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFound::new);

        String encryptedPassword = passwordEncoder.encode(create.getPassword());

        Comment comment = Comment.builder()
                .author(create.getAuthor())
                .password(encryptedPassword)
                .content(create.getContent())
                .build();

        post.addComment(comment);
    }

    public void delete(Long commentId, CommentDelete request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotFound::new);

//        String encryptedPassword = passwordEncoder.encode(comment.getPassword());
        String encryptedPassword = comment.getPassword();
        if(!passwordEncoder.matches(request.getPassword(), encryptedPassword)){
            throw new InvalidPassword();
        }

    }
}
