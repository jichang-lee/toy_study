package org.spring.testspring.repository;

import org.spring.testspring.domain.Post;
import org.spring.testspring.requset.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
