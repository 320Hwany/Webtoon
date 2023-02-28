package com.webtoon.author.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.exception.AuthorUnauthorizedException;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.CartoonSearch;
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
    public void signup(AuthorSignup authorSignup) {
        Author author = Author.getFromAuthorSignup(authorSignup, passwordEncoder);
        authorRepository.save(author);
    }

    public void checkDuplication(AuthorSignup authorSignup) {
        Optional<Author> findAuthorByNickname = authorRepository.findByNickname(authorSignup.getNickname());
        Optional<Author> findAuthorByEmail = authorRepository.findByEmail(authorSignup.getEmail());
        if (findAuthorByNickname.isPresent() || findAuthorByEmail.isPresent()) {
            throw new AuthorDuplicationException();
        }
    }

    public AuthorSession makeAuthorSession(AuthorLogin authorLogin) {
        Author author = authorRepository.getByEmail(authorLogin.getEmail());
        if (passwordEncoder.matches(authorLogin.getPassword(), author.getPassword())) {
            AuthorSession authorSession = AuthorSession.getFromAuthor(author);
            return authorSession;
        }
        throw new AuthorUnauthorizedException();
    }

    public void makeSessionForAuthorSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.makeSession(request);
    }

    public void invalidateSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.invalidateSession(request);
    }

    public Author getById(Long authorId) {
        return authorRepository.getById(authorId);
    }

    @Transactional
    public void delete(Long authorSessionId) {
        Author author = authorRepository.getById(authorSessionId);
        authorRepository.delete(author);
    }

    public List<AuthorCartoonResponse> findAllByNicknameContains(CartoonSearch cartoonSearch) {
        List<Author> authorList = authorRepository.findAllByNicknameContains(cartoonSearch);
        return authorList.stream()
                .map(AuthorCartoonResponse::getFromAuthor)
                .collect(Collectors.toList());
    }
}
