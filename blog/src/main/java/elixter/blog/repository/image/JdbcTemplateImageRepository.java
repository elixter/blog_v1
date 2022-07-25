package elixter.blog.repository.image;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.image.Image;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Repository
@Qualifier("jdbcTemplateImageRepository")
public class JdbcTemplateImageRepository implements ImageRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final SimpleJdbcInsert jdbcRelate;

    @Autowired
    public JdbcTemplateImageRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("images")
                .usingGeneratedKeyColumns("id");

        this.jdbcRelate = new SimpleJdbcInsert(dataSource)
                .withTableName("images_posts")
                .usingColumns("image_id", "post_id");
    }

    @Override
    public Image save(Image image) {

        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("origin_name", image.getOriginName())
                .addValue("stored_name", image.getStoredName())
                .addValue("create_at", image.getCreateAt())
                .addValue("status", image.getStatus().toString());
        Number key = jdbcInsert.executeAndReturnKey(params);
        image.setId(key.longValue());

        return image;
    }

    @Override
    @Transactional
    public <S extends Image> Iterable<S> saveAll(Iterable<S> entities) {

        List<Image> imageList = (List<Image>) entities;
        List<SqlParameterSource> batchParams = new ArrayList<>();
        for (Image img : imageList) {
            img.setStatus(RecordStatus.exist);
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("origin_name", img.getOriginName())
                    .addValue("stored_name", img.getStoredName())
                    .addValue("create_at", img.getCreateAt())
                    .addValue("status", img.getStatus().toString());
            batchParams.add(param);
        }
        jdbcInsert.executeBatch(batchParams.toArray(new SqlParameterSource[0]));

        String lastIdSql = "select last_insert_id()";
        Long lastId = jdbcTemplate.queryForObject(lastIdSql, new MapSqlParameterSource(), Long.class);

        int lastIdx = imageList.size() - 1;
        for (int i = lastIdx; i >= 0; i--) {
            imageList.get(i).setId(lastId - lastIdx + i);
        }

        return (Iterable<S>) imageList;
    }

    @Override
    public Optional<Image> findById(Long id) {

        String sql = "select * from images where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        try {
            Image image = jdbcTemplate.queryForObject(sql, param, imageRowMapper());
            return Optional.of(image);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Image> findAll() {

        String sql = "select * from images";

        return jdbcTemplate.query(sql, imageRowMapper());
    }

    @Override
    public List<Image> findByStatus(RecordStatus status) {

        String sql = "select * from images where status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.toString());

        return jdbcTemplate.query(sql, param, imageRowMapper());
    }

    @Override
    public List<Image> findByPostId(Long postId) {

        String sql = "select * from images join images_posts ip on images.id = ip.image_id where ip.post_id = :postId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("postId", postId);

        return jdbcTemplate.query(sql, param, imageRowMapper());
    }

    @Override
    public List<Image> findByStoredName(List<String> urlList) {

//        String in = String.join(",", Collections.nCopies(urlList.size(), "?"));
        String sql = "select * from images where stored_name in (:urlList)";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("urlList", urlList);

        return jdbcTemplate.query(sql, param, imageRowMapper());
    }

    @Override
    public Optional<Image> findByStoredName(String storedName) {

        String sql = "select * from images where stored_name = :storedName";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("storedName", storedName);

        try {
            Image image = jdbcTemplate.queryForObject(sql, param, imageRowMapper());
            return Optional.of(image);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void relateWithPost(List<Long> idList, Long postId) {

        List<Map<String, Object>> records = new LinkedList<>();
        for (Long imageId : idList) {
            Map<String, Object> record = new HashMap<>();
            record.put("image_id", imageId);
            record.put("post_id", postId);
            records.add(record);
        }

        jdbcRelate.executeBatch(SqlParameterSourceUtils.createBatch(records));
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
        return ((rs, rowNum) -> Image.builder()
                .id(rs.getLong("id"))
                .originName(rs.getString("origin_name"))
                .storedName(rs.getString("stored_name"))
                .createAt(rs.getTimestamp("create_at").toLocalDateTime())
                .status(RecordStatus.valueOf(rs.getString("status")))
                .build());
    }
}
