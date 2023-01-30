package com.webtoon.contentImgInfo.service;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@Transactional
class ContentImgInfoServiceTest extends ServiceTest {

    @Autowired
    private ContentImgInfoService contentImgInfoService;

    @Test
    @DisplayName("만화 이미지에 대한 정보를 저장합니다 - 성공")
    void saveContentImg() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        // when
        contentImgInfoService.saveContentImgInfo(multipartFile, content);

        // then
        assertThat(contentImgInfoRepository.count()).isEqualTo(1L);
    }
}