package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageRepository {
    Image save(Image image);

    List<Image> findAll();
    List<Image> findByStatus(String status);

    void updateStatusById(Long id, String status);
    void updateStatusByIdBatch(List<Long> idList, String status);
}
