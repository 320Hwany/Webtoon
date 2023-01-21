package com.webtoon.author.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public void signup(AuthorSignup authorSignup) {
        Author author = Author.getFromAuthorSignup(authorSignup);
        authorRepository.save(author);
    }

    public List<Author> findAllByNickName(String nickName) {
        return authorRepository.findAllByNickName(nickName);
    }

    @Transactional
    public Author update(AuthorSession authorSession, AuthorUpdate authorUpdate) {
        Author author = authorRepository.getById(authorSession.getId());
        author.update(authorUpdate);
        return author;
    }

    @Transactional
    public void delete(AuthorSession authorSession) {
        Author author = authorRepository.getById(authorSession.getId());
        authorRepository.delete(author);
    }

    public void checkDuplication(AuthorSignup authorSignup) {
        Optional<Author> findAuthorByNickName = authorRepository.findByNickName(authorSignup.getNickName());
        Optional<Author> findAuthorByEmail = authorRepository.findByEmail(authorSignup.getEmail());
        if (findAuthorByNickName.isPresent() || findAuthorByEmail.isPresent()) {
            throw new AuthorDuplicationException();
        }
    }

    public AuthorSession makeAuthorSession(AuthorLogin authorLogin) {
        Author author = authorRepository.getByEmailAndPassword(authorLogin.getEmail(), authorLogin.getPassword());
        AuthorSession authorSession = AuthorSession.getFromAuthor(author);
        return authorSession;
    }

    public void makeSessionForAuthorSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.makeSession(request);
    }

    public void invalidateSession(AuthorSession authorSession, HttpServletRequest request) {
        authorSession.invalidateSession(request);
    }
}
