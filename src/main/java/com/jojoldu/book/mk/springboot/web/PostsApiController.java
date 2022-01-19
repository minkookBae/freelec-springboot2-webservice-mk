package com.jojoldu.book.mk.springboot.web;

import com.jojoldu.book.mk.springboot.domain.posts.PostsRepository;
import com.jojoldu.book.mk.springboot.service.posts.PostsService;
import com.jojoldu.book.mk.springboot.web.dto.PostsResponseDto;
import com.jojoldu.book.mk.springboot.web.dto.PostsSaveRequestDto;
import com.jojoldu.book.mk.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }

    @PutMapping("/api/v1/posts/{id}")
    public long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }

}
