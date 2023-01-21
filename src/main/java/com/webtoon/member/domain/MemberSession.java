package com.webtoon.member.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Getter
@NoArgsConstructor
public class MemberSession implements Serializable {

    private Long id;

    private String nickName;

    private String email;

    private String password;

    @Builder
    public MemberSession(Long id, String nickName, String email, String password) {
        this.id = id;
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public void makeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("memberSession", this);
    }

    public static MemberSession getByMember(Member member) {
        return MemberSession.builder()
                .id(member.getId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }
}
