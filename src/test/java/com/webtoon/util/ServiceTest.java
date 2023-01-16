package com.webtoon.util;

import com.webtoon.author.domain.Author;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.cartoon.repository.CartoonRepository;
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
}
