package com.webtoon.util;

import com.webtoon.author.domain.Author;
import com.webtoon.author.dto.request.AuthorSession;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.util.enumerated.DayOfTheWeek;
import com.webtoon.util.enumerated.Progress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class ServiceTest {

    @Autowired
    protected AuthorRepository authorRepository;

    @Autowired
    protected CartoonRepository cartoonRepository;

    protected Author saveAuthorInRepository() {
        Author author = Author.builder()
                .nickName("작가 이름")
                .email("yhwjd99@gmail.com")
                .password("1234")
                .build();

        authorRepository.save(author);
        return author;
    }

    protected Cartoon saveCartoonInRepository(Author author) {
        Cartoon cartoon = Cartoon.builder()
                .title("만화 제목")
                .dayOfTheWeek(DayOfTheWeek.MON)
                .progress(Progress.SERIALIZATION)
                .author(author)
                .build();
        cartoonRepository.save(cartoon);
        return cartoon;
    }

    protected static AuthorSession getAuthorSessionFromAuthor(Author author) {
        AuthorSession authorSession = AuthorSession.builder()
                .id(author.getId())
                .nickName(author.getNickName())
                .email(author.getEmail())
                .password(author.getPassword())
                .build();
        return authorSession;
    }
}
