package com.webtoon.cartoon.domain;

import com.webtoon.cartoon.dto.request.CartoonSave;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@NoArgsConstructor
@Entity
public class Cartoon {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "cartoon_id")
    private Long id;

    private String title;

    public Cartoon(CartoonSave cartoonSave) {
        this.title = cartoonSave.getTitle();
    }
}
