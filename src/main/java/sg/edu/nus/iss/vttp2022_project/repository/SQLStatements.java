package sg.edu.nus.iss.vttp2022_project.repository;

public interface SQLStatements {
    
    public static final String SQL_SELECT_BY_EMAIL_PASSWORD = 
    "select * from user where email = ? and password = sha1(?)";
}
