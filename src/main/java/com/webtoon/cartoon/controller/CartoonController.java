package com.webtoon.cartoon.controller;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.dto.response.CartoonListResult;
import com.webtoon.cartoon.service.CartoonService;
import com.webtoon.cartoon.service.CartoonTransactionService;
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
        cartoonTransactionService.saveTransactionSet(cartoonSave, authorSession);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/cartoon/title")
    public ResponseEntity<CartoonListResult> getCartoonListByTitle(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByTitle(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);
        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PostMapping("/cartoon/genre")
    public ResponseEntity<CartoonListResult> getCartoonListByGenre(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        cartoonService.validateGenreValid(cartoonSearchDto.getGenre());
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByGenre(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);

        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PostMapping("/cartoon/likes")
    public ResponseEntity<CartoonListResult> getCartoonListOrderByLikes(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllOrderByLikes(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);

        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PostMapping("/cartoon/day")
    public ResponseEntity<CartoonListResult> getCartoonListByDay(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByDayOfTheWeek(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);

        return ResponseEntity.ok(new CartoonListResult(cartoonResponseList.size(), cartoonResponseList));
    }

    @PatchMapping("/cartoon/{cartoonId}")
    public ResponseEntity<Void> update(@LoginForAuthor AuthorSession authorSession,
                                                  @PathVariable Long cartoonId,
                                                  @RequestBody @Valid CartoonUpdate cartoonUpdate) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonUpdate(cartoonUpdate);
        Cartoon.validateEnumTypeValid(cartoonEnumField);
        cartoonService.validateAuthorityForCartoon(authorSession, cartoonId);
        cartoonTransactionService.updateTransactionSet(cartoonId, cartoonUpdate);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/cartoon/{cartoonId}")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId) {
        cartoonService.validateAuthorityForCartoon(authorSession, cartoonId);
        cartoonTransactionService.deleteTransactionSet(cartoonId);
        return ResponseEntity.ok().build();
    }
}
