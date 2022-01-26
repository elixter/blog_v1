package elixter.blog.controller;

import elixter.blog.domain.Post;
import elixter.blog.service.post.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/post")
public class PostController {
    private final PostService postService;
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Post> GetPostHandler(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Post> GetAllPostsHandler(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "hashtag", required = false) String hashtag
    ) {
        List<Post> result;

        LOGGER.debug("category : {}", category);

        if (category == null && hashtag == null) {
            result = postService.findPost();
        } else if (category != null) {
            result = postService.findPostByCategory(category);
        } else {
            result = postService.findPostByHashtag(hashtag);
        }

        return result;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void PostCreatePostHandler(@RequestBody Post createPostBody) {
        LOGGER.debug("Request body : {}", createPostBody);

        Long nextId = (long) postService.findPost().size() + 1;

        createPostBody.setId(nextId);

        postService.createPost(createPostBody);
    }

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