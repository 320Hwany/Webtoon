package com.webtoon.member.presentation;

import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.dto.request.MemberCharge;
import com.webtoon.member.dto.request.MemberLogin;
import com.webtoon.member.dto.request.MemberSignup;
import com.webtoon.member.dto.request.MemberUpdate;
import com.webtoon.member.dto.response.MemberResponse;
import com.webtoon.member.application.MemberService;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        memberService.signup(memberSignup);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/member/login")
    public ResponseEntity<MemberResponse> login(@RequestBody @Valid MemberLogin memberLogin,
                                                HttpServletRequest request) {

        MemberResponse memberResponse = memberService.login(memberLogin, request);
        return ResponseEntity.ok(memberResponse);
    }

    @PatchMapping("/member")
    public ResponseEntity<MemberResponse> update(@LoginForMember MemberSession memberSession,
                                                 @RequestBody @Valid MemberUpdate memberUpdate) {
        MemberResponse memberResponse = memberService.update(memberSession, memberUpdate);
        return ResponseEntity.ok(memberResponse);
    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> delete(@LoginForMember MemberSession memberSession) {
        memberService.delete(memberSession);
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
        memberService.chargeCoin(memberSession, memberCharge);
        return ResponseEntity.ok().build();
    }
}
