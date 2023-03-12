package com.webtoon.cartoon.application;

import com.webtoon.author.domain.Author;
import com.webtoon.author.domain.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.domain.CartoonSearch;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonSearchDto;
import com.webtoon.cartoon.dto.request.CartoonSearchTitle;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
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
    public void saveSet(CartoonSave cartoonSave, AuthorSession authorSession) {
        Author author = authorRepository.getById(authorSession.getId());
        Cartoon cartoon = cartoonSave.toEntity(author);
        cartoonRepository.save(cartoon);
    }

    public List<CartoonResponse> findAllByTitleSet(CartoonSearchTitle cartoonSearchTitle) {
        List<Cartoon> cartoonList = cartoonRepository.findAllByTitle(cartoonSearchTitle);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllByCartoonCondOrderByLikesSet(CartoonSearchDto cartoonSearchDto) {

        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonRepository.findAllByCartoonCondOrderByLikes(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllByCartoonCondOrderByRatingSet(CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonRepository.findAllByCartoonCondOrderByRating(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public void validateAuthorityForCartoon(AuthorSession authorSession, Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.validateAuthorityForCartoon(authorSession);
    }

    public void validateGenreValid(String genre) {
        if (!Genre.validateValid(genre)) {
            throw new EnumTypeValidException(false, false, true);
        }
    }

    @Transactional
    public void updateSet(CartoonUpdate cartoonUpdate, Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.update(cartoonUpdate);
    }

    @Transactional
    public void deleteSet(Long cartoonId) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoonRepository.delete(cartoon);
    }

    @Transactional
    public void addLike(Cartoon cartoon) {
        cartoon.addLike();
    }
}
