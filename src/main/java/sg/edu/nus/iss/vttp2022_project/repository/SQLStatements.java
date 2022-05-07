package sg.edu.nus.iss.vttp2022_project.repository;

public interface SQLStatements {
    
    public static final String SQL_SELECT_BY_USERNAME_PASSWORD = 
    "select * from user where username = ? and password = sha1(?)";

    public static final String SQL_SELECT_BY_EMAIL = 
    "select * from user where email = ?";

    public static final String SQL_SELECT_BY_USERNAME = 
    "select * from user where username = ?";

    public static final String SQL_COUNT_BY_USERNAME_PASSWORD = 
    "select count(*) as user_count from user where username = ? and password = sha1(?)";

    public static final String SQL_INSERT_NEW_USER =
    "insert into user (username, email, name, password) values (?,?,?,sha1(?))";
}
