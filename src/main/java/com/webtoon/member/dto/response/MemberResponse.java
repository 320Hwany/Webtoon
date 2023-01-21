package com.webtoon.member.dto.response;

import com.webtoon.member.domain.MemberSession;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    private String nickName;
    private String email;
    private String password;

    @Builder
    public MemberResponse(String nickName, String email, String password) {
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

    public static MemberResponse getFromMemberSession(MemberSession memberSession) {
        return MemberResponse.builder()
                .nickName(memberSession.getNickName())
                .email(memberSession.getEmail())
                .password(memberSession.getPassword())
                .build();
    }
}
