package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository {

    Author save(Author author);

    Author getById(Long id);
    Optional<Author> findByNickName(String nickName);

    Optional<Author> findByEmail(String email);

    Author getByEmailAndPassword(String email, String password);

    void deleteAll();

    void deleteById(Long id);
}
