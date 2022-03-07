package elixter.blog.repository.imageRepository;

import elixter.blog.domain.Image;
import elixter.blog.repository.image.JdbcTemplateLocalImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LocalImageRepositoryTest {
    JdbcTemplateLocalImageRepository imageRepository = new JdbcTemplateLocalImageRepository(DataSource);

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    public void save() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("힘들때 웃는자가 일류다", "png", "src/test/resources/img/힘들때 웃는자가 일류다.png");

        Image savedImage = imageRepository.save(mockMultipartFile);

        Assertions.assertThat(savedImage.getUrl()).isEqualTo("http://localhost:8080/static/img/힘들때 웃는자가 일류다.png");
    }
}
