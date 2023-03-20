package com.webtoon.cartoon.presentation;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.dto.request.*;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.dto.response.CartoonListResult;
import com.webtoon.cartoon.application.CartoonService;
import com.webtoon.util.annotation.LoginForAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CartoonController {

    private final CartoonService cartoonService;

    @PostMapping("/cartoon")
    public ResponseEntity<Void> save(@LoginForAuthor AuthorSession authorSession,
                                     @RequestBody @Valid CartoonSave cartoonSave) {
        cartoonService.save(cartoonSave, authorSession);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cartoon/title")
    public ResponseEntity<CartoonListResult> getCartoonListByTitle(
            @ModelAttribute @Valid CartoonSearchTitle cartoonSearchTitle) {

        List<CartoonResponse> cartoonResponseList = cartoonService.findAllByTitle(cartoonSearchTitle);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @GetMapping("/cartoon/orderby/likes")
    public ResponseEntity<CartoonListResult> getCartoonListByCondOrderByLikes(
            @ModelAttribute @Valid CartoonSearchCond cartoonSearchDto) {

        List<CartoonResponse> cartoonResponseList =
                cartoonService.findAllByCartoonCondOrderByLikes(cartoonSearchDto);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @GetMapping("/cartoon/orderby/rating")
    public ResponseEntity<CartoonListResult> getCartoonListByCondOrderByRating(
            @RequestBody @Valid CartoonSearchCond cartoonSearchDto) {

        List<CartoonResponse> cartoonResponseList =
                cartoonService.findAllByCartoonCondOrderByRatingSet(cartoonSearchDto);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PatchMapping("/cartoon/{cartoonId}")
    public ResponseEntity<CartoonResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                  @PathVariable Long cartoonId,
                                                  @RequestBody @Valid CartoonUpdate cartoonUpdate) {

        CartoonUpdateSet cartoonUpdateSet = CartoonUpdateSet.toCartoonUpdateSet(authorSession, cartoonUpdate, cartoonId);
        CartoonResponse cartoonResponse = cartoonService.update(cartoonUpdateSet);
        return ResponseEntity.ok(cartoonResponse);
    }

    @DeleteMapping("/cartoon/{cartoonId}")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId) {
        cartoonService.delete(authorSession, cartoonId);
        return ResponseEntity.ok().build();
    }
}
