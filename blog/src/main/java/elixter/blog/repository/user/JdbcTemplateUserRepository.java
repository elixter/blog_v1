package elixter.blog.repository.user;

import elixter.blog.constants.RecordStatus;
import elixter.blog.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
@Qualifier("jdbcTemplateUserRepository")
@Repository
public class JdbcTemplateUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcTemplateUserRepository(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(User user) {

        user.setStatus(RecordStatus.exist);
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("login_id", user.getLoginId())
                .addValue("login_pw", user.getLoginPw())
                .addValue("email", user.getEmail())
                .addValue("profile_image", user.getProfileImage())
                .addValue("create_at", user.getCreateAt())
                .addValue("status", user.getStatus().ordinal());
        Number key = jdbcInsert.executeAndReturnKey(param);
        user.setId(key.longValue());

        return user;
    }

    @Override
    public void update(User user) {

        String sql = "update users set name = :name, login_pw = :loginPw, email = :email, profile_image = :profileImage where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", user.getName())
                .addValue("loginPw", user.getLoginPw())
                .addValue("email", user.getEmail())
                .addValue("profileImage", user.getProfileImage())
                .addValue("id", user.getId());
        try {
            jdbcTemplate.update(sql, param);
        } catch (DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public Optional<User> findById(Long id) {

        String sql = "select * from users where status = :status and id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.ordinal())
                .addValue("id", id);

        try {
            User user = jdbcTemplate.queryForObject(sql, param, userRowMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByIdAndStatus(Long id, RecordStatus status) {

        String sql = "select * from users where status = :status and id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.ordinal())
                .addValue("id", id);

        try {
            User user = jdbcTemplate.queryForObject(sql, param, userRowMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {

        String sql = "select * from users where status = :status and login_id = :loginId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.ordinal())
                .addValue("loginId", loginId);

        try {
            User user = jdbcTemplate.queryForObject(sql, param, userRowMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLoginIdAndStatus(String loginId, RecordStatus status) {

        String sql = "select * from users where status = :status and login_id = :loginId";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.ordinal())
                .addValue("loginId", loginId);

        try {
            User user = jdbcTemplate.queryForObject(sql, param, userRowMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findByName(String name) {

        String sql = "select * from users where status = :status and name = :name";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.ordinal())
                .addValue("name", name);
        List<User> result = jdbcTemplate.query(sql, param, userRowMapper());

        return result;
    }

    @Override
    public List<User> findByNameAndStatus(String name, RecordStatus status) {

        String sql = "select * from users where status = :status and name = :name";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.ordinal())
                .addValue("name", name);
        List<User> result = jdbcTemplate.query(sql, param, userRowMapper());

        return result;
    }

    @Override
    public Page<User> findByName(String name, Pageable pageable) {

        Long count = getUserCountByName(name, RecordStatus.exist);

        String sql = "select * from users where status = :status and name = :name";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.ordinal())
                .addValue("name", name);
        List<User> result = jdbcTemplate.query(sql, param, userRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Page<User> findByNameAndStatus(String name, RecordStatus status, Pageable pageable) {

        Long count = getUserCountByName(name, status);

        String sql = "select * from users where status = :status and name = :name";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.ordinal())
                .addValue("name", name);
        List<User> result = jdbcTemplate.query(sql, param, userRowMapper());

        return new PageImpl<>(result, pageable, count);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        String sql = "select * from users where status = :status and email = :email";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.ordinal())
                .addValue("email", email);
        try {
            User user = jdbcTemplate.queryForObject(sql, param, userRowMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByEmailAndStatus(String email, RecordStatus status) {

        String sql = "select * from users where status = :status and email = :email";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", status.ordinal())
                .addValue("email", email);
        try {
            User user = jdbcTemplate.queryForObject(sql, param, userRowMapper());
            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll(Long offset, Long limit) {

        String sql = "select * from users where status = :status limit :offset, :limit";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.exist.ordinal())
                .addValue("offset", offset)
                .addValue("limit", limit);
        List<User> result = jdbcTemplate.query(sql, param, userRowMapper());

        return result;
    }

    @Override
    public void delete(Long id) {

        String sql = "update users set status = :status where id = :id";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("status", RecordStatus.deleted.ordinal())
                .addValue("id", id);
        jdbcTemplate.update(sql, param);
    }

    private RowMapper<User> userRowMapper() {
        return ((rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setLoginId(rs.getString("login_id"));
            user.setLoginPw(rs.getString("login_pw"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setProfileImage(rs.getString("profile_image"));
            user.setCreateAt(rs.getTimestamp("create_at").toLocalDateTime());
            user.setStatus(RecordStatus.values()[rs.getInt("status")]);
            return user;
        });
    }

    private Long getUserCountByName(String name, RecordStatus status) {
        String sql = "select count(*) from users where name = :name and status = :status";
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("status", status.ordinal());

        return jdbcTemplate.queryForObject(sql, param, Long.class);
    }
}
