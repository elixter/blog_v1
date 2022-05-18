package elixter.blog.controller;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.dto.post.UpdatePostRequestDto;
import elixter.blog.exception.post.PostNotFoundException;
import elixter.blog.service.hashtag.HashtagService;
import elixter.blog.service.image.ImageService;
import elixter.blog.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@RestController
@Transactional
@RequestMapping(value = "/api/posts")
public class PostController {

    private final PostService postService;
    private final HashtagService hashtagService;

    @Autowired
    public PostController(PostService postService, HashtagService hashtagService) {
        this.postService = postService;
        this.hashtagService = hashtagService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetPostResponseDto> GetPostHandler(@PathVariable Long id) {
        GetPostResponseDto result = postService.findPostById(id);
        if (result.isEmpty()) {
            throw new PostNotFoundException(id);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<Object> GetAllPostsHandler(
            @RequestParam(value = "filterType", required = false) String filterType,
            @RequestParam(value = "filterString", required = false) String filterString,
            @PageableDefault Pageable pageable
    ) {
        log.info("filterType : {}, filterString : {}", filterType, filterString);

        GetAllPostsResponseDto result = postService.findAllPost(filterType, filterString, pageable);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> PostCreatePostHandler(@RequestBody CreatePostRequestDto createPostBody) {

        log.info("Request body : {}", createPostBody);

        Post createdPost = postService.createPost(createPostBody);

        List<Hashtag> hashtags = createPostBody.hashtagListMapping(createdPost.getId());
        hashtagService.createHashtags(hashtags);

        return ResponseEntity.created(URI.create("/blog/posts/" + createdPost.getId())).build();
    }

    @PutMapping
    public void PutUpdatePostHandler(@RequestBody UpdatePostRequestDto updatePostBody) {

        log.info("Request body : {}", updatePostBody);

        postService.updatePost(updatePostBody.PostMapping());
    }

    @DeleteMapping("/{id}")
    public void DeletePostHandler(@PathVariable Long id) {

        log.info("Target post id : {}", id);

        postService.deletePost(id);
        hashtagService.deleteHashtagByPostId(id);
    }
}
