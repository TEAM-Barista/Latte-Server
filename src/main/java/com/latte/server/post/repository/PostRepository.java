package com.latte.server.post.repository;

import com.latte.server.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Donggun on 2021-08-05
 */

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    @Query("select count(r) from Reply r join r.post p where p.id = :postId")
    int countReplies(@Param("postId") Long postId);

}
