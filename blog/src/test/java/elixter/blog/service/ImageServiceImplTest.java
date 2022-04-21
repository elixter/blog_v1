package elixter.blog.service;

import elixter.blog.constants.RecordStatusConstants;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.post.PostRepository;
import elixter.blog.service.image.ImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootTest
public class ImageServiceImplTest {

    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    PostRepository postRepository;

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    public void save() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("힘들때 웃는자가 일류다", "png", "src/test/resources/img/힘들때 웃는자가 일류다.png");

        String resultUrl = imageService.save(mockMultipartFile);

        Assertions.assertThat(resultUrl).isEqualTo("http://localhost:8080/api/image/힘들때 웃는자가 일류다.png");
    }

    @Test
    @Transactional
    public void relateWithPost() {
        Image image = Image.builder()
                .originName("test")
                .url("http://test.com")
                .createAt(LocalDateTime.now())
                .status(RecordStatusConstants.recordStatusExist).build();

        imageRepository.save(image);

        Post post = Post.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .status(RecordStatusConstants.recordStatusExist)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        postRepository.save(post);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> imageRepository.relateWithPost(Arrays.asList(image.getId()), post.getId()));
    }
}
