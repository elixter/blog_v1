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
    public Image save(MultipartFile image) {
        Image result = imageStorage.save(image);
        if (result.isEmpty()) {
            log.info("Saving image [{}] is failed", image.getOriginalFilename());
            return result;
        }

        result.setCreateAt(LocalDateTime.now());
        result.setStatus(RecordStatusConstants.recordStatusPending);

        return imageRepository.save(result);
    }

    @Override
    public void relateWithPost(List<Long> imageIdList, Long postId) {
        imageRepository.relateWithPost(imageIdList, postId);
    }

    @Override
    public Image getImageByStoredName(String name) {
        return imageRepository.findByStoredName(name).orElse(Image.getEmpty());
    }
}
