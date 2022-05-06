package sg.edu.nus.iss.vttp2022_project.model;

import org.springframework.jdbc.support.rowset.SqlRowSet;

public class ConversionUtils {

    public static User populateFromDb(SqlRowSet rs) {

        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setName(rs.getString("name"));
        
        return user;
    }
}
