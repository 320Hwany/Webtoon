package com.webtoon.author.presentation;

import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.dto.response.AuthorListResult;
import com.webtoon.author.application.AuthorTransactionService;
import com.webtoon.author.application.AuthorService;
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
    private final AuthorTransactionService authorTransactionService;

    @PostMapping("/author/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid AuthorSignup authorSignup) {
        authorTransactionService.signupSet(authorSignup);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/login")
    public ResponseEntity<AuthorResponse> login(@RequestBody @Valid AuthorLogin authorLogin,
                                                HttpServletRequest httpServletRequest) {
        AuthorResponse authorResponse = authorTransactionService.loginSet(authorLogin, httpServletRequest);
        return ResponseEntity.ok(authorResponse);
    }

    @PostMapping("/author/nickname")
    public ResponseEntity<AuthorListResult> getAuthorListByNickname(@RequestBody CartoonSearchDto cartoonSearchDto) {
        List<AuthorCartoonResponse> authorCartoonResponseList =
                authorTransactionService.findAllByNicknameContains(cartoonSearchDto);
        return ResponseEntity.ok(new AuthorListResult(authorCartoonResponseList.size(), authorCartoonResponseList));
    }

    @PatchMapping("/author")
    public ResponseEntity<AuthorResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                 @RequestBody @Valid AuthorUpdate authorUpdate) {
        AuthorResponse authorResponse =
                authorTransactionService.updateSet(authorSession.getId(), authorUpdate);
        return ResponseEntity.ok(authorResponse);
    }

    @DeleteMapping("/author")
    public ResponseEntity<Void> delete(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorTransactionService.deleteSet(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/author/logout")
    public ResponseEntity<Void> logout(@LoginForAuthor AuthorSession authorSession,
                                       HttpServletRequest httpServletRequest) {
        authorService.invalidateSession(authorSession, httpServletRequest);
        return ResponseEntity.ok().build();
    }
}
