package com.webtoon.cartoon.application;

import com.webtoon.author.application.AuthorService;
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
public class CartoonTransactionService {

    private final AuthorService authorService;
    private final CartoonService cartoonService;

    @Transactional
    public void saveSet(CartoonSave cartoonSave, AuthorSession authorSession) {
        Author author = authorService.getById(authorSession.getId());
        Cartoon cartoon = Cartoon.getFromCartoonSaveAndAuthor(cartoonSave, author);
        cartoonService.save(cartoon);
    }

    public List<CartoonResponse> findAllByTitleSet(CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByTitle(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllByCartoonCondSet(CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllByCartoonCond(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    public List<CartoonResponse> findAllOrderByLikes(CartoonSearchDto cartoonSearchDto) {
        CartoonSearch cartoonSearch = CartoonSearch.getByCartoonSearchDto(cartoonSearchDto);
        List<Cartoon> cartoonList = cartoonService.findAllOrderByLikes(cartoonSearch);
        return CartoonResponse.getFromCartoonList(cartoonList);
    }

    @Transactional
    public void updateSet(CartoonUpdate cartoonUpdate, Long cartoonId) {
        Cartoon cartoon = cartoonService.getById(cartoonId);
        cartoon.update(cartoonUpdate);
    }

    @Transactional
    public void deleteSet(Long cartoonId) {
        Cartoon cartoon = cartoonService.getById(cartoonId);
        cartoonService.delete(cartoon);
    }
}
