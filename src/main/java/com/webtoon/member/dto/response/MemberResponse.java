package com.webtoon.member.dto.response;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

    private String nickname;
    private String email;

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
