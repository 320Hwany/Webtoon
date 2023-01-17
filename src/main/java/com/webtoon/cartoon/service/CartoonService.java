package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.dto.request.CartoonUpdate;
import com.webtoon.cartoon.exception.CartoonForbiddenException;
import com.webtoon.cartoon.repository.CartoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonService {

    private final CartoonRepository cartoonRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    public Cartoon save(CartoonSave cartoonSave, AuthorSession authorSession) {
        Author author = authorRepository.getById(authorSession.getId());
        Cartoon.checkEnumTypeValid(cartoonSave.getDayOfTheWeek(), cartoonSave.getProgress());
        Cartoon cartoon = Cartoon.getFromCartoonSaveAndAuthor(cartoonSave, author);
        return cartoonRepository.save(cartoon);
    }

    @Transactional
    public Cartoon update(CartoonUpdate cartoonUpdate, Long cartoonId, AuthorSession authorSession) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        Cartoon.checkEnumTypeValid(cartoonUpdate.getDayOfTheWeek(), cartoonUpdate.getProgress());
        cartoon.checkAuthorityForCartoon(authorSession);
        cartoon.update(cartoonUpdate);
        return cartoon;
    }
}
