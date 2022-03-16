package elixter.blog.repository.image;

import elixter.blog.Constants;
import elixter.blog.domain.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
@Qualifier("jdbcTemplateImageRepository")
public class JdbcTemplateImageRepository implements ImageRepository {
    private static final String IMAGE_FILE_FOLDER = "D:/blog_v1/blog/src/main/resources/static/img";

    private final JdbcTemplate jdbcTemplate;
    private final ImageStorage imageStorage;

    @Autowired
    public JdbcTemplateImageRepository(DataSource dataSource, @Qualifier("localImageStorage") ImageStorage imageStorage) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.imageStorage = imageStorage;
    }

    @Override
    public Image save(MultipartFile multipartFile) throws IOException {
        String resultUrl = imageStorage.save(multipartFile);
        log.debug("resultUrl : {}", resultUrl);

        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("images").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("origin_name", multipartFile.getOriginalFilename());
        params.put("url", resultUrl);
        params.put("status", Constants.recordStatusWait);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));

        return new Image(key.longValue(), multipartFile.getOriginalFilename(), resultUrl, "PENDING");
    }

    /***
     *
     * @param name - Origin image name
     * @return Image data as byte array
     *
     * This method is Only used for get local saved image data.
     */
    @Override
    public byte[] get(String name) {
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
