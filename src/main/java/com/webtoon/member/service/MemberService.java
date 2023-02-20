package com.webtoon.member.service;

import com.webtoon.content.domain.Content;
import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.exception.MemberDuplicationException;
import com.webtoon.member.exception.MemberUnauthorizedException;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void signup(MemberSignup memberSignup) {
        Member member = Member.getFromMemberSignup(memberSignup, passwordEncoder);
        memberRepository.save(member);
    }

    public MemberSession makeMemberSession(MemberLogin memberLogin) {
        Member member = memberRepository.getByEmail(memberLogin.getEmail());
        if (passwordEncoder.matches(memberLogin.getPassword(), member.getPassword())) {
            MemberSession memberSession = MemberSession.getFromMember(member);
            return memberSession;
        }
        throw new MemberUnauthorizedException();
    }

    public void checkDuplication(MemberSignup memberSignup) {
        Optional<Member> findMemberByNickname = memberRepository.findByNickname(memberSignup.getNickname());
        Optional<Member> findMemberByEmail = memberRepository.findByEmail(memberSignup.getEmail());
        if (findMemberByNickname.isPresent() || findMemberByEmail.isPresent()) {
            throw new MemberDuplicationException();
        }
    }

    public void makeSessionForMemberSession(MemberSession memberSession, HttpServletRequest request) {
        memberSession.makeSession(request);
    }

    public void logout(MemberSession memberSession, HttpServletRequest httpServletRequest) {
        memberSession.invalidate(httpServletRequest);
    }
}
