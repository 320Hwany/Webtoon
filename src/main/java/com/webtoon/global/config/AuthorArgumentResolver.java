package com.webtoon.global.config;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.exception.AuthorUnauthorizedException;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.util.annotation.LoginForAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class AuthorArgumentResolver implements HandlerMethodArgumentResolver {

    private final AuthorRepository authorRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasAuthorSessionType = parameter.getParameterType().equals(AuthorSession.class);
        boolean hasLoginForAuthorAnnotation = parameter.hasParameterAnnotation(LoginForAuthor.class);
        return hasAuthorSessionType && hasLoginForAuthorAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new AuthorUnauthorizedException();
        }
        AuthorSession authorSession = (AuthorSession) session.getAttribute("authorSession");
        authorRepository.validateAuthorPresent(authorSession);
        return authorSession;
    }
}
