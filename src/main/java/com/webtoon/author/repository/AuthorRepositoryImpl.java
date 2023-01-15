package com.webtoon.author.repository;

import com.webtoon.author.domain.Author;
import com.webtoon.author.exception.AuthorNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public Author save(Author author) {
        return authorJpaRepository.save(author);
    }

    @Override
    public Author getById(Long id) {
        return authorJpaRepository.findById(id)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Optional<Author> findByNickName(String nickName) {
        return authorJpaRepository.findByNickName(nickName);
    }

    @Override
    public Optional<Author> findByEmail(String email) {
        return authorJpaRepository.findByEmail(email);
    }

    @Override
    public Author getByEmailAndPassword(String email, String password) {
        return authorJpaRepository.findByEmailAndPassword(email, password)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public void deleteAll() {
        authorJpaRepository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        authorJpaRepository.deleteById(id);
    }
}
