package com.latte.server.post.repository;

import com.latte.server.post.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        em.persist(post);
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    // fetch join
    public List<Post> findAll() {
        return em.createQuery(
                "select p from Post p" +
                        " join fetch p.user u", Post.class
        ).getResultList();
    }

}