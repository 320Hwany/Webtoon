package com.webtoon.member.presentation;

import com.webtoon.member.domain.Member;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.dto.response.MemberResponse;
import com.webtoon.member.application.MemberService;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/member/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid MemberSignup memberSignup) {
        memberService.signupSet(memberSignup);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/member/login")
    public ResponseEntity<MemberResponse> login(@RequestBody @Valid MemberLogin memberLogin,
                                                HttpServletRequest request) {

        MemberSession memberSession = memberService.makeMemberSession(memberLogin);
        memberService.makeSessionForMemberSession(memberSession, request);
        MemberResponse memberResponse = MemberResponse.getFromMemberSession(memberSession);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/member")
    public ResponseEntity<MemberResponse> update(@LoginForMember MemberSession memberSession,
                                                 @RequestBody @Valid MemberUpdate memberUpdate) {
        Member member = memberService.updateSet(memberSession, memberUpdate);
        MemberResponse memberResponse = MemberResponse.getFromMember(member);
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> delete(@LoginForMember MemberSession memberSession) {
        memberService.deleteSet(memberSession);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/member/logout")
    public ResponseEntity<Void> logout(@LoginForMember MemberSession memberSession,
                                       HttpServletRequest httpServletRequest) {
        memberService.logout(memberSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/member/charge")
    public ResponseEntity<Void> charge(@LoginForMember MemberSession memberSession,
                                       @RequestBody @Valid MemberCharge memberCharge) {
        memberService.chargeCoinTransactionSet(memberSession, memberCharge);
        return ResponseEntity.ok().build();
    }
}
