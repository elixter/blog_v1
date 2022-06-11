package elixter.blog.repository.post;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.post.Post;
import elixter.blog.exception.post.PostNotFoundException;
import elixter.blog.utils.RepositoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Primary
@Repository
public class JdbcTemplatePostRepository implements PostRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplatePostRepository(DataSource datasource) {
        System.out.println("dataSource = " + datasource);
        jdbcTemplate = new JdbcTemplate(datasource);
    }

    @Override
    public Post save(Post post) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("posts").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("title", post.getTitle());
        parameters.put("content", post.getContent());
        parameters.put("category", post.getCategory());
        parameters.put("thumbnail", post.getThumbnail());
        parameters.put("status", post.getStatus().ordinal());
        parameters.put("create_at", post.getCreateAt());
        parameters.put("update_at", post.getUpdateAt());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        post.setId(key.longValue());

        return post;
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update(
                "update posts set title = ?, content = ?,  category = ?, thumbnail = ?, update_at = ? where id = ?",
                post.getTitle(),
                post.getContent(),
                post.getCategory(),
                post.getThumbnail(),
                post.getUpdateAt(),
                post.getId()
        );
    }

    @Override
    public Optional<Post> findById(Long id) {
        List<Post> result = jdbcTemplate.query("select * from posts where id = ? and status = ?", postRowMapper(), id, RecordStatus.exist.ordinal());
        return result.stream().findAny();
    }

    @Override
    public Optional<Post> findByIdAndStatus(Long id, RecordStatus status) {
        List<Post> result = jdbcTemplate.query("select * from posts where id = ? and status = ?", postRowMapper(), id, status.ordinal());
        return result.stream().findAny();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {

        log.debug("get non-cached posts data with pageable [{}]", pageable);
        Long count = jdbcTemplate.queryForObject(
                String.format("SELECT * FROM posts WHERE status = %d", RecordStatus.exist.ordinal()),
                Long.class
        );

        List<Post> result = jdbcTemplate.query("select * from posts where status = ? limit ?, ?", postRowMapper(), RecordStatus.exist.ordinal(), pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional
    public Page<Post> findAllByStatus(RecordStatus status, Pageable pageable) {
        log.debug("get non-cached posts data with pageable [{}]", pageable);
        Long count = jdbcTemplate.queryForObject(
                String.format("SELECT * FROM posts WHERE status = %d", RecordStatus.exist.ordinal()),
                Long.class
        );

        List<Post> result = jdbcTemplate.query("select * from posts where status = ? limit ?, ?", postRowMapper(), status.ordinal(), pageable.getOffset(), pageable.getPageSize());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByCategory(String category, Pageable pageable) {

        String orderBy = RepositoryUtils.getOrderBy(pageable);
        log.debug("order clause={}", orderBy);

        log.debug("get non-cached posts data with category [{}] and pageable [{}]", category, pageable);
        List<Post> result = jdbcTemplate.query(
                "select * from posts where category = ? and status = ? " + orderBy + " limit ?, ?",
                postRowMapper(),
                category,
                RecordStatus.exist.ordinal(),
                pageable.getOffset(),
                pageable.getPageSize()
        );

        Long count = jdbcTemplate.queryForObject(
                String.format(
                        "SELECT COUNT(*) FROM posts WHERE category = '%s' AND status = %d",
                        category,
                        RecordStatus.exist.ordinal()
                ),
                Long.class
        );


        return new PageImpl<Post>(result, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByCategoryAndStatus(String category, RecordStatus status, Pageable pageable) {
        String orderBy = RepositoryUtils.getOrderBy(pageable);
        log.debug("order clause={}", orderBy);

        log.debug("get non-cached posts data with category [{}] and pageable [{}]", category, pageable);
        List<Post> result = jdbcTemplate.query(
                "select * from posts where category = ? and status = ? " + orderBy + " limit ?, ?",
                postRowMapper(),
                category,
                status.ordinal(),
                pageable.getOffset(),
                pageable.getPageSize()
        );

        Long count = jdbcTemplate.queryForObject(
                String.format(
                        "SELECT COUNT(*) FROM posts WHERE category = '%s' AND status = %d",
                        category,
                        RecordStatus.exist.ordinal()
                ),
                Long.class
        );


        return new PageImpl<Post>(result, pageable, count);
    }

    @Override
    public void delete(Long id) {
        int affected = jdbcTemplate.update("update posts set status = ? where id = ?", RecordStatus.deleted.ordinal(), id);
        if (affected == 0) {
            throw new PostNotFoundException(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByHashtag(String hashtag, Pageable pageable) {

        log.debug("get non-cached posts data with hashtag [{}] and pageable [{}]", hashtag, pageable);
        Long count = jdbcTemplate.queryForObject(
                String.format(
                        "SELECT COUNT(*) FROM posts p JOIN hashtags h ON p.id = h.post_id WHERE h.tag = '%s' AND p.status = %d",
                        hashtag,
                        RecordStatus.exist.ordinal()
                ),
                Long.class
        );

        List<Post> result = jdbcTemplate.query(
                "select * from posts p join hashtags h on p.id = h.post_id where h.tag = ? and p.status = ? limit ?, ?",
                postRowMapper(),
                hashtag,
                RecordStatus.exist.ordinal(),
                pageable.getOffset(),
                pageable.getPageSize()
        );

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByHashtagAndStatus(String hashtag, RecordStatus status, Pageable pageable) {

        log.debug("get non-cached posts data with hashtag [{}] and pageable [{}]", hashtag, pageable);
        Long count = jdbcTemplate.queryForObject(
                String.format(
                        "SELECT COUNT(*) FROM posts p JOIN hashtags h ON p.id = h.post_id WHERE h.tag = '%s' AND p.status = %d",
                        hashtag,
                        RecordStatus.exist.ordinal()
                ),
                Long.class
        );

        List<Post> result = jdbcTemplate.query(
                "select * from posts p join hashtags h on p.id = h.post_id where h.tag = ? and p.status = ? group by h.post_id limit ?, ?",
                postRowMapper(),
                hashtag,
                status.ordinal(),
                pageable.getOffset(),
                pageable.getPageSize()
        );

        return new PageImpl<>(result, pageable, count);
    }

    private RowMapper<Post> postRowMapper() {
        return new RowMapper<Post>() {
            @Override
            public Post mapRow(ResultSet rs, int rowNum) throws SQLException {
                Post post = new Post();
                post.setId(rs.getLong("id"));
                post.setTitle(rs.getString("title"));
                post.setContent(rs.getString("content"));
                post.setCategory(rs.getString("category"));
                post.setThumbnail(rs.getString("thumbnail"));
                post.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
                post.setUpdateAt(rs.getTimestamp("update_at").toLocalDateTime());
                post.setStatus(RecordStatus.values()[rs.getInt("status")]);

                return post;
            }
        };
    }
}
