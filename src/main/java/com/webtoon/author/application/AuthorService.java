package com.webtoon.author.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.dto.response.AuthorResponse;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.exception.AuthorNotMatchException;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void signupSet(AuthorSignup authorSignup) {
        checkDuplication(authorSignup);
        signup(authorSignup);
    }

    public void checkDuplication(AuthorSignup authorSignup) {
        Optional<Author> findAuthorByNickname = authorRepository.findByNickname(authorSignup.getNickname());
        Optional<Author> findAuthorByEmail = authorRepository.findByEmail(authorSignup.getEmail());
        if (findAuthorByNickname.isPresent() || findAuthorByEmail.isPresent()) {
            throw new AuthorDuplicationException();
        }
    }

    @Transactional
    public void signup(AuthorSignup authorSignup) {
        Author author = Author.getFromAuthorSignup(authorSignup, passwordEncoder);
        authorRepository.save(author);
    }

    @Transactional
    public AuthorResponse loginSet(AuthorLogin authorLogin, HttpServletRequest httpServletRequest) {
        AuthorSession authorSession = makeAuthorSession(authorLogin);
        makeSessionForAuthorSession(authorSession, httpServletRequest);
        return AuthorResponse.getFromAuthorSession(authorSession);
    }

    @Transactional
    public AuthorSession makeAuthorSession(AuthorLogin authorLogin) {
        Author author = authorRepository.getByEmail(authorLogin.getEmail());
        if (passwordEncoder.matches(authorLogin.getPassword(), author.getPassword())) {
            AuthorSession authorSession = AuthorSession.getFromAuthor(author);
            return authorSession;
        }
        throw new AuthorNotMatchException();
    }

    public void makeSessionForAuthorSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.makeSession(request);
    }

    @Transactional
    public AuthorResponse updateSet(Long authorId, AuthorUpdate authorUpdate) {
        Author author = authorRepository.getById(authorId);
        author.update(authorUpdate, passwordEncoder);
        AuthorResponse authorResponse = AuthorResponse.getFromAuthor(author);
        return authorResponse;
    }

    @Transactional
    public void deleteSet(AuthorSession authorSession, HttpServletRequest httpServletRequest) {
        Author author = authorRepository.getById(authorSession.getId());
        authorRepository.delete(author);
        invalidateSession(authorSession, httpServletRequest);
    }

    public void invalidateSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.invalidateSession(request);
    }

    public List<AuthorCartoonResponse> findAllByNicknameContains(CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Author> authorList = authorRepository.findAllByNicknameContains(cartoonSearch);
        return authorList.stream()
                .map(AuthorCartoonResponse::getFromAuthor)
                .collect(Collectors.toList());
    }
}
