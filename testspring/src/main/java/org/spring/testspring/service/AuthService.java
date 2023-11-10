package org.spring.testspring.service;

import lombok.RequiredArgsConstructor;
import org.spring.testspring.domain.User;
import org.spring.testspring.exception.AlreadyExistsEmailException;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.Signup;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(Signup signup) {
        Optional<User> byEmail = userRepository.findByEmail(signup.getEmail());
        if(byEmail.isPresent()){
            throw new AlreadyExistsEmailException();
        }


        String encrypt = passwordEncoder.encode(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .email(signup.getEmail())
                .password(encrypt)
                .build();

        userRepository.save(user);
    }
}
