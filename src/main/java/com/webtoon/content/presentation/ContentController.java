package com.webtoon.content.presentation;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.application.CartoonService;
import com.webtoon.content.application.ContentService;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.content.dto.request.ContentUpdateSet;
import com.webtoon.content.dto.response.ContentListResult;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForAuthor;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RequiredArgsConstructor
@RestController
public class ContentController {

    private final CartoonService cartoonService;
    private final ContentService contentService;

    @PostMapping("/content/{cartoonId}")
    public ResponseEntity<Void> save(@LoginForAuthor AuthorSession authorSession,
                                     @PathVariable Long cartoonId,
                                     @RequestBody @Valid ContentSave contentSave) {
        contentService.saveSet(authorSession, cartoonId, contentSave);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/content/{cartoonId}")
    public ResponseEntity<ContentListResult> getContentList(@PathVariable Long cartoonId, Pageable pageable) {
        List<ContentResponse> contentResponseList = contentService.findAllByCartoonId(cartoonId, pageable);
        return ResponseEntity.ok(new ContentListResult(contentResponseList.size(), contentResponseList));
    }

    @GetMapping("/content/{cartoonId}/{contentEpisode}")
    public ResponseEntity<ContentResponse> getPreviewContent(@LoginForMember MemberSession memberSession,
                                                             @PathVariable Long cartoonId,
                                                             @PathVariable int contentEpisode) {

        ContentGet contentGet = ContentGet.getFromIdAndEpisode(memberSession.getId(), cartoonId, contentEpisode);
        Content content = contentService.getContentTransactionSet(contentGet);
        ContentResponse contentResponse = ContentResponse.getFromContent(content);
        return ResponseEntity.ok(contentResponse);
    }

    @PatchMapping("/content/{cartoonId}/{contentEpisode}")
    public ResponseEntity<Void> update(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId, @PathVariable int contentEpisode,
                                       @RequestBody @Valid ContentUpdate contentUpdate) {

        ContentUpdateSet contentUpdateSet =
                ContentUpdateSet.getFromIdAndEpisode(cartoonId, contentEpisode, contentUpdate);
        contentService.updateSet(authorSession, cartoonId, contentUpdateSet);
        return ResponseEntity.ok().build();
    }
}
