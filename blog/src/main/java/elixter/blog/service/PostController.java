package elixter.blog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

        Post post = new Post(1L, "test", "test", "test", "test");
        postService.createPost(post);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Optional<Post> GetPostHandler(@PathVariable Long id) {
        return postService.findPostById(id);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Post> GetAllPostsHandler(@RequestParam(value = "category", required = false) String category) {
        List<Post> result;

        LOGGER.debug("category : {}", category);

        if (category == null) {
            result = postService.findPost();
        } else {
            result = postService.findPostByCategory(category);
        }

        return result;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public void PostCreatePostHandler(@RequestBody Post createPostBody) {
        LOGGER.debug("Request body : {}", createPostBody);

        Long nextId = (long)postService.findPost().size() + 1;

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
