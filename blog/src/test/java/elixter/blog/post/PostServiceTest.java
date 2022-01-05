package elixter.blog.post;

import elixter.blog.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PostServiceTest {

    PostService postService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        postService = appConfig.postService();
    }

    @Test
    @DisplayName("게시글 조회 테스트")
    void createPost() {
        Long postId = 1L;
        Post post = new Post(postId, "테스트", "테스트", "카테고리", "img.com");
        postService.createPost(post);

        Post foundPost = postService.findPost(postId);
        Assertions.assertThat(foundPost).isEqualTo(post);
    }

    @Test
    @DisplayName("카테고리 게시글 조회 테스트")
    void findAllByCategory() {
        Post post1 = new Post(1L, "테스트", "테스트", "카테고리", "img.com");
        Post post2 = new Post(2L, "테스트", "테스트", "카테고리", "img.com");
        Post post3 = new Post(3L, "테스트", "테스트", "카테고리1", "img.com");
        Post post4 = new Post(4L, "테스트", "테스트", "카테고리2", "img.com");
        Post post5 = new Post(5L, "테스트", "테스트", "카테고리3", "img.com");

        postService.createPost(post1);
        postService.createPost(post2);
        postService.createPost(post3);
        postService.createPost(post4);
        postService.createPost(post5);

        ArrayList<Post> postsByCategory = postService.findAllPostsByCategory("카테고리");

        Assertions.assertThat(postsByCategory.size()).isEqualTo(2);
    }
}
