package com.webtoon.member.application;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.exception.MemberDuplicationException;
import com.webtoon.member.exception.MemberUnauthorizedException;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    public Member getById(Long memberId) {
        return memberRepository.getById(memberId);
    }

    @Transactional
    public void save(Member member) {
        memberRepository.save(member);
    }

    @Transactional
    public void delete(Member member) {
        memberRepository.delete(member);
    }
}
