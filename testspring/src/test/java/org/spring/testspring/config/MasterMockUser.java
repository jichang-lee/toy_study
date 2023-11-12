package org.spring.testspring.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = MasterMockSecurityContext.class)
public @interface MasterMockUser {

    String name () default "이지창";
    String email () default "jichang@naver.com";
    String password () default "";
//    String role () default "ROLE_ADMIN";
}
