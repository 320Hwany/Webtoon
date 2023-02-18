package com.webtoon.global.config;

import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.exception.MemberUnauthorizedException;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
public class MemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasMemberSessionType = parameter.getParameterType().equals(MemberSession.class);
        boolean hasLoginForMemberAnnotation = parameter.hasParameterAnnotation(LoginForMember.class);
        return hasMemberSessionType && hasLoginForMemberAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = httpServletRequest.getSession(false);
        if (session == null) {
            throw new MemberUnauthorizedException();
        }

        MemberSession memberSession = (MemberSession) session.getAttribute("memberSession");
        memberRepository.validateMemberPresent(memberSession);

        return memberSession;
    }
}
