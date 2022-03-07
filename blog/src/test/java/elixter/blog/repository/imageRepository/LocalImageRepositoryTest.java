package elixter.blog.repository.imageRepository;

import elixter.blog.repository.image.LocalImageRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class LocalImageRepositoryTest {
    LocalImageRepository imageRepository = new LocalImageRepository();

    private MockMultipartFile getMockMultipartFile(String fileName, String contentType, String path) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File(path));
        return new MockMultipartFile(fileName, fileName + "." + contentType, contentType, fileInputStream);
    }

    @Test
    public void save() throws IOException {
        MockMultipartFile mockMultipartFile = getMockMultipartFile("힘들때 웃는자가 일류다", "png", "src/test/resources/img/힘들때 웃는자가 일류다.png");

        String resultUrl = imageRepository.save(mockMultipartFile);

        Assertions.assertThat(resultUrl).isEqualTo("http://localhost:8080/static/img/힘들때 웃는자가 일류다.png");
    }
}
