package com.latte.server.post.service;

import com.latte.server.category.domain.Category;
import com.latte.server.post.domain.Post;
import com.latte.server.post.domain.Tag;
import com.latte.server.post.repository.PostRepository;
import com.latte.server.post.repository.TagRepository;
import com.latte.server.user.domain.User;
import com.latte.server.user.domain.UserRole;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

/**
 * Created by Donggun on 2021-08-19
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TagServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    TagService tagService;
    @Autowired
    TagRepository tagRepository;

    @Test
    public void 포스트에_태그_추가() {
        //given
        Post post = createPost();
        Category category = createCategory();
        Long postId = post.getId();
        Long categoryId = category.getId();

        //when
        Long tagId = tagService.addPostTag(postId, categoryId);

        //then
        List<Tag> tags = post.getPostTags();
        assertThat(tags.get(0).getId()).isEqualTo(tagId);
    }

    @Test
    public void 포스트에_태그_삭제() {
        //given
        String NOT_EXIST_TAG = "[ERROR] No such Tag";

        Post post = createPost();
        Category category = createCategory();
        Long postId = post.getId();
        Long categoryId = category.getId();
        Long tagId = tagService.addPostTag(postId, categoryId);

        // when
        tagService.delete(tagId);
        Optional<Tag> findTag = tagRepository.findById(tagId);

        //then
        assertThat(findTag).isEmpty();
    }

    private Post createPost() {
        User user = new User().builder()
                .userRole(UserRole.ROLE_ADMIN)
                .email("test@test.com")
                .nickName("userA")
                .password("$2a$10$GTHxsIH/0g0j/cv9MF9Iu.7mX.KMJvuGpDn/kMtBxIftCTgdsoLD6")
                .accessNotify(false)
                .build();
        em.persist(user);
        Post post = Post.createPost(user, "test content", "test title", "#stdio.h");
        em.persist(post);
        return post;
    }

    private Category createCategory() {
        Category category = Category.createCategory("test category", "test kind");
        em.persist(category);
        return category;
    }
}
