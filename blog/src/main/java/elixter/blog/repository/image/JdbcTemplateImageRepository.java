package elixter.blog.repository.image;

import elixter.blog.domain.image.Image;
import elixter.blog.domain.imagePostRelation.ImagePostRelation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

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
    public List<Image> findByPostId(Long postId) {
        return jdbcTemplate.query("SELECT * FROM images join images_posts ip on images.id = ip.image_id WHERE ip.post_id = ?", imageRowMapper(), postId);
    }

    @Override
    public Optional<Image> findByUrl(String url) {
        return jdbcTemplate.query("SELECT * FROM images WHERE url = ?", imageRowMapper(), url).stream().findAny();
    }

    @Override
    public List<Image> findByUrlBatch(List<String> urlList) {
        String in = String.join(",", Collections.nCopies(urlList.size(), "?"));
        String query = String.format("SELECT * FROM images WHERE url in (%s)", in);

        return jdbcTemplate.query(query, imageRowMapper(), urlList.toArray());
    }

    @Override
    public void updateStatusById(Long id, String status) {
        jdbcTemplate.update("UPDATE images SET status = ? WHERE id = ?", status, id);
    }

    @Override
    public void updateStatusByIdBatch(List<Long> idList, String status) {
        jdbcTemplate.batchUpdate("UPDATE images set status = ? WHERE id = ?", getStatementSetter(idList, status));
    }

    @Override
    public void relateWithPost(List<Long> idList, Long postId) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("images_posts")
                .usingColumns("image_id", "post_id");

        List<Map<String, Object>> records = new LinkedList<>();
        for (Long imageId : idList) {
            Map<String, Object> record = new HashMap<>();
            record.put("image_id", imageId);
            record.put("post_id", postId);
            records.add(record);
        }

        jdbcInsert.executeBatch(SqlParameterSourceUtils.createBatch(records));
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
