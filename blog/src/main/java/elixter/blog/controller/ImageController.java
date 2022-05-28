package elixter.blog.controller;

import elixter.blog.domain.image.Image;
import elixter.blog.dto.image.ImageUploadResponseDto;
import elixter.blog.exception.RestException;
import elixter.blog.service.image.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;

@Slf4j
@RestController
@RequestMapping(value = "/api/image")
public class ImageController {

    @Value("${server.uri}")
    private String serverUri;

    @Value("${file.image}")
    private String imageDir;

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<ImageUploadResponseDto> PostUploadImageHandler(@RequestPart MultipartFile image) {
        if (!image.getContentType().startsWith("image")) {
            throw new RestException(
                    HttpStatus.BAD_REQUEST,
                    "File type " + image.getContentType() + " is not supported"
            );
        }

        Image savedImage = imageService.save(image);
        if (savedImage.isEmpty()) {
            throw new RestException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Image upload failed."
            );
        }
        String resultUrl = getImageUrl(savedImage);

        return ResponseEntity.ok(new ImageUploadResponseDto(resultUrl, savedImage.getOriginName()));
    }

    @GetMapping(value = "/{imageName}", produces = "image/*")
    public Resource GetImageHandler(@PathVariable String imageName) throws MalformedURLException {
        Image image = imageService.getImageByStoredName(imageName);
        if (image.isEmpty()) {
            throw new RestException(HttpStatus.NOT_FOUND, "image not found");
        }

        return new UrlResource("file:" + getFullPath(image));
    }

    @NotNull
    private String getFullPath(Image image) {
        return imageDir + image.getStoredName();
    }

    private String getImageUrl(Image savedImage) {
        return serverUri + "/api/image/" + savedImage.getStoredName();
    }
}
