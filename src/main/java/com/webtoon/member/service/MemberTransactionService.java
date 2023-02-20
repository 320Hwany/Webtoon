package com.webtoon.member.service;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberTransactionService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Member updateTransactionSet(MemberSession memberSession, MemberUpdate memberUpdate) {
        Member member = memberRepository.getById(memberSession.getId());
        member.update(memberUpdate, passwordEncoder);
        return member;
    }

    @Transactional
    public void deleteTransactionSet(MemberSession memberSession) {
        Member member = memberRepository.getById(memberSession.getId());
        memberRepository.delete(member);
    }

    @Transactional
    public void chargeCoinTransactionSet(MemberSession memberSession, MemberCharge memberCharge) {
        Member member = memberRepository.getById(memberSession.getId());
        member.chargeCoin(memberCharge.getChargeAmount());
    }
}
