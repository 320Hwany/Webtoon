package com.webtoon.member.repository;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Optional<Member> findByNickName(String nickName);

    Optional<Member> findByEmail(String email);

    Member getByEmailAndPassword(String email, String password);

    void validateMemberPresent(MemberSession memberSession);
    long count();
}
