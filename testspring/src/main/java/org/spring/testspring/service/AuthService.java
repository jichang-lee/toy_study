package org.spring.testspring.service;

import lombok.RequiredArgsConstructor;
import org.spring.testspring.crypto.PasswordEncoder;
import org.spring.testspring.domain.Session;
import org.spring.testspring.domain.User;
import org.spring.testspring.exception.AlreadyExistsEmailException;
import org.spring.testspring.exception.InvalidRequest;
import org.spring.testspring.exception.InvalidSigningInformation;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.Login;
import org.spring.testspring.requset.Signup;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Long signIn(Login login){

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(()-> new InvalidSigningInformation());

        user.addSession();

        boolean matches = passwordEncoder.matches(login.getPassword(), user.getPassword());
        if(!matches){
            throw new InvalidSigningInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<User> byEmail = userRepository.findByEmail(signup.getEmail());
        if(byEmail.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        String encodePassword = passwordEncoder.encrypt(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .email(signup.getEmail())
                .password(encodePassword)
                .build();

        userRepository.save(user);
    }
}
