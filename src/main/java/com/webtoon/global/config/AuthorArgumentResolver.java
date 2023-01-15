package com.webtoon.global.config;

import com.webtoon.author.dto.request.AuthorSession;
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
        return parameter.hasParameterAnnotation(LoginForAuthor.class);
    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new AuthorUnauthorizedException();
        }
        AuthorSession authorSession = (AuthorSession) session.getAttribute("authorSession");
        authorRepository.getByEmailAndPassword(authorSession.getEmail(), authorSession.getPassword());
        return authorSession;
    }
}
