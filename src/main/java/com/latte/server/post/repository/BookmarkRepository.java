package com.latte.server.post.repository;


import com.latte.server.post.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Donggun on 2021-08-26
 */

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
}
