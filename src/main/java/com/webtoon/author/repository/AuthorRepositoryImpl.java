package com.webtoon.author.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.domain.QAuthor;
import com.webtoon.author.exception.AuthorNotFoundException;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.domain.QCartoon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.webtoon.author.domain.QAuthor.author;
import static org.springframework.data.domain.Sort.Direction.DESC;


@RequiredArgsConstructor
@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    private final AuthorJpaRepository authorJpaRepository;

    @Override
    public void save(Author author) {
        authorJpaRepository.save(author);
    }

    @Override
    public Author getById(Long id) {
        return authorJpaRepository.findById(id)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Optional<Author> findByNickname(String nickname) {
        return authorJpaRepository.findByNickname(nickname);
    }

    @Override
    public List<Author> findAllByNicknameContains(CartoonSearch cartoonSearch) {
        PageRequest pageRequest = PageRequest.of(cartoonSearch.getPage(), cartoonSearch.getLimit(),
                Sort.by(DESC, "id"));
        return authorJpaRepository.findAllByNicknameContains(cartoonSearch.getNickname(), pageRequest);
    }

    @Override
    public Author getByEmail(String email) {
        return authorJpaRepository.findByEmail(email)
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public Optional<Author> findByEmail(String email) {
        return authorJpaRepository.findByEmail(email);
    }

    @Override
    public void validateAuthorPresent(AuthorSession authorSession) {
        authorJpaRepository.findById(authorSession.getId())
                .orElseThrow(AuthorNotFoundException::new);
    }

    @Override
    public void delete(Author author) {
        authorJpaRepository.delete(author);
    }

    @Override
    public long count() {
        return authorJpaRepository.count();
    }
}
