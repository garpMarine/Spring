package com.adobe.orderapp.service;

import com.adobe.orderapp.dto.Post;
import com.adobe.orderapp.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.DelegatingFilterProxy;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class AggregatorService {
    private final PostService postService;
    private final UserService userService;

    @Async("servicePool")
    public CompletableFuture<List<Post>> getPosts() {
        System.out.println(Thread.currentThread() + " for getPosts()");
        return CompletableFuture.completedFuture(postService.findAll());
    }


    @Async("servicePool")
    public CompletableFuture<List<User>> getUsers() {
        System.out.println(Thread.currentThread() + " for getUsers()");
        return CompletableFuture.completedFuture(userService.getUsers());
    }


}
