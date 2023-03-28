package com.webtoon.contentImgInfo.application;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.content.domain.Content;
import com.webtoon.contentImgInfo.domain.ContentImgInfo;
import com.webtoon.contentImgInfo.exception.ContentImgInfoNotFoundException;
import com.webtoon.util.ServiceTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

class ContentImgInfoServiceTest extends ServiceTest {

    @Autowired
    private ContentImgInfoService contentImgInfoService;

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
        contentImgInfoService.imgUploadOnServer(multipartFile, imgDir);
        UrlResource contentImg = new UrlResource("file:" + imgDir + "hello.txt");

        // then
        assertThat(contentImg.getFilename()).isEqualTo("hello.txt");
        Files.delete(Path.of(imgDir + "hello.txt"));
    }

    @Test
    @DisplayName("content id로부터 만화 이미지 정보를 가져옵니다 - 성공")
    void getByContentId200() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        saveContentImgInfoInRepository(content);

        // when
        ContentImgInfo contentImgInfo = contentImgInfoService.getByContentId(content.getId());

        // then
        assertThat(contentImgInfo.getContent().getId()).isEqualTo(content.getId());
    }

    @Test
    @DisplayName("content id에 맞는 만화 이미지가 존재하지 않으면 예외가 발생합니다 - 실패")
    void getByContentId404() {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);

        // expected
        Assertions.assertThrows(ContentImgInfoNotFoundException.class,
                () -> contentImgInfoService.getByContentId(content.getId()));
    }

    @Test
    @DisplayName("서버에 있는 만화 이미지를 가져옵니다 - 성공")
    void getImgFromServer() throws IOException {
        // given
        Author author = saveAuthorInRepository();
        Cartoon cartoon = saveCartoonInRepository(author);
        Content content = saveContentInRepository(cartoon);
        ContentImgInfo contentImgInfo = saveContentImgInfoInRepository(content);

        MockMultipartFile multipartFile = new MockMultipartFile(
                "imgName",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        String imgDir = "/Users/jeong-youhwan/file/test/";
        multipartFile.transferTo(new File(imgDir + "hello.txt"));

        // when
        UrlResource contentImg = contentImgInfoService.getImgFromServer(contentImgInfo, imgDir);

        // then
        assertThat(contentImg.getFilename()).isEqualTo("hello.txt");
        Files.delete(Path.of(imgDir + "hello.txt"));
    }

    @Test
    @DisplayName("만화 이미지에 대한 정보를 저장합니다 - 성공")
    void save() {
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
        contentImgInfoService.save(content.getId(), multipartFile);

        // then
        assertThat(contentImgInfoRepository.count()).isEqualTo(1L);
    }
}