package elixter.blog.repository.postImage;

import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.domain.postImage.PostImage;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.post.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@SpringBootTest
public class PostImageRepositoryTest {

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;
    private final ImageRepository imageRepository;

    @Autowired
    public PostImageRepositoryTest(
            PostImageRepository postImageRepository,
            @Qualifier("jpaPostRepository") PostRepository postRepository,
            @Qualifier("jpaImageRepository") ImageRepository imageRepository
    ) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
        this.imageRepository = imageRepository;
    }

    @Test
    @Transactional
    void save() {
        Post post = Post.builder()
                .title("조인 테이블 테스트")
                .content("조인 테이블 테스트")
                .thumbnail("123123")
                .category("조인 테이블 테스트 카테고리")
                .createAt(LocalDateTime.now().withNano(0))
                .updateAt(LocalDateTime.now().withNano(0))
                .status(RecordStatus.exist)
                .build();
        postRepository.save(post);

        Image image = Image.builder()
                .storedName("조인 테이블 테스트")
                .originName("조인 테이블 테스트")
                .createAt(LocalDateTime.now().withNano(0))
                .status(RecordStatus.exist)
                .build();
        imageRepository.save(image);

        PostImage postImage = PostImage.builder()
                .post(post)
                .image(image)
                .build();

        postImage = postImageRepository.save(postImage);
        log.info("postImage relation = {}", postImage);
    }
}
