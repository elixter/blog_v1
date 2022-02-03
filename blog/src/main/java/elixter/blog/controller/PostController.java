package elixter.blog.controller;

import elixter.blog.domain.Hashtag;
import elixter.blog.domain.Post;
import elixter.blog.dto.CreatePostRequestDto;
import elixter.blog.dto.GetPostResponseDto;
import elixter.blog.service.hashtag.HashtagService;
import elixter.blog.service.post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

// TODO: 한글 꺠짐 현성과 Post 요청했을 경우 헤더 파싱 안된다는 에러 고쳐야함

@RestController
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
        GetPostResponseDto responseBody = new GetPostResponseDto();
        try {
            Post post = postService.findPostById(id).get();
            responseBody.postMapping(post);
        }
        catch (NoSuchElementException e) {
            throw new DataNotFoundException();
        }

        List<Hashtag> hashtags = hashtagService.findHashtagByPostId(id);
        responseBody.hashtagMapping(hashtags);

        return responseBody;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Post> GetAllPostsHandler(
            @RequestParam(value = "searchType", required = false) String searchType,
            @RequestParam(value = "searchValue", required = false) String searchValue
    ) {
        List<Post> result;

        LOGGER.debug("searchType : {}, searchValue : {}", searchType, searchValue);

        switch(searchType) {
            case "category":
                result = postService.findPostByCategory(searchValue);
                break;
            case "hashtag":
                result = postService.findPostByHashtag(searchValue);
                break;
            default:
                result = postService.findPost();
                break;
        }

        return result;
    }

    @Transactional
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Post PostCreatePostHandler(@RequestBody CreatePostRequestDto createPostBody) {
        LOGGER.debug("Request body : {}", createPostBody);

        Post post = createPostBody.PostMapping();
        Long postId = postService.createPost(post);
        post.setId(postId);

        List<Hashtag> hashtags = createPostBody.HashtagListMapping(post.getId());
        hashtagService.createHashtags(hashtags);

        return post;
    }

    @Transactional
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public void PutUpdatePostHandler(@RequestBody Post updatePostBody) {
        LOGGER.debug("Request body : {}", updatePostBody);

        postService.updatePost(updatePostBody);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void DeletePostHandler(@PathVariable Long id) {
        LOGGER.debug("Target post id : {}", id);

        postService.deletePost(id);
    }
}
