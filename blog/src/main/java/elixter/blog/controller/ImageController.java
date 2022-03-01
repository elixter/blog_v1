package elixter.blog.controller;

import elixter.blog.service.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/image")
public class ImageController {
    private final ImageService imageService;

    @Autowired

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping
    public String PostImageHandler(MultipartFile imageData) throws IOException {
        byte[] byteData = imageData.getBytes();

        return imageService.save(byteData);
    }
}
