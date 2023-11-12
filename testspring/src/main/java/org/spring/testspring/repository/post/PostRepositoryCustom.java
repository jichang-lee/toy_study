package org.spring.testspring.repository.post;

import org.spring.testspring.domain.Post;
import org.spring.testspring.requset.post.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
