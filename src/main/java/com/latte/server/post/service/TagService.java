package com.latte.server.post.service;


import com.latte.server.category.domain.Category;
import com.latte.server.category.repository.CategoryRepository;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.post.repository.TagRepository;
import com.latte.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.util.StringUtils.hasText;

/**
 * Created by Donggun on 2021-08-17
 */

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TagService {
    private static final String NOT_EXIST_POST = "[ERROR] No such Post";
    private static final String NOT_EXIST_CATEGORY = "[ERROR] No such Category";
    private static final String NOT_EXIST_TAG = "[ERROR] No such Tag";

    private final PostRepository postRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public Long addPostTag(Long postId, Long categoryId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_POST));

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_CATEGORY));

        Tag tag = Tag.createTag(post, category);
        post.addPostTag(tag);

        tagRepository.save(tag);

        return tag.getId();
    }

    public void delete(Long tagId) {
        Tag findTag = tagRepository.findById(tagId)
                .orElseThrow(() -> new IllegalArgumentException(NOT_EXIST_TAG));

        tagRepository.delete(findTag);
    }
}
