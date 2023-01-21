package com.webtoon.member.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.member.domain.Member;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.exception.MemberDuplicationException;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public void checkDuplication(MemberSignup memberSignup) {
        Optional<Member> findMemberByNickName = memberRepository.findByNickName(memberSignup.getNickName());
        Optional<Member> findMemberByEmail = memberRepository.findByEmail(memberSignup.getEmail());
        if (findMemberByNickName.isPresent() || findMemberByEmail.isPresent()) {
            throw new MemberDuplicationException();
        }
    }
}
