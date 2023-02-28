package com.webtoon.content.application;

import com.webtoon.cartoon.application.CartoonService;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdateSet;
import com.webtoon.content.exception.ContentNotFoundException;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.TWO_WEEKS;

@RequiredArgsConstructor
@Service
public class ContentTransactionService {

    private final ContentRepository contentRepository;

    private final CartoonService cartoonService;

    private final MemberRepository memberRepository;

    @Transactional
    public void saveSet(Long cartoonId, ContentSave contentSave) {
        Cartoon cartoon = cartoonService.getById(cartoonId);
        Content content = Content.getFromContentSaveAndCartoon(contentSave, cartoon);
        contentRepository.save(content);
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
    public void updateSet(ContentUpdateSet contentUpdateSet) {
        Content content =
                contentRepository.findByCartoonIdAndEpisode(contentUpdateSet.getCartoonId(),
                        contentUpdateSet.getContentEpisode()).orElseThrow(ContentNotFoundException::new);
        content.update(contentUpdateSet.getContentUpdate());
    }
}
