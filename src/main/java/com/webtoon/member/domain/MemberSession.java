package com.webtoon.member.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Objects;

import static com.webtoon.util.constant.ConstantCommon.MEMBER_SESSION;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSession implements Serializable {

    private Long id;

    private String nickname;

    private String email;

    private String password;

    @Builder
    private MemberSession(Long id, String nickname, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static MemberSession toEntity(Member member) {
        return MemberSession.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    public void makeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute(MEMBER_SESSION, this);
    }

    public void invalidate(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
