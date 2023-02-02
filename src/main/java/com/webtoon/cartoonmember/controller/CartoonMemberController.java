package com.webtoon.cartoonmember.controller;

import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.service.CartoonMemberService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CartoonMemberController {

    private final CartoonMemberService cartoonMemberService;

    @PostMapping("/read/{cartoonId}")
    public ResponseEntity<Void> memberReadCartoon(@LoginForMember MemberSession memberSession,
                                                  @PathVariable Long cartoonId) {

        CartoonMemberSave cartoonMemberSave =
                CartoonMemberSave.getFromCartoonIdAndMemberId(cartoonId, memberSession.getId());
        cartoonMemberService.save(cartoonMemberSave);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/thumbsUp/{cartoonId}")
    public ResponseEntity<Void> thumbsUp(@LoginForMember MemberSession memberSession,
                                         @PathVariable Long cartoonId) {
        cartoonMemberService.thumbsUp(cartoonId, memberSession.getId());
        cartoonMemberService.addLike(cartoonId);
        return ResponseEntity.ok().build();
    }
}
