package com.adobe.orderapp.service;

import com.adobe.orderapp.dto.Post;
import io.micrometer.observation.annotation.Observed;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface PostService {
    @Observed(name="posts.find-all-posts", contextualName = "posts.find-all")
    @GetExchange("/posts")
    List<Post> findAll();

    @GetExchange("/posts/{id}")
    Post findById(@PathVariable Integer id);
}
