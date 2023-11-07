package org.spring.testspring.config;

import lombok.RequiredArgsConstructor;
import org.spring.testspring.config.data.UserSession;
import org.spring.testspring.domain.Session;
import org.spring.testspring.exception.UnAuthorized;
import org.spring.testspring.repository.SessionRepository;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("authorization");
        if(accessToken == null || accessToken.equals("")){
            throw new UnAuthorized();
        }
            Session session = sessionRepository.findByAccessToken(accessToken)
                    .orElseThrow(UnAuthorized::new);




        return new UserSession(session.getUser().getId());

    }
}
