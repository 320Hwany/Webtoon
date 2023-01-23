package com.webtoon.member.service;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.MemberDuplicationException;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void signup(MemberSignup memberSignup) {
        Member member = Member.getFromMemberSignup(memberSignup);
        memberRepository.save(member);
    }

    @Transactional
    public Member update(MemberSession memberSession, MemberUpdate memberUpdate) {
        Member member = memberRepository.getById(memberSession.getId());
        member.update(memberUpdate);
        return member;
    }

    public MemberSession makeMemberSession(MemberLogin memberLogin) {
        Member member = memberRepository.getByEmailAndPassword(memberLogin.getEmail(), memberLogin.getPassword());
        MemberSession memberSession = MemberSession.getByMember(member);
        return memberSession;
    }

    public void checkDuplication(MemberSignup memberSignup) {
        Optional<Member> findMemberByNickName = memberRepository.findByNickName(memberSignup.getNickName());
        Optional<Member> findMemberByEmail = memberRepository.findByEmail(memberSignup.getEmail());
        if (findMemberByNickName.isPresent() || findMemberByEmail.isPresent()) {
            throw new MemberDuplicationException();
        }
    }

    public void makeSessionForMemberSession(MemberSession memberSession, HttpServletRequest request) {
        memberSession.makeSession(request);
    }
}
