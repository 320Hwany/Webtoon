package com.webtoon.contentImgInfo.domain;

import com.webtoon.content.domain.Content;
import com.webtoon.util.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;

import java.io.File;
import java.io.IOException;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
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
    public ContentImgInfo(String imgName, Content content) {
        this.imgName = imgName;
        this.content = content;
    }

    public static void imgUploadOnServer(MultipartFile multipartFile, String imgDir) throws IOException {
        String fullPath = imgDir + multipartFile.getOriginalFilename();
        multipartFile.transferTo(new File(fullPath));
    }

    public static ContentImgInfo makeContentImgInfo(MultipartFile multipartFile, Content content) {
            return ContentImgInfo.builder()
                    .imgName(multipartFile.getOriginalFilename())
                    .content(content)
                    .build();
    }
}
