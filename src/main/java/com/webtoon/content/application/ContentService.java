package com.webtoon.content.application;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.content.repository.ContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContentService {

    private final ContentRepository contentRepository;


    public Content getById(Long id) {
        return contentRepository.getById(id);
    }

    public List<ContentResponse> findAllByCartoonId(Long cartoonId, Pageable pageable) {
        List<Content> contentList = contentRepository.findAllByCartoonId(cartoonId, pageable);
        return ContentResponse.getFromContentList(contentList);
    }
}
