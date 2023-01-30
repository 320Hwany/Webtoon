package com.webtoon.contentImgInfo.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.*;

class ContentImgTest extends DomainTest {

    @Test
    @DisplayName("만화 이미지에 대한 정보를 생성합니다 - 성공")
    void makeContentImg() {
        // given
        Author author = getAuthor();
        Cartoon cartoon = getCartoon(author);
        Content content = getContent(cartoon);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        // when
        ContentImgInfo contentImg = ContentImgInfo.makeContentImgInfo(multipartFile, content);

        // then
        assertThat(contentImg.getImgName()).isEqualTo(multipartFile.getOriginalFilename());
        assertThat(contentImg.getContent()).isEqualTo(content);
    }
}