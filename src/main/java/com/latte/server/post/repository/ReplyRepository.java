package com.latte.server.post.repository;

import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    Long countByPostAndIsDeleted(Post post, int isDeleted);

    List<Reply> findByPostAndIsDeleted(Post post, int isDeleted);
}
