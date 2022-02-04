package elixter.blog.controller;

import elixter.blog.domain.Hashtag;
import elixter.blog.domain.Post;
import elixter.blog.dto.post.CreatePostRequestDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@Transactional
@RequestMapping(value = "/api/post")
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

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
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

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<GetPostResponseDto> GetAllPostsHandler(
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue
    ) {
        List<GetPostResponseDto> result = new ArrayList<>();
        List<Post> postList;

        LOGGER.debug("searchType : {}, searchValue : {}", searchType, searchValue);

        switch(searchType) {
            case "category":
                postList = postService.findPostByCategory(searchValue);
                break;
            case "hashtag":
                postList = postService.findPostByHashtag(searchValue);
                break;
            default:
                postList = postService.findPost();
                break;
        }

        for (Post post : postList) {
            GetPostResponseDto postResponse = new GetPostResponseDto();
            postResponse.postMapping(post);
            postResponse.hashtagMapping(hashtagService.findHashtagByPostId(post.getId()));
            result.add(postResponse);
        }

        return result;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Long PostCreatePostHandler(@RequestBody CreatePostRequestDto createPostBody) {
        LOGGER.debug("Request body : {}", createPostBody);

        Post post = createPostBody.PostMapping();
        Long postId = postService.createPost(post);
        post.setId(postId);

        List<Hashtag> hashtags = createPostBody.HashtagListMapping(post.getId());
        hashtagService.createHashtags(hashtags);

        return postId;
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void PutUpdatePostHandler(@RequestBody UpdatePostRequestDto updatePostBody) {
        LOGGER.debug("Request body : {}", updatePostBody);

        Post post = updatePostBody.PostMapping();
        postService.updatePost(post);

        List<Hashtag> hashtags = updatePostBody.HashtagListMapping();
        hashtagService.deleteHashtagById(post.getId());
        hashtagService.createHashtags(hashtags);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void DeletePostHandler(@PathVariable Long id) {
        LOGGER.debug("Target post id : {}", id);

        postService.deletePost(id);
        hashtagService.deleteHashtagByPostId(id);
    }
}
