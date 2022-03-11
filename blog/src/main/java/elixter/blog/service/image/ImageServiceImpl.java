package elixter.blog.service.image;

import elixter.blog.repository.image.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(@Qualifier("jdbcTemplateImageRepository") ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public String save(MultipartFile image) throws IOException {
        return imageRepository.save(image).getUrl();
    }

    @Override
    public byte[] getImageByName(String name) {
        return imageRepository.get(name);
    }
}
