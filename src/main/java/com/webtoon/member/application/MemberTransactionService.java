package com.webtoon.member.application;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberTransactionService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signupSet(MemberSignup memberSignup) {
        memberService.checkDuplication(memberSignup);
        Member member = Member.getFromMemberSignup(memberSignup, passwordEncoder);
        memberService.save(member);
    }

    @Transactional
    public Member updateSet(MemberSession memberSession, MemberUpdate memberUpdate) {
        Member member = memberService.getById(memberSession.getId());
        member.update(memberUpdate, passwordEncoder);
        return member;
    }

    @Transactional
    public void deleteSet(MemberSession memberSession) {
        Member member = memberService.getById(memberSession.getId());
        memberService.delete(member);
    }

    @Transactional
    public void chargeCoinTransactionSet(MemberSession memberSession, MemberCharge memberCharge) {
        Member member = memberService.getById(memberSession.getId());
        member.chargeCoin(memberCharge.getChargeAmount());
    }
}
