package com.webtoon.contentimg.domain;

import com.webtoon.content.domain.Content;
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
public class ContentImg {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cartoon_img_id")
    private Long id;
    private String imgName;
    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "content_id")
    private Content content;

    @Builder
    public ContentImg(String imgName, Content content) {
        this.imgName = imgName;
        this.content = content;
    }

    public static void imgUploadOnServer(MultipartFile multipartFiles[], String imgDir) throws IOException {
        String fullPath = imgDir + multipartFiles[0].getOriginalFilename();
        multipartFiles[0].transferTo(new File(fullPath));
    }

    public static ContentImg makeContentImg(MultipartFile multipartFiles[], Content content) {
            return ContentImg.builder()
                    .imgName(multipartFiles[0].getOriginalFilename())
                    .content(content)
                    .build();
    }

}
