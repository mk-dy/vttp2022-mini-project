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

    public Optional<User> returnUser(String email, String password) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_BY_EMAIL_PASSWORD,email,password);
        if (!rs.next()) {
            return Optional.empty();
        }
        User user = ConversionUtils.populateFromDb(rs);

        return Optional.of(user);
    }
}
