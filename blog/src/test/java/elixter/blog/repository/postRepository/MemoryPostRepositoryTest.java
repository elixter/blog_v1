package elixter.blog.repository.postRepository;

import elixter.blog.domain.post.Post;
import elixter.blog.repository.post.MemoryPostRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MemoryPostRepositoryTest {
    MemoryPostRepository repository = new MemoryPostRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    @DisplayName("저장 테스트")
    public void saveTest() {
        Post post = new Post();
        post.setTitle("Test title");
        post.setContent("This is test content");
        post.setCategory("test category");
        post.setThumbnail("http://www.testimage.com");

        repository.save(post);
        Post result = repository.findById(post.getId()).get();
        assertThat(post).isEqualTo(result);
    }

    @Test
    @DisplayName("카테고리로 검색 테스트")
    public void findByCategoryTest() {
        Post post1 = new Post();
        post1.setTitle("Test title1");
        post1.setContent("This is test content1");
        post1.setCategory("test category1");
        post1.setThumbnail("http://www.testimage.com");
        repository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Test title2");
        post2.setContent("This is test content2");
        post2.setCategory("test category2");
        post2.setThumbnail("http://www.testimage.com");
        repository.save(post2);

        List<Post> result = repository.findByCategory("test category1", 0L, 1000L);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo(post1);
    }

    @Test
    public void findAllTest() {
        Post post1 = new Post();
        post1.setTitle("Test title1");
        post1.setContent("This is test content1");
        post1.setCategory("test category1");
        post1.setThumbnail("http://www.testimage.com");
        repository.save(post1);

        Post post2 = new Post();
        post2.setTitle("Test title2");
        post2.setContent("This is test content2");
        post2.setCategory("test category2");
        post2.setThumbnail("http://www.testimage.com");
        repository.save(post2);

        List<Post> result = repository.findAll(0L, 1000L);

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(post1);
        assertThat(result).contains(post2);
    }
}
