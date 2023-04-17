package com.webtoon.contentImgInfo.domain;

import com.webtoon.content.domain.Content;
import com.webtoon.contentImgInfo.exception.GetImgException;
import com.webtoon.contentImgInfo.exception.ImgUploadException;
import com.webtoon.contentImgInfo.exception.MediaTypeException;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class ContentImgInfo extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "content_img_info_id")
    private Long id;

    private String imgName;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @Builder
    protected ContentImgInfo(String imgName, Content content) {
        this.imgName = imgName;
        this.content = content;
    }

    public static ContentImgInfo toContentImgInfo(MultipartFile uploadImg, Content content) {
        return ContentImgInfo.builder()
                .imgName(uploadImg.getOriginalFilename())
                .content(content)
                .build();
    }

    public static void imgUploadOnServer(MultipartFile uploadImg, String imgDir) {
        String fullPath = imgDir + uploadImg.getOriginalFilename();
        try {
            uploadImg.transferTo(new File(fullPath));
        } catch (IOException e) {
            throw new ImgUploadException();
        }
    }

    public UrlResource getImgFromServer(ContentImgInfo contentImgInfo, String imgDir) {
        try {
            return new UrlResource("file:" + imgDir + contentImgInfo.getImgName());
        } catch (MalformedURLException e) {
            throw new GetImgException();
        }
    }

    public MediaType getMediaType(ContentImgInfo contentImgInfo, String imgDir) {
        try {
            return MediaType.parseMediaType(
                    Files.probeContentType(Paths.get("file:" + imgDir + contentImgInfo.getImgName())));
        } catch (IOException e) {
            throw new MediaTypeException();
        }
    }
}
