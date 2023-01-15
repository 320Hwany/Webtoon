package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import com.webtoon.author.exception.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public Author save(Author author) {
        return authorJpaRepository.save(author);
    }

    @Override
    public Author getByEmailAndPassword(String email, String password) {
        return authorJpaRepository.findByEmailAndPassword(email, password)
                .orElseThrow(AuthorNotFoundException::new);
    }
}
