package dk.via.cars.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public final class User {
    private final String username;
    private final String password;
    private final List<String> authorities;

    public User(String username, String password, List<String> authorities) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public UserDetails toUserDetails() {
        List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream().map(SimpleGrantedAuthority::new).toList();
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return grantedAuthorities;
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public String getUsername() {
                return username;
            }
        };
    }
}
