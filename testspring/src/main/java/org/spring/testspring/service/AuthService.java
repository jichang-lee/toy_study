package org.spring.testspring.service;

import lombok.RequiredArgsConstructor;
import org.spring.testspring.domain.Session;
import org.spring.testspring.domain.User;
import org.spring.testspring.exception.InvalidSigningInformation;
import org.spring.testspring.repository.UserRepository;
import org.spring.testspring.requset.Login;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {


    private final UserRepository userRepository;



    @Transactional
    public String signIn(Login login){

        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSigningInformation::new);
        Session session = user.addSession();

        return session.getAccessToken();
    }
}
