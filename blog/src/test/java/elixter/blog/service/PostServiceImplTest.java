package elixter.blog.service;

import elixter.blog.domain.post.Post;
import elixter.blog.service.post.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PostServiceImplTest {
    @Autowired
    PostService postService;

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

        Post result1 = postService.findPostById(id).get();
        post.setCreateAt(result1.getCreateAt());
        post.setUpdateAt(result1.getUpdateAt());
        assertThat(result1).isEqualTo(post);

        assertThat(postService.findPostById(id + 104340).isEmpty()).isEqualTo(true);
    }

    @Test
    void updatePost() {
        Post post = new Post();

        post.setTitle("test");
        post.setContent("test");
        post.setCategory("test");
        post.setThumbnail("test");

        Long id = postService.createPost(post);
        post.setId(id);

        System.out.println("post = " + post);

        post.setContent("update test");
        postService.updatePost(post);

        Post result = postService.findPostById(id).get();

        post.setCreateAt(result.getCreateAt());
        post.setUpdateAt(result.getUpdateAt());

        System.out.println("result = " + result);

        assertThat(result).isEqualTo(post);
    }

    @Test
    void findPostByCategory() {
        Post post = new Post();
        post.setTitle("test");
        post.setContent("test");
        post.setCategory("test");
        post.setThumbnail("test");
        Long id = postService.createPost(post);
        post.setId(id);

        Post post2 = new Post();
        post2.setTitle("test");
        post2.setContent("test");
        post2.setCategory("test_category");
        post2.setThumbnail("test");
        Long id2 = postService.createPost(post2);
        post2.setId(id2);

        Post post3 = new Post();
        post3.setTitle("test");
        post3.setContent("test");
        post3.setCategory("test");
        post3.setThumbnail("test");
        Long id3 = postService.createPost(post3);
        post3.setId(id3);

        Post post4 = new Post();
        post4.setTitle("test");
        post4.setContent("test");
        post4.setCategory("test_category");
        post4.setThumbnail("test");
        Long id4 = postService.createPost(post4);
        post4.setId(id4);

        List<Post> result1 = postService.findPostByCategory("test", 0L, 1000L);
        List<Post> result2 = postService.findPostByCategory("test_category", 0L, 1000L);

        List<Post> expect1 = new ArrayList<>();
        expect1.add(post);
        expect1.add(post3);

        List<Post> expect2 = new ArrayList<>();
        expect2.add(post2);
        expect2.add(post4);

        assertThat(result1).isEqualTo(expect1);
        assertThat(result2).isEqualTo(expect2);
    }
}
