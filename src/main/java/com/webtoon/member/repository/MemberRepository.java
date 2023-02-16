package com.webtoon.member.repository;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;

import java.util.Optional;

public interface MemberRepository {

    void save(Member member);

    Member getById(Long id);

    Optional<Member> findBynickname(String nickname);

    Optional<Member> findByEmail(String email);

    Member getByEmailAndPassword(String email, String password);

    void validateMemberPresent(MemberSession memberSession);

    void delete(Member member);
    long count();
}
