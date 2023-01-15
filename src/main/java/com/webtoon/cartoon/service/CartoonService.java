package com.webtoon.cartoon.service;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.cartoon.repository.CartoonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartoonService {

    private final CartoonRepository cartoonRepository;

    public Cartoon save(CartoonSave cartoonSave, AuthorSession authorSession) {

//        Author author = Author.builder()
//                .id(authorSession.getId())
//                .nickName(authorSession.getNickName())
//                .email(authorSession.getEmail())
//                .password(authorSession.getPassword())
//                .build();

//        Cartoon cartoon = Cartoon.builder()
//                .title(cartoonSave.getTitle())
//                .dayOfTheWeek(cartoonSave.getDayOfTheWeek())
//                .progress(cartoonSave.getProgress())
//                .author(author)
//                .build();
//
//        return cartoonRepository.save(cartoon);
        return null;
    }
}
