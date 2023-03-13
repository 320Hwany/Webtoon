package com.webtoon.content.application;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdateSet;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public void saveSet(AuthorSession authorSession, Long cartoonId, ContentSave contentSave) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.validateAuthorityForCartoon(authorSession);
        Content content = contentSave.toEntity(cartoon);
        contentRepository.save(content);
    }

    public List<ContentResponse> findAllByCartoonId(Long cartoonId, Pageable pageable) {
        List<Content> contentList = contentRepository.findAllByCartoonId(cartoonId, pageable);
        return ContentResponse.getFromContentList(contentList);
    }

    @Transactional
    public Content getContentTransactionSet(ContentGet contentGet) {
        Content content =
                contentRepository.findByCartoonIdAndEpisode(contentGet.getCartoonId(), contentGet.getContentEpisode())
                        .orElseThrow(ContentNotFoundException::new);
        LocalDate lockLocalDate = content.getLockLocalDate(TWO_WEEKS);
        Member member = memberRepository.getById(contentGet.getMemberSessionId());
        member.validatePreviewContent(lockLocalDate);
        return content;
    }

    @Transactional
    public void update(AuthorSession authorSession, Long cartoonId, ContentUpdateSet contentUpdateSet) {
        Cartoon cartoon = cartoonRepository.getById(cartoonId);
        cartoon.validateAuthorityForCartoon(authorSession);
        Content content =
                contentRepository.findByCartoonIdAndEpisode(contentUpdateSet.getCartoonId(),
                        contentUpdateSet.getContentEpisode()).orElseThrow(ContentNotFoundException::new);
        content.update(contentUpdateSet.getContentUpdate());
    }
}
