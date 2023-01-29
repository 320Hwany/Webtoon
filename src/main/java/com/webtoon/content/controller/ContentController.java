package com.webtoon.content.controller;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.cartoon.service.CartoonService;
import com.webtoon.content.domain.Content;
import com.webtoon.content.dto.request.ContentSave;
import com.webtoon.content.dto.request.ContentUpdate;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.content.service.ContentService;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForAuthor;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class ContentController {

    private final ContentService contentService;
    private final CartoonService cartoonService;

    @PostMapping("/content/{cartoonId}")
    public ResponseEntity<Void> save(@LoginForAuthor AuthorSession authorSession,
                                     @PathVariable Long cartoonId,
                                     @RequestBody @Valid ContentSave contentSave) {
        cartoonService.validateAuthorityForCartoon(authorSession, cartoonId);
        Content content = contentService.getContentFromContentSaveAndCartoonId(contentSave, cartoonId);
        contentService.save(content);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/content/{cartoonId}/{contentEpisode}")
    public ResponseEntity<ContentResponse> getContent(@PathVariable Long cartoonId,
                                                      @PathVariable Integer contentEpisode) {

        Content content = contentService.findByCartoonIdAndEpisode(cartoonId, contentEpisode);
        ContentResponse contentResponse = ContentResponse.getFromContent(content);
        return ResponseEntity.ok(contentResponse);
    }

    @PatchMapping("/content/{cartoonId}/{contentEpisode}")
    public ResponseEntity<Void> update(@LoginForAuthor AuthorSession authorSession,
                                       @PathVariable Long cartoonId, @PathVariable Integer contentEpisode,
                                       @RequestBody @Valid ContentUpdate contentUpdate) {

        cartoonService.validateAuthorityForCartoon(authorSession, cartoonId);
        Content content = contentService.findByCartoonIdAndEpisode(cartoonId, contentEpisode);
        contentService.update(content, contentUpdate);
        return ResponseEntity.ok().build();
    }
}
