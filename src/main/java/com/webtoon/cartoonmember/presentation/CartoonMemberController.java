package com.webtoon.cartoonmember.presentation;

import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoon.dto.response.CartoonListResult;
import com.webtoon.cartoonmember.dto.request.CartoonMemberRating;
import com.webtoon.cartoonmember.dto.request.CartoonMemberSave;
import com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp;
import com.webtoon.cartoonmember.application.CartoonMemberService;
import com.webtoon.cartoonmember.dto.request.CartoonSearchAge;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.content.dto.response.ContentListResult;
import com.webtoon.global.openfeign.DynamicUrlOpenFeign;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import com.webtoon.util.constant.ConstantCommon;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CartoonMemberController {

    private final CartoonMemberService cartoonMemberService;
    private final DynamicUrlOpenFeign feign;

    @PostMapping("/cartoonMember/read/{cartoonId}")
    public ResponseEntity<ContentListResult> memberReadCartoon(@LoginForMember MemberSession memberSession,
                                                               @PathVariable Long cartoonId) {

        CartoonMemberSave cartoonMemberSave =
                CartoonMemberSave.getFromCartoonIdAndMemberId(cartoonId, memberSession.getId());
        cartoonMemberService.save(cartoonMemberSave);
        return feign.findContentList(cartoonId, ConstantCommon.firstPage);
    }

    @PostMapping("/cartoonMember/thumbsUp/{cartoonId}")
    public ResponseEntity<Void> thumbsUp(@LoginForMember MemberSession memberSession,
                                         @PathVariable Long cartoonId) {

        CartoonMemberThumbsUp cartoonMemberThumbsUp =
                CartoonMemberThumbsUp.getFromCartoonIdAndMemberId(cartoonId, memberSession.getId());
        cartoonMemberService.thumbsUp(cartoonMemberThumbsUp);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cartoonMember/member")
    public ResponseEntity<CartoonListResult> findAllForMember(@LoginForMember MemberSession memberSession) {

        List<CartoonMemberResponse> cartoonMemberResponseList =
                cartoonMemberService.findAllCartoonByMemberId(memberSession.getId());
        return ResponseEntity.ok(new CartoonListResult(cartoonMemberResponseList.size(), cartoonMemberResponseList));
    }

    @GetMapping("/cartoonMember/member/likeList")
    public ResponseEntity<CartoonListResult> findLikeListForMember(@LoginForMember MemberSession memberSession) {

        List<CartoonMemberResponse> cartoonMemberResponseList =
                cartoonMemberService.findLikeListForMember(memberSession.getId());
        return ResponseEntity.ok(new CartoonListResult(cartoonMemberResponseList.size(), cartoonMemberResponseList));
    }

    @GetMapping("/cartoonMember/ageRange")
    public ResponseEntity<CartoonListResult> findAllByMemberAge(
            @ModelAttribute @Valid CartoonSearchAge cartoonSearchAge) {

        List<CartoonCore> cartoonCoreList = cartoonMemberService.findAllByMemberAge(cartoonSearchAge);
        return ResponseEntity.ok(new CartoonListResult(cartoonCoreList.size(), cartoonCoreList));
    }

    @PostMapping("/cartoonMember/rating/{cartoonId}/{rating}")
    public ResponseEntity<Void> rating(@LoginForMember MemberSession memberSession,
                                       @PathVariable Long cartoonId,
                                       @PathVariable double rating) {

        CartoonMemberRating cartoonMemberRating =
                CartoonMemberRating.getFromIdAndRating(cartoonId, memberSession.getId(), rating);
        cartoonMemberService.rating(cartoonMemberRating);
        return ResponseEntity.ok().build();
    }
}
