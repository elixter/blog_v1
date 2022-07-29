package elixter.blog.repository.image;

import elixter.blog.domain.RecordStatus;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.domain.postImage.PostImage;
import elixter.blog.repository.post.PostRepository;
import elixter.blog.repository.postImage.PostImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class JpaImageRepositoryTest {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final ImageStorage imageStorage;
    private final PostImageRepository postImageRepository;

    @Autowired
    public JpaImageRepositoryTest(
            ImageRepository imageRepository,
            @Qualifier("jpaPostRepository") PostRepository postRepository,
            @Qualifier("localImageStorage") ImageStorage imageStorage,
            PostImageRepository postImageRepository
    ) {
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
        this.imageStorage = imageStorage;
        this.postImageRepository = postImageRepository;
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }


    @Test
    @Transactional
    public void save() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("힘들때 웃는자가 일류다", "png", "src/test/resources/img/힘들때 웃는자가 일류다.png");
        Image saved = imageStorage.save(mockMultipartFile);
        saved.setStatus(RecordStatus.exist);
        saved.setCreateAt(LocalDateTime.now().withNano(0));

        saved = imageRepository.save(saved);
        System.out.println("savedImage = " + saved);

        Assertions.assertThat(saved).isNotEqualTo(Image.getEmpty());
    }

    @Test
    @Transactional
    public void findByPostId() {

        Post post = new Post();
        post.setTitle("Test title");
        post.setContent("This is test content");
        post.setCategory("test category");
        post.setThumbnail("http://www.testimage.com");

        Image img1 = new Image();
        img1.setStoredName("http://localhost");
        img1.setOriginName("test");
        img1.setCreateAt(LocalDateTime.now().withNano(0));
        img1.setStatus(RecordStatus.pending);

        Image img2 = new Image();
        img2.setStoredName("http://localhost2");
        img2.setOriginName("test2");
        img2.setCreateAt(LocalDateTime.now().withNano(0));
        img2.setStatus(RecordStatus.pending);


        postRepository.save(post);

        imageRepository.save(img1);
        imageRepository.save(img2);

        PostImage postImage1 = PostImage.builder()
                .post(post)
                .image(img1)
                .build();

        PostImage postImage2 = PostImage.builder()
                .post(post)
                .image(img2)
                .build();

        postImageRepository.saveAll(Arrays.asList(postImage1, postImage2));

        List<Image> result = imageRepository.findByPostId(post.getId());

        Assertions.assertThat(result).contains(img1, img2);
    }

    @Test
    @Transactional
    public void findByUrl() {
        Image img = new Image();
        img.setStoredName("findByUrlTestUrl123123");
        img.setOriginName("test");
        img.setCreateAt(LocalDateTime.now().withNano(0));
        img.setStatus(RecordStatus.pending);
        imageRepository.save(img);

        Image result = imageRepository.findByStoredName(img.getStoredName()).orElse(Image.getEmpty());
        Assertions.assertThat(result).isNotEqualTo(Image.getEmpty());

        Assertions.assertThat(result).isEqualTo(img);
    }

    @Test
    @Transactional
    public void findByStoredNameList() {
        Image img1 = new Image();
        img1.setStoredName("findByUrlTestUrl1");
        img1.setOriginName("test");
        img1.setCreateAt(LocalDateTime.now().withNano(0));
        img1.setStatus(RecordStatus.pending);

        Image img2 = new Image();
        img2.setStoredName("findByUrlTestUrl2");
        img2.setOriginName("test2");
        img2.setCreateAt(LocalDateTime.now().withNano(0));
        img2.setStatus(RecordStatus.pending);

        imageRepository.save(img1);
        imageRepository.save(img2);

        List<String> storedName = Arrays.asList(img1.getStoredName(), img2.getStoredName());

        List<Image> results = imageRepository.findByStoredName(storedName);

        Assertions.assertThat(results).contains(img1, img2);
    }
}
