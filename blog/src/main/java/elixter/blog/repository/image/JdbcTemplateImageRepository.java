package elixter.blog.repository.image;

import elixter.blog.constants.RecordStatusConstants;
import elixter.blog.domain.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
    public Image save(Image image) {
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

    @Override
    public List<Image> findAll() {
        return jdbcTemplate.query("SELECT * FROM images", imageRowMapper());
    }

    @Override
    public List<Image> findByStatus(String status) {
        return jdbcTemplate.query("SELECT * FROM images WHERE status = ?", imageRowMapper(), status);
    }

    @Override
    public void updateStatusById(Long id, String status) {
        jdbcTemplate.update("UPDATE images SET status = ? WHERE id = ?", status, id);
    }

    @Override
    public void updateStatusByIdBatch(List<Long> idList, String status) {
        jdbcTemplate.batchUpdate("UPDATE images set status = ? WHERE id = ?", getStatementSetter(idList, status));
    }

    @NotNull
    private BatchPreparedStatementSetter getStatementSetter(List<Long> idList, String status) {
        return new BatchPreparedStatementSetter() {
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1, status);
                ps.setLong(2, idList.get(i));
            }

            public int getBatchSize() {
                return idList.size();
            }
        };
    }

    private RowMapper<Image> imageRowMapper() {
        return new RowMapper<Image>() {
            @Override
            public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
                Image image = new Image();
                image.setId(rs.getLong("id"));
                image.setUrl(rs.getString("url"));
                image.setOriginName(rs.getString("origin_name"));
                image.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
                image.setStatus(rs.getString("status"));

                return image;
            }
        };
    }
}
