package com.webtoon.cartoon.domain;

import com.webtoon.author.domain.Author;
import com.webtoon.cartoon.dto.request.CartoonSave;
import com.webtoon.util.embedded.DayOfTheWeek;
import com.webtoon.util.embedded.Progress;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.EnumType.*;
import static javax.persistence.FetchType.LAZY;
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

    @Enumerated(STRING)
    private DayOfTheWeek dayOfTheWeek;

    @Enumerated(STRING)
    private Progress progress;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "author_id")
    private Author author;

    @Builder
    public Cartoon(String title, DayOfTheWeek dayOfTheWeek, Progress progress, Author author) {
        this.title = title;
        this.dayOfTheWeek = dayOfTheWeek;
        this.progress = progress;
        this.author = author;
    }
}
