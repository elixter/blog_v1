package elixter.blog.controller;

import elixter.blog.dto.image.ImageUploadResponseDto;
import elixter.blog.exception.RestException;
import elixter.blog.service.image.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.handler.ResponseStatusExceptionHandler;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping(value = "/api/image")
public class ImageController {
    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public ImageUploadResponseDto PostUploadImageHandler(@RequestPart MultipartFile image) {
        if (!image.getContentType().startsWith("image")) {
            throw new RestException(
                    HttpStatus.BAD_REQUEST,
                    "File type " + image.getContentType() + " is not supported"
            );
        }

        String url;
        try {
            url = imageService.save(image);
        } catch (IOException e){
            throw new RestException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    "Image upload failed : " + e.getMessage()
            );
        }

        return new ImageUploadResponseDto(url);
    }

    @GetMapping(value = "/{imageName}", produces = "image/*")
    public byte[] GetImageHandler(@PathVariable String imageName) {
        return imageService.getImageByName(imageName);
    }
}
