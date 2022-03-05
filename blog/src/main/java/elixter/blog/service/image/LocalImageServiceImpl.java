package elixter.blog.service.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Component
public class LocalImageServiceImpl implements ImageService{
    private static final String SERVER_PREFIX = "http://localhost:8080";
    private static final String IMAGE_FILE_FOLDER = "D:/blog_v1/blog/src/main/resources/static/img";

    @Override
    public String save(MultipartFile imageData) throws IOException, IllegalStateException {
        log.debug("contentType : {}", imageData.getContentType());

        File folder = new File(IMAGE_FILE_FOLDER);
        if (!folder.exists()) folder.mkdirs();

        File destination = new File(IMAGE_FILE_FOLDER + File.separator + imageData.getOriginalFilename());
        imageData.transferTo(destination);

        String resultUrl = SERVER_PREFIX + "/static/img/" + destination.getName();
        log.debug("resultUrl : {}", resultUrl);

        return resultUrl;
    }
}
