package com.webtoon.content.repository;

import com.webtoon.content.domain.Content;
import org.springframework.stereotype.Repository;

@Repository
public interface ContentRepository {

    Content save(Content content);
}
