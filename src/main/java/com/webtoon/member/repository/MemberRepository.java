package com.webtoon.member.repository;

import com.webtoon.member.domain.Member;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Optional<Member> findByNickName(String nickName);
    Optional<Member> findByEmail(String email);

    long count();
}
