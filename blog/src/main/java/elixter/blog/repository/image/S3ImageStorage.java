package elixter.blog.repository.image;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
@Qualifier("s3ImageStorage")
public class S3ImageStorage implements ImageStorage {
    @Override
    public String save(MultipartFile multipartFile) throws IOException {
        return null;
    }

    @Override
    public byte[] getByName(String name) {
        return new byte[0];
    }
}
