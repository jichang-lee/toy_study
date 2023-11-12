package org.spring.testspring.config;

import org.spring.testspring.domain.User;
import org.spring.testspring.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;


public class MasterMockSecurityContext implements WithSecurityContextFactory<MasterMockUser> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public SecurityContext createSecurityContext(MasterMockUser annotation) {
        User user = User.builder()
                .name(annotation.name())
                .email(annotation.email())
                .password(annotation.password())
                .build();

        userRepository.save(user);

        UserPrincipal principal = new UserPrincipal(user);
        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_ADMIN");
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        principal,
                        user.getPassword(),
                        List.of(role));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authenticationToken);

        return context;
    }
}
