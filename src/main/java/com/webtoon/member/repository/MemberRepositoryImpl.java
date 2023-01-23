package com.webtoon.member.repository;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
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
    public Optional<Member> findByNickName(String nickName) {
        return memberJpaRepository.findByNickName(nickName);
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        return memberJpaRepository.findByEmail(email);
    }

    @Override
    public Member getByEmailAndPassword(String email, String password) {
        return memberJpaRepository.findByEmailAndPassword(email, password)
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public void validateMemberPresent(MemberSession memberSession) {
        memberJpaRepository.findById(memberSession.getId())
                .orElseThrow(MemberNotFoundException::new);
    }

    @Override
    public long count() {
        return memberJpaRepository.count();
    }
}
