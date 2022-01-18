package elixter.blog.repository.post;

import elixter.blog.domain.Post;
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

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters));
        post.setId(key.longValue());

        return post;
    }

    @Override
    public void update(Post post) {
        jdbcTemplate.update("update posts set title = ?, content = ?,  category = ?, thumbnail = ? where id = ?", post.getTitle(), post.getContent(), post.getCategory(), post.getThumbnail(), post.getId());
    }

    @Override
    public Optional<Post> findById(Long id) {
        List<Post> result = jdbcTemplate.query("select * from posts where id = ?", postRowMapper(), id);
        return result.stream().findAny();
    }

    @Override
    public List<Post> findAll() {
        return null;
    }

    @Override
    public List<Post> findByCategory(String category) {
        List<Post> result = jdbcTemplate.query("select * from posts where category = ?", postRowMapper(), category);
        return result;
    }

    @Override
    public void delete(Long id) {

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

                return post;
            }
        };
    }
}
