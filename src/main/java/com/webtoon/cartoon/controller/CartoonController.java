package com.webtoon.cartoon.controller;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonEnumField;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.service.CartoonService;
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
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonSave(cartoonSave);
        cartoonService.checkEnumTypeValid(cartoonEnumField);
        cartoonService.save(cartoonSave, authorSession);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/cartoon/title")
    public ResponseEntity<List<CartoonResponse>> getCartoonListByTitle(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByTitle(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);

        return ResponseEntity.ok(cartoonResponseList);
    }

    @GetMapping("/cartoon/genre")
    public ResponseEntity<List<CartoonResponse>> getCartoonListByGenre(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        cartoonService.checkGenreValid(cartoonSearchDto.getGenre());
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByGenre(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);

        return ResponseEntity.ok(cartoonResponseList);
    }

    @GetMapping("/cartoon/likes")
    public ResponseEntity<List<CartoonResponse>> getCartoonListOrderByLikes(
            @RequestBody @Valid CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllOrderByLikes(cartoonSearch);
        List<CartoonResponse> cartoonResponseList = CartoonResponse.getFromCartoonList(cartoonList);

        return ResponseEntity.ok(cartoonResponseList);
    }

    @PatchMapping("/cartoon/{cartoonId}")
    public ResponseEntity<CartoonResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                  @PathVariable Long cartoonId,
                                                  @RequestBody @Valid CartoonUpdate cartoonUpdate) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonUpdate(cartoonUpdate);
        cartoonService.checkEnumTypeValid(cartoonEnumField);
        cartoonService.checkAuthorityForCartoon(cartoonId, authorSession);
        Cartoon afterUpdateCartoon = cartoonService.update(cartoonId, cartoonUpdate);
        CartoonResponse cartoonResponse = CartoonResponse.getFromCartoon(afterUpdateCartoon);

        return ResponseEntity.ok(cartoonResponse);
    }
}
