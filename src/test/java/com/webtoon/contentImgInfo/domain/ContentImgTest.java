package com.webtoon.contentImgInfo.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.util.DomainTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class ContentImgTest extends DomainTest {

    @Test
    @DisplayName("만화 이미지를 서버에 업로드 합니다 - 성공")
    void imgUploadOnServer() throws IOException {
        // given
        String imgDir = "/Users/jeong-youhwan/file/test/";

        MockMultipartFile multipartFile = new MockMultipartFile(
                "imgName",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        // when
        ContentImgInfo.imgUploadOnServer(multipartFile, imgDir);
        UrlResource contentImg = new UrlResource("file:" + imgDir + "hello.txt");

        // then
        assertThat(contentImg.getFilename()).isEqualTo("hello.txt");
        Files.delete(Path.of(imgDir + "hello.txt"));
    }

    @Test
    @DisplayName("만화 이미지에 대한 정보를 생성합니다 - 성공")
    void makeContentImgInfo() {
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