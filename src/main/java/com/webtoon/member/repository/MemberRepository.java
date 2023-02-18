package com.webtoon.member.repository;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Member getById(Long id);

    Member getByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmail(String email);

    void validateMemberPresent(MemberSession memberSession);

    void delete(Member member);
    long count();
}
