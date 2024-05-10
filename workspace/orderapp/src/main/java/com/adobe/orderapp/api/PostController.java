package com.adobe.orderapp.api;

import com.adobe.orderapp.dto.Post;
import com.adobe.orderapp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService; // bean

    @GetMapping
    public List<Post> getPosts() {
        return  postService.findAll();
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable int id) {
        postService.findAll(); // dummy call to service
        return  postService.findById(id);
    }

}
