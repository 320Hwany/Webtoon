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
    List<Author> findAllByNickName(String nickName);

    Optional<Author> findByEmail(String email);

    Author getByEmailAndPassword(String email, String password);

    Boolean checkAuthorPresent(AuthorSession authorSession);

    void delete(Author author);

    long count();
}
