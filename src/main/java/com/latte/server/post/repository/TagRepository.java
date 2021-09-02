package com.latte.server.post.repository;

import com.latte.server.post.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Donggun on 2021-08-17
 */

public interface TagRepository extends JpaRepository<Tag, Long> {
}


