package com.webtoon.member.dto.response;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberResponse {

    private String nickname;
    private String email;

    @Builder
    private MemberResponse(String nickname, String email) {
        this.nickname = nickname;
        this.email = email;
    }

    public static MemberResponse getFromMember(Member member) {
        return MemberResponse.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .build();
    }

    public static MemberResponse getFromMemberSession(MemberSession memberSession) {
        return MemberResponse.builder()
                .nickname(memberSession.getNickname())
                .email(memberSession.getEmail())
                .build();
    }
}
