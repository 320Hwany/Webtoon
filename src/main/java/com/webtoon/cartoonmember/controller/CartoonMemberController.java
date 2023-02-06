package com.webtoon.cartoonmember.controller;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.service.CartoonService;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.service.CartoonMemberService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class CartoonMemberController {

    private final CartoonMemberService cartoonMemberService;
    private final CartoonService cartoonService;

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

    @PostMapping("/cartoonMember/member")
    public ResponseEntity<List<CartoonResponse>> findAllForMember(@LoginForMember MemberSession memberSession) {
        List<Cartoon> cartoonList = cartoonMemberService.findAllForMember(memberSession.getId());
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);
        return ResponseEntity.ok(cartoonResponseList);
    }

    @PostMapping("/cartoonMember/member/likeList")
    public ResponseEntity<List<CartoonResponse>> findLikeListForMember(@LoginForMember MemberSession memberSession) {
        List<Cartoon> cartoonList = cartoonMemberService.findLikeListForMember(memberSession.getId());
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);
        return ResponseEntity.ok(cartoonResponseList);
    }

    @PostMapping("/rating/{cartoonId}/{rating}")
    public ResponseEntity<Void> rating(@LoginForMember MemberSession memberSession,
                                       @PathVariable Long cartoonId,
                                       @PathVariable Float rating) {
        CartoonMember cartoonMember =
                cartoonMemberService.getByCartoonIdAndMemberId(cartoonId, memberSession.getId());
        Cartoon cartoon = cartoonService.getById(cartoonMember.getCartoon().getId());
        cartoonService.rating(cartoon, rating);
        return ResponseEntity.ok().build();
    }
}
