package org.spring.testspring.repository;

import org.spring.testspring.domain.Session;
import org.spring.testspring.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session,Long> {


   Optional<Session> findByAccessToken(String accessToken);
}
