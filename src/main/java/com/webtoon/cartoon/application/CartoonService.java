package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.*;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.exception.EnumTypeValidException;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.util.enumerated.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonService {

    private final CartoonRepository cartoonRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public void save(CartoonSave cartoonSave, AuthorSession authorSession) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonSave(cartoonSave);
        Cartoon.validateEnumTypeValid(cartoonEnumField);
        Author author = authorRepository.getById(authorSession.getId());
        Cartoon cartoon = cartoonSave.toEntity(author);
        cartoonRepository.save(cartoon);
    }

    public List<CartoonResponse> findAllByTitleSet(CartoonSearchTitle cartoonSearchTitle) {
        List<Cartoon> cartoonList = cartoonRepository.findAllByTitle(cartoonSearchTitle);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllByCartoonCondOrderByLikesSet(CartoonSearchDto cartoonSearchDto) {
        CartoonSearchDto cartoonEnumValidField = cartoonSearchDto.toCartoonEnumField();
        cartoonEnumValidField.validateEnumTypeValid();
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonEnumValidField);
        List<Cartoon> cartoonList = cartoonRepository.findAllByCartoonCondOrderByLikes(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllByCartoonCondOrderByRatingSet(CartoonSearchDto cartoonSearchDto) {
        CartoonSearchDto cartoonEnumValidField = cartoonSearchDto.toCartoonEnumField();
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonEnumValidField);
        List<Cartoon> cartoonList = cartoonRepository.findAllByCartoonCondOrderByRating(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public void validateGenreValid(String genre) {
        if (!Genre.validateValid(genre)) {
            throw new EnumTypeValidException(false, false, true);
        }
    }

    @Transactional
    public void update(AuthorSession authorSession, CartoonUpdate cartoonUpdate, Long cartoonId) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonUpdate(cartoonUpdate);
        Cartoon.validateEnumTypeValid(cartoonEnumField);
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        validateAuthorityForCartoon(authorSession, cartoon);
        cartoon.update(cartoonUpdate);
    }

    @Transactional
    public void delete(AuthorSession authorSession, Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        validateAuthorityForCartoon(authorSession, cartoon);
        cartoonRepository.delete(cartoon);
    }

    public void validateAuthorityForCartoon(AuthorSession authorSession, Cartoon cartoon) {
        cartoon.validateAuthorityForCartoon(authorSession);
    }

    @Transactional
    public void addLike(Cartoon cartoon) {
        cartoon.addLike();
    }
}
