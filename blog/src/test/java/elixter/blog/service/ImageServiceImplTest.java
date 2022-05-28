package elixter.blog.service;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.image.Image;
import elixter.blog.domain.post.Post;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.post.PostRepository;
import elixter.blog.service.image.ImageService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
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
    @Transactional
    public void save() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("힘들때 웃는자가 일류다", "png", "src/test/resources/img/힘들때 웃는자가 일류다.png");

        Image image = imageService.save(mockMultipartFile);

        Assertions.assertThat(image).isNotEqualTo(Image.getEmpty());
    }

    @Test
    @Transactional
    public void relateWithPost() {
        Image image = Image.builder()
                .originName("test")
                .storedName("http://test.com")
                .createAt(LocalDateTime.now())
                .status(RecordStatus.exist).build();

        imageRepository.save(image);

        Post post = Post.builder()
                .title("test")
                .category("test")
                .content("test")
                .thumbnail("test")
                .status(RecordStatus.exist)
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();

        postRepository.save(post);

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> imageRepository.relateWithPost(Arrays.asList(image.getId()), post.getId()));

        org.junit.jupiter.api.Assertions.assertThrows(DataIntegrityViolationException.class, () -> imageRepository.relateWithPost(Arrays.asList(99999999L), 999999999L));
    }
}
