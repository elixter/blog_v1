package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Repository
@Qualifier("s3ImageStorage")
public class S3ImageStorage implements ImageStorage {

    @Override
    public Image save(MultipartFile multipartFile) {
        return null;
    }
}
