package com.webtoon.author.controller;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.dto.response.AuthorListResult;
import com.webtoon.author.service.AuthorService;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.util.annotation.LoginForAuthor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping("/author/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid AuthorSignup authorSignup) {
        authorService.checkDuplication(authorSignup);
        authorService.signup(authorSignup);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/login")
    public ResponseEntity<AuthorResponse> login(@RequestBody @Valid AuthorLogin authorLogin,
                                                HttpServletRequest httpServletRequest) {
        AuthorSession authorSession = authorService.makeAuthorSession(authorLogin);
        authorService.makeSessionForAuthorSession(authorSession, httpServletRequest);
        AuthorResponse authorResponse = AuthorResponse.getFromAuthorSession(authorSession);
        return ResponseEntity.ok(authorResponse);
    }

    @PostMapping("/author/nickname")
    public ResponseEntity<AuthorListResult> getAuthorByNickname(@RequestBody CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<AuthorCartoonResponse> authorCartoonResponseList =
                authorService.findAllByNicknameContains(cartoonSearch);

        return ResponseEntity.ok(new AuthorListResult(authorCartoonResponseList.size(), authorCartoonResponseList));
    }

    @PatchMapping("/author")
    public ResponseEntity<AuthorResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                 @RequestBody @Valid AuthorUpdate authorUpdate) {
        Author author = authorService.getById(authorSession.getId());
        authorService.update(author, authorUpdate);
        AuthorResponse authorResponse = AuthorResponse.getFromAuthor(author);
        return ResponseEntity.ok(authorResponse);
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        Author author = authorService.getById(authorSession.getId());
        authorService.delete(author);
        authorService.invalidateSession(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/logout")
    public ResponseEntity<Void> logout(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorService.invalidateSession(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }
}
