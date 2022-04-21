package elixter.blog.service.image;

import elixter.blog.constants.RecordStatusConstants;
import elixter.blog.domain.image.Image;
import elixter.blog.repository.image.ImageRepository;
import elixter.blog.repository.image.ImageStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;
    private final ImageStorage imageStorage;

    @Autowired
    public ImageServiceImpl(
            @Qualifier("jdbcTemplateImageRepository") ImageRepository imageRepository,
            @Qualifier("localImageStorage") ImageStorage imageStorage
    ) {
        this.imageRepository = imageRepository;
        this.imageStorage = imageStorage;
    }

    @Override
    public String save(MultipartFile image) throws IOException {
        String resultUrl = imageStorage.save(image);
        log.debug("resultUrl : {}", resultUrl);

        Image uploadImage = new Image();
        uploadImage.setUrl(resultUrl);
        uploadImage.setOriginName(image.getOriginalFilename());
        uploadImage.setCreateAt(LocalDateTime.now());
        uploadImage.setStatus(RecordStatusConstants.recordStatusPending);

        return imageRepository.save(uploadImage).getUrl();
    }

    @Override
    public void relateWithPost(List<Long> imageIdList, Long postId) {
        imageRepository.relateWithPost(imageIdList, postId);
    }

    /***
     *
     * @param name - Origin image name
     * @return Image data as byte array
     *
     * This method is Only used for get local saved image data.
     */
    @Override
    public byte[] getImageByName(String name) {
        return imageStorage.getByName(name);
    }
}
