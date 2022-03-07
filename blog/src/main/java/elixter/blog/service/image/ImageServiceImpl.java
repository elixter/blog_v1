package elixter.blog.service.image;

import elixter.blog.repository.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class ImageServiceImpl implements ImageService{
    private final ImageRepository imageRepository;

    @Override
    public String save(MultipartFile image) throws IOException {
        return imageRepository.save(image).getUrl();
    }

    @Override
    public byte[] getImageByName(String name) {
        return imageRepository.get(name);
    }
}
