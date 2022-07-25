package elixter.blog.repository.post;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.post.Post;
import elixter.blog.exception.post.PostNotFoundException;
import elixter.blog.repository.RepositoryUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Slf4j
@Qualifier("jdbcTemplatePostRepository")
@Repository
public class JdbcTemplatePostRepository implements PostRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcTemplatePostRepository(DataSource datasource) {

        System.out.println("dataSource = " + datasource);
        jdbcTemplate = new NamedParameterJdbcTemplate(datasource);
        jdbcInsert = new SimpleJdbcInsert(datasource)
                .withTableName("posts")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Post save(Post post) {

        post.setStatus(RecordStatus.exist);
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("title", post.getTitle())
                .addValue("content", post.getContent())
                .addValue("category", post.getCategory())
                .addValue("thumbnail", post.getThumbnail())
                .addValue("create_at", post.getCreateAt())
                .addValue("update_at", post.getUpdateAt())
                .addValue("status", post.getStatus().toString());
        Number key = jdbcInsert.executeAndReturnKey(param);
        post.setId(key.longValue());

        return post;
    }

    @Override
    public void update(Post post) {

        String sql =  "update posts set title = :title, content = :content,  category = :category, thumbnail = :thumbnail, update_at = :updateAt where id = :id";
        SqlParameterSource param = new BeanPropertySqlParameterSource(post);

        jdbcTemplate.update(sql, param);
    }

    @Override
    public Optional<Post> findById(Long id) {

        String sql = "select * from posts where id = :id and status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", RecordStatus.exist.toString());

        try {
            Post post = jdbcTemplate.queryForObject(sql, param, postRowMapper());
            return Optional.of(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Post> findByIdAndStatus(Long id, RecordStatus status) {

        String sql = "select * from posts where id = :id and status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", status.toString());

        try {
            Post post = jdbcTemplate.queryForObject(sql, param, postRowMapper());
            return Optional.of(post);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findAll(Pageable pageable) {

        log.debug("get non-cached posts data with pageable [{}]", pageable);
        Long count = getPostCount(RecordStatus.exist);

        String orderBy = RepositoryUtils.getOrderBy(pageable);
        String sql = "select * from posts where status = :status " + orderBy + " limit :offset, :limit";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.toString())
                .addValue("offset", pageable.getOffset())
                .addValue("limit", pageable.getPageSize());
        List<Post> result = jdbcTemplate.query(sql, param, postRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional
    public Page<Post> findAllByStatus(RecordStatus status, Pageable pageable) {

        log.debug("get non-cached posts data with pageable [{}]", pageable);
        Long count = getPostCount(status);

        String orderBy = RepositoryUtils.getOrderBy(pageable);
        String sql = "select * from posts where status = :status " + orderBy + " limit :offset, :limit";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.toString())
                .addValue("offset", pageable.getOffset())
                .addValue("limit", pageable.getPageSize());
        List<Post> result = jdbcTemplate.query(sql, param, postRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByCategory(String category, Pageable pageable) {


        log.debug("get non-cached posts data with category [{}] and pageable [{}]", category, pageable);
        Long count = getPostCountByCategory(category, RecordStatus.exist);

        String orderBy = RepositoryUtils.getOrderBy(pageable);
        log.debug("order clause={}", orderBy);
        String sql = "select * from posts where category = :category and status = :status " + orderBy + " limit :offset, :limit";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("category", category)
                .addValue("status", RecordStatus.exist.toString())
                .addValue("offset", pageable.getOffset())
                .addValue("limit", pageable.getPageSize());
        List<Post> result = jdbcTemplate.query(sql, param, postRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByCategoryAndStatus(String category, RecordStatus status, Pageable pageable) {


        log.debug("get non-cached posts data with category [{}] and pageable [{}]", category, pageable);
        Long count = getPostCountByCategory(category, status);

        String orderBy = RepositoryUtils.getOrderBy(pageable);
        log.debug("order clause={}", orderBy);
        String sql = "select * from posts where category = :category and status = :status " + orderBy + " limit :offset, :limit";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("category", category)
                .addValue("status", status.toString())
                .addValue("offset", pageable.getOffset())
                .addValue("limit", pageable.getPageSize());
        List<Post> result = jdbcTemplate.query(sql, param, postRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public void delete(Long id) {

        String sql = "update posts set status = :status where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.deleted.toString())
                .addValue("id", id);
        int affected = jdbcTemplate.update(sql, param);
        if (affected == 0) {
            throw new PostNotFoundException(id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByHashtag(String hashtag, Pageable pageable) {

        log.debug("get non-cached posts data with hashtag [{}] and pageable [{}]", hashtag, pageable);
        Long count = getPostCountByHashtag(hashtag, RecordStatus.exist);

        String orderBy = RepositoryUtils.getOrderBy(pageable);
        String sql = "select * from posts p join hashtags h on p.id = h.post_id where h.tag = :tag and p.status = :status " + orderBy + " limit :offset, :limit";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("tag", hashtag)
                .addValue("status", RecordStatus.exist.toString())
                .addValue("offset", pageable.getOffset())
                .addValue("limit", pageable.getPageSize());
        List<Post> result = jdbcTemplate.query(sql, param, postRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> findByHashtagAndStatus(String hashtag, RecordStatus status, Pageable pageable) {

        log.debug("get non-cached posts data with hashtag [{}] and pageable [{}]", hashtag, pageable);
        Long count = getPostCountByHashtag(hashtag, RecordStatus.exist);

        String orderBy = RepositoryUtils.getOrderBy(pageable);
        String sql = "select * from posts p join hashtags h on p.id = h.post_id where h.tag = :tag and p.status = :status " + orderBy + " limit :offset, :limit";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("tag", hashtag)
                .addValue("status", status.toString())
                .addValue("offset", pageable.getOffset())
                .addValue("limit", pageable.getPageSize());
        List<Post> result = jdbcTemplate.query(sql, param, postRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    private RowMapper<Post> postRowMapper() {
        return BeanPropertyRowMapper.newInstance(Post.class);
    }

    private Long getPostCount(RecordStatus status) {
        String sql = "select count(*) as cnt from posts where status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.toString());

        return jdbcTemplate.queryForObject(sql, param, Long.class);
    }

    private Long getPostCountByCategory(String category, RecordStatus status) {
        String sql = "select count(*) from posts where category = :category and status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("category", category)
                .addValue("status", status.toString());
        return jdbcTemplate.queryForObject(sql, param, Long.class);
    }

    private Long getPostCountByHashtag(String hashtag, RecordStatus status) {
        String sql = "select count(*) from posts p join hashtags h on p.id = h.post_id where h.tag = :tag and p.status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("tag", hashtag)
                .addValue("status", status.toString());
        return jdbcTemplate.queryForObject(sql, param, Long.class);
    }
}
