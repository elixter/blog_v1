package elixter.blog.controller;

import elixter.blog.dto.image.ImageUploadResponseDto;
import elixter.blog.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/image")
public class ImageController {
    private final ImageService imageService;

    @Autowired

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    // TODO: 에러처리 다시해야됨
    @PostMapping
    public ImageUploadResponseDto PostUploadImageHandler(@RequestPart MultipartFile image) throws IOException {
        if (!image.getContentType().startsWith("image")) {
            throw new HttpClientErrorException(
                    HttpStatus.BAD_REQUEST,
                    "File type " + image.getContentType() + " is not supported"
            );
        }

        String url = imageService.save(image);

        return new ImageUploadResponseDto(url);
    }

    @GetMapping(value = "/{imageName}", produces = "image/*")
    public byte[] GetImageHandler(@PathVariable String imageName) {
        return imageService.getImageByName(imageName);
    }
}
