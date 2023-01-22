package com.webtoon.cartoonimg.domain;

import com.webtoon.content.domain.Content;
import lombok.Getter;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class CartoonImg {


    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cartoon_img_id")
    private Long id;
    private String imgName;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "content_id")
    private Content content;
}
