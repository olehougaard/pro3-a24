package dk.via.cars.security;

import dk.via.cars.business.persistence.UserPersistence;
import dk.via.cars.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserPersistence userPersistence;

    public UserDetailsServiceImpl(UserPersistence userPersistence) {
        this.userPersistence = userPersistence;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userPersistence.read(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }
            return user.toUserDetails();
        } catch (SQLException e) {
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }
}
