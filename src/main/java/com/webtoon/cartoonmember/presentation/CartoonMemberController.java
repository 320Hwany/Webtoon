package com.webtoon.cartoonmember.presentation;

import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoon.dto.response.CartoonListResult;
import com.webtoon.cartoonmember.dto.request.*;
import com.webtoon.cartoonmember.application.CartoonMemberService;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.response.ContentListResult;
import com.webtoon.global.error.BindingException;
import com.webtoon.global.resttemplate.RestTemplateService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.webtoon.cartoonmember.dto.request.CartoonMemberRating.toCartoonMemberRating;
import static com.webtoon.cartoonmember.dto.request.CartoonMemberSave.*;
import static com.webtoon.cartoonmember.dto.request.CartoonMemberThumbsUp.*;
import static com.webtoon.util.constant.ConstantCommon.*;

@RequiredArgsConstructor
@RestController
public class CartoonMemberController {

    private final CartoonMemberService cartoonMemberService;
    private final RestTemplateService restTemplateService;

    @PostMapping("/cartoonMember/read/{cartoonId}")
    public ResponseEntity<ContentListResult> memberReadCartoon(@LoginForMember MemberSession memberSession,
                                                               @PathVariable Long cartoonId) {

        CartoonMemberSave cartoonMemberSave = toCartoonMemberSave(cartoonId, memberSession.getId());
        cartoonMemberService.save(cartoonMemberSave);
        ContentGet contentGet = ContentGet.toContentGet(FIRST_PAGE, PAGE_LIMIT, cartoonId);
        ContentListResult contentListResult = restTemplateService.getContentList(contentGet);
        return ResponseEntity.ok(contentListResult);
    }

    @PostMapping("/cartoonMember/thumbsUp/{cartoonId}")
    public ResponseEntity<Void> thumbsUp(@LoginForMember MemberSession memberSession,
                                         @PathVariable Long cartoonId) {

        CartoonMemberThumbsUp cartoonMemberThumbsUp = toCartoonMemberThumbsUp(cartoonId, memberSession.getId());
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
            @ModelAttribute @Valid CartoonSearchAge cartoonSearchAge, BindingResult bindingResult) {

        BindingException.validate(bindingResult);
        List<CartoonCore> cartoonCoreList = cartoonMemberService.findAllByMemberAge(cartoonSearchAge);
        return ResponseEntity.ok(new CartoonListResult(cartoonCoreList.size(), cartoonCoreList));
    }

    @GetMapping("/cartoonMember/gender")
    public ResponseEntity<CartoonListResult> findAllByMemberGender(
            @ModelAttribute @Valid CartoonSearchGender cartoonSearchGender, BindingResult bindingResult) {

        BindingException.validate(bindingResult);
        List<CartoonCore> cartoonCoreList = cartoonMemberService.findAllByMemberGender(cartoonSearchGender);
        return ResponseEntity.ok(new CartoonListResult(cartoonCoreList.size(), cartoonCoreList));
    }

    @PostMapping("/cartoonMember/rating/{cartoonId}/{rating}")
    public ResponseEntity<Void> rating(@LoginForMember MemberSession memberSession,
                                       @PathVariable Long cartoonId,
                                       @PathVariable double rating) {

        CartoonMemberRating cartoonMemberRating = toCartoonMemberRating(cartoonId, memberSession.getId(), rating);
        cartoonMemberService.rating(cartoonMemberRating);
        return ResponseEntity.ok().build();
    }
}
