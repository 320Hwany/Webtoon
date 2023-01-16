package com.webtoon.author.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorLogin;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.dto.request.AuthorSignup;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.exception.AuthorDuplicationException;
import com.webtoon.author.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public Author signup(AuthorSignup authorSignup) {
        checkDuplication(authorSignup);
        Author author = authorSignup.toEntity();
        return authorRepository.save(author);
    }

    public AuthorSession makeAuthorSession(AuthorLogin authorLogin) {
        Author author = authorRepository.getByEmailAndPassword(authorLogin.getEmail(), authorLogin.getPassword());
        AuthorSession authorSession = author.getAuthorSession();
        return authorSession;
    }

    @Transactional
    public Author update(AuthorSession authorSession, AuthorUpdate authorUpdate) {
        Author author = authorRepository.getById(authorSession.getId());
        author.update(authorUpdate);
        return author;
    }

    public void checkDuplication(AuthorSignup authorSignup) {
        Optional<Author> findAuthorByNickName = authorRepository.findByNickName(authorSignup.getNickName());
        Optional<Author> findAuthorByEmail = authorRepository.findByEmail(authorSignup.getEmail());
        if (findAuthorByNickName.isPresent() || findAuthorByEmail.isPresent()) {
            throw new AuthorDuplicationException();
        }
    }

    @Transactional
    public void delete(AuthorSession authorSession) {
        authorRepository.deleteById(authorSession.getId());
    }
}