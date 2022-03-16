package elixter.blog.repository.hashtag;

import elixter.blog.Constants;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.dto.hashtag.SearchHashtagDto;
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
import java.util.*;

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
    public List<Hashtag> batchSave(List<Hashtag> hashtags) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("hashtags").usingGeneratedKeyColumns("id");

        List<MapSqlParameterSource> batchParams = new ArrayList<>();

        for (Hashtag hashtag : hashtags) {
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("tag", hashtag.getTag());
            parameters.put("post_id", hashtag.getPostId());
            parameters.put("status", Constants.recordStatusExist);
            batchParams.add(new MapSqlParameterSource(parameters));
        }

        jdbcInsert.executeBatch(batchParams.toArray(new MapSqlParameterSource[0]));

        return null;
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
    public List<SearchHashtagDto> searchTag(String tag, Long offset, Long limit) {
        return jdbcTemplate.query("Select tag, count(*) as tag_count from HASHTAGS where tag like ? group by tag limit ?, ?", searchHashtagsRowMapper(), tag+'%', offset, limit);
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("update hashtags set status = ? where id = ?", hashtagRowMapper(), Constants.recordStatusDeleted, id);
    }

    @Override
    public void deleteByTag(String tag) {
        jdbcTemplate.update("update hashtags set status = ? where tag = ?", hashtagRowMapper(), Constants.recordStatusDeleted, tag);
    }

    @Override
    public void deleteByPostId(Long postId) {
        jdbcTemplate.update("delete from hashtags where post_id = ?", postId);
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

    private RowMapper<SearchHashtagDto> searchHashtagsRowMapper() {
        return new RowMapper<SearchHashtagDto>() {
            @Override
            public SearchHashtagDto mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new SearchHashtagDto(rs.getString("tag"), rs.getLong("tag_count"));
            }
        };
    }
}
