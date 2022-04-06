package elixter.blog.repository.image;

import elixter.blog.constants.RecordStatusConstants;
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
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateImageRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Image save(Image image) throws IOException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("images").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("origin_name", image.getOriginName());
        params.put("url", image.getUrl());
        params.put("create_at", image.getCreateAt());
        params.put("status", image.getStatus());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        image.setId(key.longValue());

        return image;
    }
}
