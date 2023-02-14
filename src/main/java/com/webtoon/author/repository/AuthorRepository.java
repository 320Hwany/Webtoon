package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    void save(Author author);

    Author getById(Long id);
    Optional<Author> findByNickName(String nickName);
    List<Author> findAllByNickNameContains(String nickName);

    Author getByEmail(String email);
    Optional<Author> findByEmail(String email);

    void validateAuthorPresent(AuthorSession authorSession);

    void delete(Author author);

    long count();
}
