package elixter.blog.repository.hashtag;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.hashtag.Hashtag;
import elixter.blog.domain.hashtag.HashtagCount;
import elixter.blog.domain.hashtag.HashtagCountInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Qualifier("jdbcTemplateHashtagRepository")
@Repository
public class JdbcTemplateHashtagRepository implements HashtagRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcTemplateHashtagRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("hashtags")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Hashtag save(Hashtag hashtag) {

        hashtag.setStatus(RecordStatus.exist);
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("tag", hashtag.getTag())
                .addValue("post_id", hashtag.getPost().getId())
                .addValue("status", hashtag.getStatus().ordinal());
        Number key = jdbcInsert.executeAndReturnKey(param);
        hashtag.setId(key.longValue());

        return hashtag;
    }

    @Override
    @Transactional
    public List<Hashtag> saveBatch(List<Hashtag> hashtags) {

        List<SqlParameterSource> batchParams = new ArrayList<>();
        for (Hashtag hashtag : hashtags) {
            hashtag.setStatus(RecordStatus.exist);
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("tag", hashtag.getTag())
                    .addValue("post_id", hashtag.getPost().getId())
                    .addValue("status", hashtag.getStatus().ordinal());
            batchParams.add(param);
        }

        jdbcInsert.executeBatch(batchParams.toArray(new SqlParameterSource[0]));
        String lastIdSql = "select last_insert_id()";
        Long lastId = jdbcTemplate.queryForObject(lastIdSql, new MapSqlParameterSource(), Long.class);

        int lastIdx = hashtags.size() - 1;
        for (int i = lastIdx; i >= 0; i--) {
            hashtags.get(i).setId(lastId - lastIdx + i);
        }

        return hashtags;
    }

    @Override
    public <S extends Hashtag> Iterable<S> saveAll(Iterable<S> entities) {
        List<Hashtag> hashtagList = new ArrayList<>();
        entities.forEach(hashtagList::add);
        return (Iterable<S>) saveBatch(hashtagList);
    }

    @Override
    public Optional<Hashtag> findById(Long id) {

        String sql = "select * from hashtags where id = :id and status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", RecordStatus.exist.ordinal());
        List<Hashtag> result = jdbcTemplate.query(sql, param, hashtagRowMapper());
        return result.stream().findAny();
    }

    @Override
    public List<Hashtag> findByTag(String tag) {

        String sql = "select * from hashtags where tag = :tag and status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("tag", tag)
                .addValue("status", RecordStatus.exist.ordinal());
        return jdbcTemplate.query(sql, param, hashtagRowMapper());
    }

    @Override
    public List<Hashtag> findAll() {

        String sql = "select * from hashtags where status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.ordinal());
        return jdbcTemplate.query(sql, param, hashtagRowMapper());
    }

    @Override
    public List<Hashtag> findByPostId(Long postId) {

        String sql = "select * from hashtags where post_id = :postId and status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("postId", postId)
                .addValue("status", RecordStatus.exist.ordinal());
        return jdbcTemplate.query(sql, param, hashtagRowMapper());
    }

    @Override
    public List<HashtagCountInterface> searchTag(String tag) {

        String sql = "select h.tag as tag, count(*) as count from hashtags h where h.tag like :tag group by h.tag";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("tag", tag + '%');
        return jdbcTemplate.query(sql, param, searchHashtagsRowMapper());
    }

    @Override
    public void deleteById(Long id) {

        String sql = "update hashtags set status = :status where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("status", RecordStatus.deleted.ordinal());
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void deleteByTag(String tag) {

        String sql = "update hashtags set status = :status where tag = :tag";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("tag", tag)
                .addValue("status", RecordStatus.deleted.ordinal());
        jdbcTemplate.update(sql, param);
    }

    @Override
    public void deleteByPostId(Long postId) {

        String sql = "update hashtags set status = :status where post_id = :postId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("post_id", postId)
                .addValue("status", RecordStatus.deleted.ordinal());
        jdbcTemplate.update(sql, param);
    }

    private RowMapper<Hashtag> hashtagRowMapper() {
        return ((rs, rowNum) -> {
            Hashtag hashtag = new Hashtag();
            hashtag.setId(rs.getLong("id"));
            hashtag.setTag(rs.getString("tag"));
            hashtag.getPost().setId(rs.getLong("post_id"));
            return hashtag;
        });
    }

    private RowMapper<HashtagCountInterface> searchHashtagsRowMapper() {
        return ((rs, rowNum) -> new HashtagCount(rs.getString("tag"), rs.getLong("count")));
    }
}
