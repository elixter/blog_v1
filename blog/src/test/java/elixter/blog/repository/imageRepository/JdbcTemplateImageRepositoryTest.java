package elixter.blog.repository.imageRepository;

import elixter.blog.constants.RecordStatusConstants;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.image.ImageStorage;
import elixter.blog.repository.image.JdbcTemplateImageRepository;
import elixter.blog.repository.image.LocalImageStorage;
import elixter.blog.repository.post.JdbcTemplatePostRepository;
import elixter.blog.repository.post.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class JdbcTemplateImageRepositoryTest {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    private final ImageStorage imageStorage;

    @Autowired
    public JdbcTemplateImageRepositoryTest(ImageRepository imageRepository, PostRepository postRepository, @Qualifier("localImageStorage") ImageStorage imageStorage) {
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
        this.imageStorage = imageStorage;
    }

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    @Transactional
    public void save() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("힘들때 웃는자가 일류다", "png", "src/test/resources/img/힘들때 웃는자가 일류다.png");

        String resultUrl = imageStorage.save(mockMultipartFile);

        Image uploadImage = new Image();
        uploadImage.setUrl(resultUrl);
        uploadImage.setOriginName(mockMultipartFile.getOriginalFilename());
        uploadImage.setCreateAt(LocalDateTime.now());
        uploadImage.setStatus(RecordStatusConstants.recordStatusPending);

        Image savedImage = imageRepository.save(uploadImage);
        System.out.println("savedImage = " + savedImage);

        Assertions.assertThat(savedImage.getUrl()).isEqualTo("http://localhost:8080/api/image/힘들때 웃는자가 일류다.png");
    }

    @Test
    @Transactional
    public void updateStatusByIdBatch() {
        Image img1 = new Image();
        img1.setUrl("http://localhost");
        img1.setOriginName("test");
        img1.setCreateAt(LocalDateTime.now());
        img1.setStatus(RecordStatusConstants.recordStatusPending);

        Image img2 = new Image();
        img2.setUrl("http://localhost2");
        img2.setOriginName("test2");
        img2.setCreateAt(LocalDateTime.now());
        img2.setStatus(RecordStatusConstants.recordStatusPending);

        imageRepository.save(img1);
        imageRepository.save(img2);

        List<Long> idList = new ArrayList<>();
        idList.add(img1.getId());
        idList.add(img2.getId());

        imageRepository.updateStatusByIdBatch(idList, RecordStatusConstants.recordStatusExist);
        img1.setStatus(RecordStatusConstants.recordStatusExist);
        img2.setStatus(RecordStatusConstants.recordStatusExist);


        List<Image> existImages = imageRepository.findByStatus(RecordStatusConstants.recordStatusExist);
        Assertions.assertThat(existImages).contains(img1, img2);
    }

    @Test
    @Transactional
    public void findByPostId() {
        Image img1 = new Image();
        img1.setUrl("http://localhost");
        img1.setOriginName("test");
        img1.setCreateAt(LocalDateTime.now());
        img1.setStatus(RecordStatusConstants.recordStatusPending);

        Image img2 = new Image();
        img2.setUrl("http://localhost2");
        img2.setOriginName("test2");
        img2.setCreateAt(LocalDateTime.now());
        img2.setStatus(RecordStatusConstants.recordStatusPending);

        Post post = new Post();
        post.setTitle("Test title");
        post.setContent("This is test content");
        post.setCategory("test category");
        post.setThumbnail("http://www.testimage.com");

        postRepository.save(post);
        Post savedPost = postRepository.findById(post.getId()).get();

        imageRepository.save(img1);
        imageRepository.save(img2);

        List<Long> idList = new ArrayList<>();
        idList.add(img1.getId());
        idList.add(img2.getId());

        imageRepository.relateWithPost(idList, savedPost.getId());
        List<Image> result = imageRepository.findByPostId(savedPost.getId());

        Assertions.assertThat(result).contains(img1, img2);
    }

    @Test
    @Transactional
    public void findByUrl() {
        Image img = new Image();
        img.setUrl("findByUrlTestUrl123123");
        img.setOriginName("test");
        img.setCreateAt(LocalDateTime.now().withNano(0));
        img.setStatus(RecordStatusConstants.recordStatusPending);
        imageRepository.save(img);

        Image result = imageRepository.findByUrl(img.getUrl()).get();

        Assertions.assertThat(result).isEqualTo(img);
    }

    @Test
    @Transactional
    public void findByUrlBatch() {
        Image img1 = new Image();
        img1.setUrl("findByUrlTestUrl1");
        img1.setOriginName("test");
        img1.setCreateAt(LocalDateTime.now());
        img1.setStatus(RecordStatusConstants.recordStatusPending);

        Image img2 = new Image();
        img2.setUrl("findByUrlTestUrl2");
        img2.setOriginName("test2");
        img2.setCreateAt(LocalDateTime.now());
        img2.setStatus(RecordStatusConstants.recordStatusPending);

        imageRepository.save(img1);
        imageRepository.save(img2);

        List<String> urlList = Arrays.asList(img1.getUrl(), img2.getUrl());

        List<Image> results = imageRepository.findByUrlBatch(urlList);

        Assertions.assertThat(results).contains(img1, img2);
    }
}
