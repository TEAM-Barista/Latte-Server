package com.latte.server.post.repository;

import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Reply;
import com.latte.server.post.domain.ReplyLike;
import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyLikeRepository extends JpaRepository<ReplyLike, Long> {
    Long countByReply(Reply reply);

    ReplyLike findByReply(Reply reply);

    ReplyLike findByReplyAndUser(Reply reply, User user);

}
