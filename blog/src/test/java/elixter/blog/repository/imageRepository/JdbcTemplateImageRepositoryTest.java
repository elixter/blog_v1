package elixter.blog.repository.imageRepository;

import elixter.blog.domain.image.Image;
import elixter.blog.repository.image.JdbcTemplateImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
public class JdbcTemplateImageRepositoryTest {
    @Autowired
    JdbcTemplateImageRepository imageRepository;

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    @Transactional
    public void save() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("힘들때 웃는자가 일류다", "png", "src/test/resources/img/힘들때 웃는자가 일류다.png");

        Image savedImage = imageRepository.save(mockMultipartFile);
        System.out.println("savedImage = " + savedImage);

        Assertions.assertThat(savedImage.getUrl()).isEqualTo("http://localhost:8080/api/image/힘들때 웃는자가 일류다.png");
    }
}
