package elixter.blog.service;

import elixter.blog.domain.image.Image;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.post.PostRepository;
import elixter.blog.service.image.ImageService;
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

@SpringBootTest
public class ImageServiceImplTest {

    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final PostRepository postRepository;

    @Autowired
    public ImageServiceImplTest(ImageService imageService, ImageRepository imageRepository, @Qualifier("jpaPostRepository") PostRepository postRepository) {
        this.imageService = imageService;
        this.imageRepository = imageRepository;
        this.postRepository = postRepository;
    }

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
}
