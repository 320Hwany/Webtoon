package com.webtoon.content.application;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.exception.CartoonNotFoundException;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.*;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.webtoon.util.constant.ConstantCommon.TWO_WEEKS;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ContentService {

    private final ContentRepository contentRepository;
    private final CartoonRepository cartoonRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public void save(ContentSaveSet contentSaveSet) {
        Cartoon cartoon = cartoonRepository.getById(contentSaveSet.getCartoonId());
        cartoon.validateAuthorityForCartoon(contentSaveSet.getAuthorSession());
        ContentSave contentSave = contentSaveSet.getContentSave();
        Content content = contentSave.toEntity(cartoon);
        contentRepository.save(content);
    }

    public List<ContentResponse> findAllForCartoon(ContentGet contentGet) {
        cartoonRepository.getById(contentGet.getCartoonId());
        List<Content> contentList = contentRepository.findAllForCartoon(contentGet);
        return ContentResponse.getFromContentList(contentList);
    }

    @Transactional
    public ContentResponse getEpisode(EpisodeGetSet episodeGet) {
        Content content =
                contentRepository.findByCartoonIdAndEpisode(episodeGet.getCartoonId(), episodeGet.getContentEpisode())
                        .orElseThrow(ContentNotFoundException::new);
        LocalDate lockLocalDate = content.getLockLocalDate(TWO_WEEKS);
        Member member = memberRepository.getById(episodeGet.getMemberSessionId());
        member.payForPreviewContent(lockLocalDate);
        return ContentResponse.getFromContent(content);
    }

    @Transactional
    public ContentResponse update(ContentUpdateSet contentUpdateSet) {
        Cartoon cartoon = cartoonRepository.getById(contentUpdateSet.getCartoonId());
        cartoon.validateAuthorityForCartoon(contentUpdateSet.getAuthorSession());
        Content content = contentRepository.findByCartoonIdAndEpisode(contentUpdateSet.getCartoonId(),
                        contentUpdateSet.getContentEpisode())
                .orElseThrow(ContentNotFoundException::new);
        content.update(contentUpdateSet.getContentUpdate());
        return ContentResponse.getFromContent(content);
    }
}
