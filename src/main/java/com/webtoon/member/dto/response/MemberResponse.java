package com.webtoon.member.dto.response;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {

    private String nickName;
    private String email;

    @Builder
    public MemberResponse(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;
    }

    public static MemberResponse getFromMember(Member member) {
        return MemberResponse.builder()
                .nickName(member.getNickName())
                .email(member.getEmail())
                .build();
    }

    public static MemberResponse getFromMemberSession(MemberSession memberSession) {
        return MemberResponse.builder()
                .nickName(memberSession.getNickName())
                .email(memberSession.getEmail())
                .build();
    }
}
