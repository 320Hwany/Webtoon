package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.*;
import com.webtoon.cartoon.dto.response.CartoonResponse;
import com.webtoon.cartoon.repository.CartoonRepository;
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
        validateCartoonSave(cartoonSave);
        Author author = authorRepository.getById(authorSession.getId());
        Cartoon cartoon = cartoonSave.toEntity(author);
        author.getCartoonList().add(cartoon);
        cartoonRepository.save(cartoon);
    }

    protected static void validateCartoonSave(CartoonSave cartoonSave) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonSave(cartoonSave);
        Cartoon.validateEnumTypeValid(cartoonEnumField);
    }

    public List<CartoonResponse> findAllByTitle(CartoonSearchTitle cartoonSearchTitle) {
        List<Cartoon> cartoonList = cartoonRepository.findAllByTitle(cartoonSearchTitle);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllByCartoonCondOrderByLikes(CartoonSearchCond cartoonSearchCond) {
        CartoonSearchCond cartoonEnumValidField = validateSearchCond(cartoonSearchCond);
        CartoonSearch cartoonSearch = cartoonEnumValidField.toCartoonSearch();
        List<Cartoon> cartoonList = cartoonRepository.findAllByCartoonCondOrderByLikes(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllByCartoonCondOrderByRatingSet(CartoonSearchCond cartoonSearchCond) {
        CartoonSearchCond cartoonEnumValidField = validateSearchCond(cartoonSearchCond);
        CartoonSearch cartoonSearch = cartoonEnumValidField.toCartoonSearch();
        List<Cartoon> cartoonList = cartoonRepository.findAllByCartoonCondOrderByRating(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    protected static CartoonSearchCond validateSearchCond(CartoonSearchCond cartoonSearchCond) {
        CartoonSearchCond cartoonEnumValidField = cartoonSearchCond.toCartoonEnumField();
        cartoonEnumValidField.validateEnumTypeValid();
        return cartoonEnumValidField;
    }

    @Transactional
    public CartoonResponse update(CartoonUpdateSet cartoonUpdateSet) {
        CartoonEnumField cartoonEnumField = CartoonEnumField.getFromCartoonUpdate(cartoonUpdateSet.getCartoonUpdate());
        Cartoon.validateEnumTypeValid(cartoonEnumField);
        Cartoon cartoon = cartoonRepository.getById(cartoonUpdateSet.getCartoonId());
        cartoon.validateAuthorityForCartoon(cartoonUpdateSet.getAuthorSession());
        cartoon.update(cartoonUpdateSet.getCartoonUpdate());
        return CartoonResponse.getFromCartoon(cartoon);
    }

    @Transactional
    public void delete(AuthorSession authorSession, Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.validateAuthorityForCartoon(authorSession);
        cartoonRepository.delete(cartoon);
    }
}
