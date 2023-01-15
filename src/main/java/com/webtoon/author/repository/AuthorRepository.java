package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository {

    Author save(Author author);

    Author getByEmailAndPassword(String email, String password);
}
