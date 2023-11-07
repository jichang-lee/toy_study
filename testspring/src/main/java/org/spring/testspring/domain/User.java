package org.spring.testspring.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private LocalDate createAt;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Session> session = new ArrayList<>();

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createAt = LocalDate.now();
    }

    public Session addSession() {
        Session userSession = Session.builder()
                .user(this)
                .build();

        session.add(userSession);

        return userSession;
    }
}