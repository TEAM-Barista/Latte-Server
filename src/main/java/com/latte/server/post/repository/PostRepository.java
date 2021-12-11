package com.latte.server.post.repository;

import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Donggun on 2021-08-05
 */

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Post findTopByUserOrderByCreatedDateDesc(User user);
}
