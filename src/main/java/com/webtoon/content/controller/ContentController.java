package com.webtoon.content.controller;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.service.CartoonService;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.content.service.ContentService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.member.service.MemberService;
import com.webtoon.util.annotation.LoginForAuthor;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;

import static com.webtoon.util.constant.Constant.TWO_WEEKS;

@RequiredArgsConstructor
@RestController
public class ContentController {

    private final ContentService contentService;
    private final CartoonService cartoonService;
    private final MemberService memberService;

    @PostMapping("/content/{cartoonId}")
    public ResponseEntity<Void> save(@LoginForAuthor AuthorSession authorSession,
                                     @PathVariable Long cartoonId,
                                     @RequestBody @Valid ContentSave contentSave) {
        Cartoon cartoon = cartoonService.getById(cartoonId);
        cartoonService.validateAuthorityForCartoon(authorSession, cartoon);
        Content content = contentService.getContentFromContentSaveAndCartoon(contentSave, cartoon);
        contentService.save(content);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/content/{cartoonId}/{contentEpisode}")
    public ResponseEntity<ContentResponse> getContent(@PathVariable Long cartoonId,
                                                      @PathVariable int contentEpisode) {

        Content content = contentService.findByCartoonIdAndEpisode(cartoonId, contentEpisode);
        ContentResponse contentResponse = ContentResponse.getFromContent(content);
        return ResponseEntity.ok(contentResponse);
    }

    @GetMapping("/content/lock/{cartoonId}/{contentEpisode}")
    public ResponseEntity<ContentResponse> getPreviewContent(@LoginForMember MemberSession memberSession,
                                                             @PathVariable Long cartoonId,
                                                             @PathVariable int contentEpisode) {

        Content content = contentService.findByCartoonIdAndEpisode(cartoonId, contentEpisode);
        LocalDate lockLocalDate = contentService.getLockLocalDate(content, TWO_WEEKS);
        memberService.validatePreviewContent(memberSession, lockLocalDate);
        ContentResponse contentResponse = ContentResponse.getFromContent(content);
        return ResponseEntity.ok(contentResponse);
    }

    @PatchMapping("/content/{cartoonId}/{contentEpisode}")
    public ResponseEntity<Void> update(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId, @PathVariable int contentEpisode,
                                       @RequestBody @Valid ContentUpdate contentUpdate) {

        Cartoon cartoon = cartoonService.getById(cartoonId);
        cartoonService.validateAuthorityForCartoon(authorSession, cartoon);
        Content content = contentService.findByCartoonIdAndEpisode(cartoonId, contentEpisode);
        contentService.update(content, contentUpdate);
        return ResponseEntity.ok().build();
    }
}
