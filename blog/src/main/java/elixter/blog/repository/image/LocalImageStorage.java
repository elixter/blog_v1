package elixter.blog.repository.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@Repository
@Qualifier("localImageStorage")
public class LocalImageStorage implements ImageStorage {
    private static final String SERVER_PREFIX = "http://localhost:8080";
    private static final String IMAGE_FILE_FOLDER = "D:/blog_v1/blog/src/main/resources/static/img";

    @Override
    public String save(MultipartFile multipartFile) throws IOException {
        log.debug("contentType : {}", multipartFile.getContentType());

        File folder = new File(IMAGE_FILE_FOLDER);
        if (!folder.exists()) folder.mkdirs();

        File destination = new File(IMAGE_FILE_FOLDER + File.separator + multipartFile.getOriginalFilename());
        multipartFile.transferTo(destination);

        String resultUrl = SERVER_PREFIX + "/api/image/" + destination.getName();
        log.debug("resultUrl : {}", resultUrl);

        return resultUrl;
    }

    @Override
    public byte[] getByName(String name) {
        FileInputStream fis = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        String fileDir = IMAGE_FILE_FOLDER + "/" + name;

        try {
            fis = new FileInputStream(fileDir);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }

        int readCount = 0;
        byte[] buf = new byte[1024];
        byte[] fileArray = null;

        try {
            while((readCount = fis.read(buf)) != -1) {
                outputStream.write(buf, 0, readCount);
            }
            fileArray = outputStream.toByteArray();
            fis.close();
        } catch (IOException e) {
            throw new RuntimeException("File Error");
        }

        return fileArray;
    }
}
