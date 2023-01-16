package com.webtoon.cartoon.controller;

import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.service.CartoonService;
import com.webtoon.util.annotation.LoginForAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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
}