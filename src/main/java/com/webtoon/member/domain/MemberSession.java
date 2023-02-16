package com.webtoon.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Objects;

@Getter
@NoArgsConstructor
public class MemberSession implements Serializable {

    private Long id;

    private String nickname;

    private String email;

    private String password;

    @Builder
    public MemberSession(Long id, String nickname, String email, String password) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    public static MemberSession getFromMember(Member member) {
        return MemberSession.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    public void makeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("memberSession", this);
    }

    public void invalidate(HttpServletRequest httpServletRequest) {
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
    }
}
