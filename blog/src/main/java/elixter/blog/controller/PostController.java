package elixter.blog.controller;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.dto.post.UpdatePostRequestDto;
import elixter.blog.exception.InvalidBodyFieldException;
import elixter.blog.exception.RestException;
import elixter.blog.exception.post.PostNotFoundException;
import elixter.blog.service.hashtag.HashtagService;
import elixter.blog.service.image.ImageService;
import elixter.blog.service.post.PostService;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
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

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
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
    public ResponseEntity<GetAllPostsResponseDto> GetAllPostsHandler(
            @RequestParam(value = "filterType", required = false) String filterType,
            @RequestParam(value = "filterString", required = false) String filterString,
            @PageableDefault Pageable pageable
    ) {
        log.info("filterType : {}, filterString : {}", filterType, filterString);

        GetAllPostsResponseDto result = postService.findAllPost(filterType, filterString, pageable);

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Object> PostCreatePostHandler(@Validated @RequestBody CreatePostRequestDto createPostBody, BindingResult bindingResult) {

        log.info("Request body : {}", createPostBody);

        if (bindingResult.hasFieldErrors()) {
            log.info("field error : {}", bindingResult.getFieldErrors());
            throw new InvalidBodyFieldException(bindingResult.getFieldErrors());
        }

        Post createdPost = postService.createPost(createPostBody);

        return ResponseEntity.created(URI.create("/blog/posts/" + createdPost.getId())).build();
    }

    @PutMapping
    public ResponseEntity<Object> PutUpdatePostHandler(@RequestBody UpdatePostRequestDto updatePostBody) {

        log.info("Request body : {}", updatePostBody);

        postService.updatePost(updatePostBody.PostMapping());

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> DeletePostHandler(@PathVariable Long id) {

        log.info("Target post id : {}", id);

        postService.deletePost(id);

        return ResponseEntity.ok().build();
    }
}
