package com.adobe.orderapp.api;

import com.adobe.orderapp.dto.Post;
import com.adobe.orderapp.dto.User;
import com.adobe.orderapp.service.AggregatorService;
import com.adobe.orderapp.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService; // bean
    private final AggregatorService aggregatorService;
    record PostDTO(String title, String user){}
    @GetMapping("/total")
    public  List<PostDTO> getPostDTO() {
        // below 2 are executing concurrently
        CompletableFuture<List<Post>> posts = aggregatorService.getPosts(); // non -blocking
        CompletableFuture<List<User>> users  = aggregatorService.getUsers(); // non - blocking

        // blocking code [ join() is used to specify that the caller thread has to wait for the other
        // thread to finish it's job
        List<Post> postList = posts.join(); // barrier
        List<User> userList = users.join(); // barrier

        return  postList.stream().map(post -> {
           String username = userList.stream().filter(user -> user.id() == post.userId()).findFirst().get().name();
            return new PostDTO(post.title(), username);
        }).collect(Collectors.toList());
    }

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
