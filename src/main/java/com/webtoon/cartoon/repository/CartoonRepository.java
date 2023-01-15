package com.webtoon.cartoon.repository;

import com.webtoon.cartoon.domain.Cartoon;
import org.springframework.stereotype.Repository;

@Repository
public interface CartoonRepository {

    Cartoon save(Cartoon cartoon);
}
