package com.latte.server.post.repository;


import com.latte.server.interview.domain.Interview;
import com.latte.server.interview.domain.InterviewBookmark;
import com.latte.server.post.domain.Bookmark;
import com.latte.server.post.domain.Post;
import com.latte.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

/**
 * Created by Donggun on 2021-08-26
 */

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Bookmark findByPost(Post post);

    Long countByPost(Post post);

    Long countByPostAndUser(Post post, User user);

    Bookmark findTopByUserOrderByCreatedDateDesc(User user);
}
