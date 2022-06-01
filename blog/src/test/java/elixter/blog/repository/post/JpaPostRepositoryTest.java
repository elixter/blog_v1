package elixter.blog.repository.post;

import elixter.blog.domain.post.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class JpaPostRepositoryTest {

    @Autowired @Qualifier("jpaPostRepository")
    PostRepository repository;

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

        Page<Post> result = repository.findByCategory("test category1", PageRequest.of(0, 20));

        assertThat(result.getContent().size()).isEqualTo(1);
        assertThat(result.getContent().get(0)).isEqualTo(post1);
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

        Page<Post> result = repository.findAll(PageRequest.of(0, 20, Sort.by(Sort.Order.desc("createAt"))));

        assertThat(result.getContent()).contains(post1);
        assertThat(result.getContent()).contains(post2);
    }
}
