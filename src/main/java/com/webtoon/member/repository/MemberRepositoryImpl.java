package com.webtoon.member.repository;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.exception.MemberForbiddenException;
import com.webtoon.member.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepositoryImpl implements MemberRepository {

    private final MemberJpaRepository memberJpaRepository;

    @Override
    public void save(Member member) {
        memberJpaRepository.save(member);
    }

    @Override
    public Member getById(Long id) {
        return memberJpaRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Member getByEmail(String email) {
        return memberJpaRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public Optional<Member> findByNickname(String nickname) {
        return memberJpaRepository.findByNickname(nickname);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Override
    public void validateMemberPresent(MemberSession memberSession) {
        memberJpaRepository.findById(memberSession.getId())
                .orElseThrow(MemberForbiddenException::new);
    }

    @Override
    public void delete(Member member) {
        memberJpaRepository.delete(member);
    }

    @Override
    public void deleteAll() {
        memberJpaRepository.deleteAll();
    }

    @Override
    public long count() {
        return memberJpaRepository.count();
    }
}
