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

    public static MemberSession getFromMember(Member member) {
        return MemberSession.builder()
                .id(member.getId())
                .nickName(member.getNickName())
                .email(member.getEmail())
                .password(member.getPassword())
                .build();
    }

    public void makeSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("memberSession", this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberSession that = (MemberSession) o;
        return Objects.equals(id, that.id) && Objects.equals(nickName, that.nickName) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickName, email, password);
    }
}
