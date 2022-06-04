package elixter.blog.repository.user;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        params.put("email", user.getEmail());
        params.put("profile_image", user.getProfileImage());
        params.put("create_at", user.getCreateAt());
        params.put("status", RecordStatus.exist.ordinal());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        user.setId(key.longValue());

        return user;
    }

    @Override
    public User update(User user) {
        User result;
        int affectedCols = 0;
        try {
            affectedCols = jdbcTemplate.update(
                    "update users set name = ?, login_pw = ?, email = ?, profile_image = ? where id = ?",
                    user.getName(),
                    user.getLoginPw(),
                    user.getEmail(),
                    user.getProfileImage(),
                    user.getId()
            );
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }

        if (affectedCols == 0) {
            result = User.getEmpty();
        } else {
            result = user;
        }

        return result;
    }

    @Override
    public Optional<User> findById(Long id) {
        List<User> result = jdbcTemplate.query(
                "select * from users where status = ? and id = ?",
                userRowMapper(),
                RecordStatus.exist.ordinal(),
                id
        );

        return result.stream().findAny();
    }

    @Override
    public Optional<User> findByIdAndStatus(Long id, RecordStatus status) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {
        List<User> result = jdbcTemplate.query(
          "select * from users where status = ? and login_id = ?",
          userRowMapper(),
          RecordStatus.exist.ordinal(),
          loginId
        );

        return result.stream().findAny();
    }

    @Override
    public Optional<User> findByLoginIdAndStatus(String loginId, RecordStatus status) {
        return Optional.empty();
    }

    @Override
    public List<User> findByName(String name) {
        List<User> result = jdbcTemplate.query(
                "select * from users where status = ? and name = ?",
                userRowMapper(),
                RecordStatus.exist.ordinal(),
                name
        );

        return result;
    }

    @Override
    public List<User> findByNameAndStatus(String name, RecordStatus status) {
        return null;
    }

    @Override
    public Page<User> findByName(String name, Pageable pageable) {
        return null;
    }

    @Override
    public Page<User> findByNameAndStatus(String name, RecordStatus status, Pageable pageable) {
        return null;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> result = jdbcTemplate.query(
                "select * from users where status = ? and email = ?",
                userRowMapper(),
                RecordStatus.exist.ordinal(),
                email
        );
        return result.stream().findAny();
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, RecordStatus status) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll(Long offset, Long limit) {
        List<User> result = jdbcTemplate.query(
                "select * from users where status = ? limit ?, ?",
                userRowMapper(),
                RecordStatus.exist.ordinal(),
                offset,
                limit
        );
        return result;
    }

    @Override
    public Page<User> findAll(Pageable page) {
        return null;
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(
                "update users set status = ? where id = ?",
                RecordStatus.deleted.ordinal(),
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
                user.setEmail(rs.getString("email"));
                user.setProfileImage(rs.getString("profile_image"));
                user.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());

                return user;
            }
        };
    }
}
