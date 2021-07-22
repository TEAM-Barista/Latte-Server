package com.latte.server.post.api;

import com.latte.server.post.domain.Post;
import com.latte.server.post.dto.PostListDto;
import com.latte.server.post.repository.PostRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PostApiController {

    private final PostRepository postRepository;

    @GetMapping("/api/v1/postList")
    public Result postListV1() {
        List<Post> posts = postRepository.findAll();
        List<PostListDto> result = posts.stream()
                .map(p -> new PostListDto(p, postRepository.countReplies(p.getId()), postRepository.countPostLikes(p.getId())))
                .collect(Collectors.toList());

        return new Result(result.size(), result);
    }

    // data로 반환
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }
}
