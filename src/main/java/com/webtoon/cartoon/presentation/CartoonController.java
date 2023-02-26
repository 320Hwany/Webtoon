package com.webtoon.cartoon.presentation;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.*;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.dto.response.CartoonListResult;
import com.webtoon.cartoon.application.CartoonService;
import com.webtoon.cartoon.application.CartoonTransactionService;
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
    private final CartoonTransactionService cartoonTransactionService;

    @PostMapping("/cartoon")
    public ResponseEntity<Void> save(@LoginForAuthor AuthorSession authorSession,
                                     @RequestBody @Valid CartoonSave cartoonSave) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonSave(cartoonSave);
        Cartoon.validateEnumTypeValid(cartoonEnumField);
        cartoonTransactionService.saveSet(cartoonSave, authorSession);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cartoon/title")
    public ResponseEntity<CartoonListResult> getCartoonListByTitle(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        List<CartoonResponse> cartoonResponseList = cartoonTransactionService.findAllByTitleSet(cartoonSearchDto);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PostMapping("/cartoon/orderby/likes")
    public ResponseEntity<CartoonListResult> getCartoonListByCondOrderByLikes(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        cartoonService.validateGenreValid(cartoonSearchDto.getGenre());
        List<CartoonResponse> cartoonResponseList =
                cartoonTransactionService.findAllByCartoonCondOrderByLikesSet(cartoonSearchDto);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PostMapping("/cartoon/orderby/rating")
    public ResponseEntity<CartoonListResult> getCartoonListByCondOrderByRating(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        cartoonService.validateGenreValid(cartoonSearchDto.getGenre());
        List<CartoonResponse> cartoonResponseList =
                cartoonTransactionService.findAllByCartoonCondOrderByRatingSet(cartoonSearchDto);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PatchMapping("/cartoon/{cartoonId}")
    public ResponseEntity<Void> update(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId,
                                       @RequestBody @Valid CartoonUpdate cartoonUpdate) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonUpdate(cartoonUpdate);
        Cartoon.validateEnumTypeValid(cartoonEnumField);
        cartoonService.validateAuthorityForCartoon(authorSession, cartoonId);
        cartoonTransactionService.updateSet(cartoonUpdate, cartoonId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cartoon/{cartoonId}")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId) {
        cartoonService.validateAuthorityForCartoon(authorSession, cartoonId);
        cartoonTransactionService.deleteSet(cartoonId);
        return ResponseEntity.ok().build();
    }
}
