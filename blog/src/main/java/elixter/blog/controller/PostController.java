package elixter.blog.controller;

import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.post.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
import elixter.blog.dto.post.GetAllPostsResponseDto;
import elixter.blog.dto.post.GetPostResponseDto;
import elixter.blog.dto.post.UpdatePostRequestDto;
import elixter.blog.service.hashtag.HashtagService;
import elixter.blog.service.post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

// TODO: 이미지 처리, hashtag 검색
@RestController
@Transactional
//@CrossOrigin(origins = "http://localhost", allowCredentials = "true")
@RequestMapping(value = "/api/posts")
public class PostController {

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Data not found")
    public class DataNotFoundException extends RuntimeException {

    }

    private final PostService postService;
    private final HashtagService hashtagService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostController(PostService postService, HashtagService hashtagService) {
        this.postService = postService;
        this.hashtagService = hashtagService;
    }

    @GetMapping("/{id}")
    public GetPostResponseDto GetPostHandler(@PathVariable Long id) {
        GetPostResponseDto result = new GetPostResponseDto();
        try {
            Post post = postService.findPostById(id).get();
            result.postMapping(post);
        }
        catch (NoSuchElementException e) {
            throw new DataNotFoundException();
        }

        List<Hashtag> hashtags = hashtagService.findHashtagByPostId(id);
        result.hashtagMapping(hashtags);

        return result;
    }

    @GetMapping
    public GetAllPostsResponseDto GetAllPostsHandler(
            @RequestParam(value = "curPage", required = false) Number curPage,
            @RequestParam(value = "pageSize", required = false) Number pageSize,
            @RequestParam(value = "filterType", required = false) String filterType,
            @RequestParam(value = "filterString", required = false) String filterString
    ) {
        GetAllPostsResponseDto result = new GetAllPostsResponseDto();
        List<Post> postList;

        LOGGER.debug("curPage : {}, pageSize: {}, filterType : {}, filterString : {}", curPage, pageSize, filterType, filterString);

        Long offset = curPage.longValue() * pageSize.longValue();
        Long limit = offset + pageSize.longValue();

        switch(filterType) {
            case "category":
                postList = postService.findPostByCategory(filterString, offset, limit);
                break;
            case "hashtag":
                postList = postService.findPostByHashtag(filterString, offset, limit);
                break;
            default:
                postList = postService.findPost(offset, limit);
                break;
        }

        for (Post post : postList) {
            GetPostResponseDto postResponse = new GetPostResponseDto();
            postResponse.postMapping(post);
            postResponse.hashtagMapping(hashtagService.findHashtagByPostId(post.getId()));
            result.getPosts().add(postResponse);
        }

        return result;
    }

    @PostMapping
    public Long PostCreatePostHandler(@RequestBody CreatePostRequestDto createPostBody) {
        LOGGER.debug("Request body : {}", createPostBody);

        Post post = createPostBody.PostMapping();
        Long postId = postService.createPost(post);
        post.setId(postId);

        List<Hashtag> hashtags = createPostBody.HashtagListMapping(post.getId());
        hashtagService.createHashtags(hashtags);

        return postId;
    }

    @PutMapping
    public void PutUpdatePostHandler(@RequestBody UpdatePostRequestDto updatePostBody) {
        LOGGER.debug("Request body : {}", updatePostBody);

        Post post = updatePostBody.PostMapping();
        postService.updatePost(post);

        List<Hashtag> hashtags = updatePostBody.HashtagListMapping();
        hashtagService.deleteHashtagById(post.getId());
        hashtagService.createHashtags(hashtags);
    }

    @DeleteMapping("/{id}")
    public void DeletePostHandler(@PathVariable Long id) {
        LOGGER.debug("Target post id : {}", id);

        postService.deletePost(id);
        hashtagService.deleteHashtagByPostId(id);
    }
}
