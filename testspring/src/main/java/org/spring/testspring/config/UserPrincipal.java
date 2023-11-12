package org.spring.testspring.config;

import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserPrincipal extends User {

    private final Long userId;

    public UserPrincipal(org.spring.testspring.domain.User user){
        super(user.getEmail(),user.getPassword(), List.of(
//                new SimpleGrantedAuthority("ROLE_USER")
                new SimpleGrantedAuthority("ROLE_ADMIN")

        ));
        this.userId = user.getId();
    }

}
