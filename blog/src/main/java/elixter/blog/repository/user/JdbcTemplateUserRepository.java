package elixter.blog.repository.user;

import elixter.blog.Constants;
import elixter.blog.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Repository
public class JdbcTemplateUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired

    public JdbcTemplateUserRepository(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public User save(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("users").usingGeneratedKeyColumns("id");

        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("login_id", user.getLoginId());
        params.put("login_pw", user.getLoginPw());
        params.put("profile_image", user.getProfileImage());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        user.setId(key.longValue());

        return user;
    }

    @Override
    public void update(User user) {
        jdbcTemplate.update(
                "update users set name = ?, login_pw = ?, profile_image = ? where id = ?",
                user.getName(),
                user.getLoginPw(),
                user.getProfileImage(),
                user.getId()
        );
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> result = jdbcTemplate.query(
                "select * from users where status = ? and id = ?",
                userRowMapper(),
                Constants.recordStatusExist,
                id
        );
        return result.stream().findAny();
    }

    @Override
    public List<User> findAll(Long offset, Long limit) {
        List<User> result = jdbcTemplate.query(
                "select * from users where status = ? limit ?, ?",
                userRowMapper(),
                Constants.recordStatusExist,
                offset,
                limit
        );
        return result;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                "update users set status = ? where id = ?",
                Constants.recordStatusDeleted,
                id
        );
    }

    private RowMapper<User> userRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();

                user.setId(rs.getLong("id"));
                user.setLoginId(rs.getString("login_id"));
                user.setLoginPw(rs.getString("login_pw"));
                user.setName(rs.getString("name"));
                user.setProfileImage(rs.getString("profile_image"));

                return user;
            }
        };
    }
}
