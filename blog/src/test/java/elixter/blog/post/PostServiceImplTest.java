package elixter.blog.post;

import elixter.blog.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
        Post post = new Post(1L, "test", "test", "test", "test");
        postService.createPost(post);

        assertThat(postService).isInstanceOf(PostService.class);
        assertThat(postService.findPostById(1L)).isEqualTo(post);
        assertThat(postService.findPostById(2L)).isEqualTo(null);
    }
}
