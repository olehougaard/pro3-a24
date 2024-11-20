package dk.via.cars.data;

import dk.via.cars.business.persistence.UserPersistence;
import dk.via.cars.model.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@Scope("singleton")
public class UserDAO implements UserPersistence {
    public static final String USER_BY_USERNAME = "SELECT * FROM \"User\" WHERE username = ?";
    public static final String ROLES_BY_USERNAME = "SELECT * FROM User_Roles WHERE username = ?";
    private final DatabaseHelper<User> helper;
    private final DatabaseHelper<String> roleHelper;

    public UserDAO(DatabaseHelper<User> helper, DatabaseHelper<String> roleHelper) {
        this.helper = helper;
        this.roleHelper = roleHelper;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        String username = rs.getString("username");
        String password = rs.getString("password");
        List<String> roles = roleHelper.map(rs1 -> rs1.getString("role"), ROLES_BY_USERNAME, username);
        return new User(username, password, roles);
    }

    @Override
    public User read(String username) throws SQLException {
        return helper.mapSingle(this::mapUser, USER_BY_USERNAME, username);
    }
}
