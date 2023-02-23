package com.webtoon.cartoonmember.presentation;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.response.CartoonListResult;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoonmember.dto.request.CartoonMemberRating;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp;
import com.webtoon.cartoonmember.application.CartoonMemberService;
import com.webtoon.cartoonmember.application.CartoonMemberTransactionalService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CartoonMemberController {

    private final CartoonMemberService cartoonMemberService;
    private final CartoonMemberTransactionalService cartoonMemberTransactionalService;

    @PostMapping("/cartoonMember/read/{cartoonId}")
    public ResponseEntity<Void> memberReadCartoon(@LoginForMember MemberSession memberSession,
                                                  @PathVariable Long cartoonId) {

        CartoonMemberSave cartoonMemberSave =
                CartoonMemberSave.getFromCartoonIdAndMemberId(cartoonId, memberSession.getId());
        cartoonMemberTransactionalService.saveSet(cartoonMemberSave);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cartoonMember/thumbsUp/{cartoonId}")
    public ResponseEntity<Void> thumbsUp(@LoginForMember MemberSession memberSession,
                                         @PathVariable Long cartoonId) {
        CartoonMemberThumbsUp cartoonMemberThumbsUp =
                CartoonMemberThumbsUp.getFromCartoonIdAndMemberId(cartoonId, memberSession.getId());
        cartoonMemberTransactionalService.thumbsUpTransactionSet(cartoonMemberThumbsUp);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cartoonMember/member")
    public ResponseEntity<CartoonListResult> findAllForMember(@LoginForMember MemberSession memberSession) {
        List<Cartoon> cartoonList = cartoonMemberService.findAllCartoonByMemberId(memberSession.getId());
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @GetMapping("/cartoonMember/member/likeList")
    public ResponseEntity<CartoonListResult> findLikeListForMember(@LoginForMember MemberSession memberSession) {
        List<Cartoon> cartoonList = cartoonMemberService.findLikeListForMember(memberSession.getId());
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PostMapping("/cartoonMember/ageRange")
    public ResponseEntity<CartoonListResult> findAllByMemberAge(@RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonMemberService.findAllByMemberAge(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }


    @PostMapping("/cartoonMember/rating/{cartoonId}/{rating}")
    public ResponseEntity<Void> rating(@LoginForMember MemberSession memberSession,
                                       @PathVariable Long cartoonId,
                                       @PathVariable double rating) {
        CartoonMemberRating cartoonMemberRating =
                CartoonMemberRating.getFromIdAndRating(cartoonId, memberSession.getId(), rating);
        cartoonMemberTransactionalService.ratingTransactionSet(cartoonMemberRating);
        return ResponseEntity.ok().build();
    }
}
