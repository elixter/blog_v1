package elixter.blog.service;

import elixter.blog.AppConfig;
import elixter.blog.domain.Post;
import elixter.blog.service.post.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;

public class PostServiceImplTest {
    PostService postService;

    @BeforeEach
    public void beforeEach() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        postService = ac.getBean(PostService.class);
    }

    @Test
    void createPost() {
        Post post = new Post();

        post.setTitle("test");
        post.setContent("test");
        post.setCategory("test");
        post.setThumbnail("test");

        Long id = postService.createPost(post);
        post.setId(id);
        assertThat(postService).isInstanceOf(PostService.class);

        Post result1 = postService.findPostById(1L).get();
        assertThat(result1).isEqualTo(post);

        Assertions.assertThrows(NoSuchElementException.class, () -> {
           postService.findPostById(2L);
        });
    }
}
