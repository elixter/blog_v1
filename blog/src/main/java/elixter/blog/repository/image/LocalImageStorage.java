package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Slf4j
@Repository
@Qualifier("localImageStorage")
public class LocalImageStorage implements ImageStorage {

    @Value("${file.image}")
    private String imageDir;

    @Override
    public Image save(MultipartFile multipartFile) {
        log.debug("contentType : {}", multipartFile.getContentType());

        File folder = new File(imageDir);
        if (!folder.exists()) folder.mkdirs();

        String storedName = createStoreFileName(multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(new File(getFullPath(storedName)));
        } catch (IOException e) {
            return Image.getEmpty();
        }

        return Image.builder()
                .originName(multipartFile.getOriginalFilename())
                .storedName(storedName)
                .build();
    }

    private String getFullPath(String filePath) {
        return imageDir + filePath;
    }

    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    private String extractExt(String originFilename) {
        int pos = originFilename.lastIndexOf(".");

        return originFilename.substring(pos + 1);
    }
}
