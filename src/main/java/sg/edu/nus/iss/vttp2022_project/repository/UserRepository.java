package sg.edu.nus.iss.vttp2022_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.vttp2022_project.model.ConversionUtils;
import sg.edu.nus.iss.vttp2022_project.model.User;
import static sg.edu.nus.iss.vttp2022_project.repository.SQLStatements.*;

@Repository
public class UserRepository {
    
    @Autowired
    private JdbcTemplate template;

    public Integer checkUserByUsernamePass(String username, String password) {
        final SqlRowSet rs = template.queryForRowSet(
            SQL_COUNT_BY_USERNAME_PASSWORD,
            username,
            password);

        if (!rs.next()) {
            return 0;
        }

        return rs.getInt("user_count");
    }

    public Optional<User> returnUser(String username, String password) {
        final SqlRowSet rs = template.queryForRowSet(
            SQL_SELECT_BY_USERNAME_PASSWORD,
            username,
            password);

        if (!rs.next()) {
            return Optional.empty();
        }
        User user = ConversionUtils.populateFromDb(rs);
        return Optional.of(user);
    }

    public boolean insertUser(User user) {
        Integer count = template.update(
            SQL_INSERT_NEW_USER,
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            user.getPassword());
        
        return count > 0;
    }

    public Optional<User> checkUserByEmail(String email) {
        final SqlRowSet rs = template.queryForRowSet(
            SQL_SELECT_BY_EMAIL,
            email);

        if (!rs.next()) {
            return Optional.empty();
        }
        User user = ConversionUtils.populateFromDb(rs);
        return Optional.of(user);
    }

    public Optional<User> checkUserByUsername(String username) {
        final SqlRowSet rs = template.queryForRowSet(
            SQL_SELECT_BY_USERNAME,
            username);
            
        if (!rs.next()) {
            return Optional.empty();
        }
        User user = ConversionUtils.populateFromDb(rs);
        return Optional.of(user);
    }
}
