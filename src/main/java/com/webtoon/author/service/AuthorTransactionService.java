package com.webtoon.author.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorUpdate;
import com.webtoon.author.dto.response.AuthorCartoonResponse;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.CartoonSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthorTransactionService {

    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Author updateTransactionSet(Long authorId, AuthorUpdate authorUpdate) {
        Author author = authorRepository.getById(authorId);
        author.update(authorUpdate, passwordEncoder);
        return author;
    }

    @Transactional
    public void deleteTransactionSet(Long authorId) {
        Author author = authorRepository.getById(authorId);
        authorRepository.delete(author);
    }

    public List<AuthorCartoonResponse> findAllByNicknameContains(CartoonSearch cartoonSearch) {
        List<Author> authorList = authorRepository.findAllByNicknameContains(cartoonSearch);
        return authorList.stream()
                .map(AuthorCartoonResponse::getFromAuthor)
                .collect(Collectors.toList());
    }
}
