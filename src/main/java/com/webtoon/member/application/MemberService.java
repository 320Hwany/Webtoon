package com.webtoon.member.application;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.dto.response.MemberResponse;
import com.webtoon.member.exception.MemberDuplicationException;
import com.webtoon.member.exception.MemberEnumTypeException;
import com.webtoon.member.exception.MemberNotMatchException;
import com.webtoon.member.exception.MemberUnauthorizedException;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.util.enumerated.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Transactional
    public void signup(MemberSignup memberSignup) {
        validateDuplication(memberSignup);
        validationGender(memberSignup);
        Member member = memberSignup.toEntity(passwordEncoder);
        memberRepository.save(member);
    }

    public void validateDuplication(MemberSignup memberSignup) {
        Optional<Member> findMemberByNickname = memberRepository.findByNickname(memberSignup.getNickname());
        Optional<Member> findMemberByEmail = memberRepository.findByEmail(memberSignup.getEmail());
        if (findMemberByNickname.isPresent() || findMemberByEmail.isPresent()) {
            throw new MemberDuplicationException();
        }
    }

    public void validationGender(MemberSignup memberSignup) {
        String gender = memberSignup.getGender();
        if (!Gender.validateValid(gender)) {
            throw new MemberEnumTypeException(Gender.validateValid(gender));
        }
    }

    public MemberResponse login(MemberLogin memberLogin, HttpServletRequest request) {
        Member member = memberRepository.getByEmail(memberLogin.getEmail());
        if (passwordEncoder.matches(memberLogin.getPassword(), member.getPassword())) {
            MemberSession memberSession = MemberSession.toEntity(member);
            memberSession.makeSession(request);
            return MemberResponse.getFromMemberSession(memberSession);
        }
        throw new MemberNotMatchException();
    }

    @Transactional
    public MemberResponse update(MemberSession memberSession, MemberUpdate memberUpdate) {
        Member member = memberRepository.getById(memberSession.getId());
        member.update(memberUpdate, passwordEncoder);
        return MemberResponse.getFromMember(member);
    }

    @Transactional
    public void delete(MemberSession memberSession) {
        Member member = memberRepository.getById(memberSession.getId());
        memberRepository.delete(member);
    }

    @Transactional
    public void chargeCoin(MemberSession memberSession, MemberCharge memberCharge) {
        Member member = memberRepository.getById(memberSession.getId());
        member.chargeCoin(memberCharge.getChargeAmount());
    }

    public void logout(MemberSession memberSession, HttpServletRequest httpServletRequest) {
        memberSession.invalidate(httpServletRequest);
    }
}
