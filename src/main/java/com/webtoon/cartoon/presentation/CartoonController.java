package com.webtoon.cartoon.presentation;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
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

        List<CartoonResponse> cartoonResponseList = cartoonService.findAllByTitleSet(cartoonSearchTitle);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @GetMapping("/cartoon/orderby/likes")
    public ResponseEntity<CartoonListResult> getCartoonListByCondOrderByLikes(
            @ModelAttribute @Valid CartoonSearchDto cartoonSearchDto) {

        List<CartoonResponse> cartoonResponseList =
                cartoonService.findAllByCartoonCondOrderByLikesSet(cartoonSearchDto);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @GetMapping("/cartoon/orderby/rating")
    public ResponseEntity<CartoonListResult> getCartoonListByCondOrderByRating(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {

        List<CartoonResponse> cartoonResponseList =
                cartoonService.findAllByCartoonCondOrderByRatingSet(cartoonSearchDto);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PatchMapping("/cartoon/{cartoonId}")
    public ResponseEntity<Void> update(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId,
                                       @RequestBody @Valid CartoonUpdate cartoonUpdate) {

        cartoonService.update(authorSession, cartoonUpdate, cartoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cartoon/{cartoonId}")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId) {
        cartoonService.delete(authorSession, cartoonId);
        return ResponseEntity.ok().build();
    }
}
