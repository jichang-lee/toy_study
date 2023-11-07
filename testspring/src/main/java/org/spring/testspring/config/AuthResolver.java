package org.spring.testspring.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.spring.testspring.config.data.UserSession;
import org.spring.testspring.domain.Session;
import org.spring.testspring.exception.UnAuthorized;
import org.spring.testspring.repository.SessionRepository;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("appConfig=====>{}",appConfig);
        String jws = webRequest.getHeader("Authorization");

        if (jws == null && jws.equals("")){
            throw new UnAuthorized();
        }



        try{
            Jws<Claims> claimsJws = Jwts.parser().
                    setSigningKey(appConfig.getJwtKey()).
                    build().
                    parseClaimsJws(jws);
            String userID = claimsJws.getBody().getSubject();
            return new UserSession(Long.parseLong(userID));
        }catch (JwtException e){
            throw new UnAuthorized();
        }

    }
}
