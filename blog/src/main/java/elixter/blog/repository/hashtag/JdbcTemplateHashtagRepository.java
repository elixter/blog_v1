package elixter.blog.repository.hashtag;

import elixter.blog.Constants;
import elixter.blog.domain.Hashtag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Primary
@Repository
public class JdbcTemplateHashtagRepository implements HashtagRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateHashtagRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Hashtag save(Hashtag hashtag) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("hashtags").usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("tag", hashtag.getTag());
        parameters.put("post_id", hashtag.getPostId());
        parameters.put("status", Constants.recordStatusExist);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        hashtag.setId(key.longValue());

        return hashtag;
    }

    @Override
    public Optional<Hashtag> findById(Long id) {
        List<Hashtag> result = jdbcTemplate.query("select * from hashtags where id = ? and status = ?", hashtagRowMapper(), id, Constants.recordStatusExist);
        return result.stream().findAny();
    }

    @Override
    public List<Hashtag> findByTag(String tag) {
        return jdbcTemplate.query("select * from hashtags where tag = ? and status = ?", hashtagRowMapper(), tag, Constants.recordStatusExist);
    }

    @Override
    public List<Hashtag> findAll() {
        return jdbcTemplate.query("select * from hashtags where status = ?", hashtagRowMapper(), Constants.recordStatusExist);
    }

    @Override
    public List<Hashtag> findByPostId(Long postId) {
        return jdbcTemplate.query("select * from hashtags where post_id = ? and status = ?", hashtagRowMapper(), postId, Constants.recordStatusExist);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("update hashtags set status = ? where id = ?", hashtagRowMapper(), Constants.recordStatusDeleted, id);
    }

    @Override
    public void deleteByTag(String tag) {
        jdbcTemplate.update("update hashtags set status = ? where tag = ?", hashtagRowMapper(), Constants.recordStatusDeleted, tag);
    }

    private RowMapper<Hashtag> hashtagRowMapper() {
        return new RowMapper<Hashtag>() {
            @Override
            public Hashtag mapRow(ResultSet rs, int rowNum) throws SQLException {
                Hashtag hashtag = new Hashtag();
                hashtag.setId(rs.getLong("id"));
                hashtag.setTag(rs.getString("tag"));
                hashtag.setPostId(rs.getLong("post_id"));

                return hashtag;
            }
        };
    }
}
