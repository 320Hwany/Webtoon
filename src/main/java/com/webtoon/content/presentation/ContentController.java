package com.webtoon.content.presentation;

import com.webtoon.author.domain.AuthorSession;
import com.webtoon.content.application.ContentService;
import com.webtoon.content.dto.request.*;
import com.webtoon.content.dto.response.ContentListResult;
import com.webtoon.content.dto.response.ContentResponse;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForAuthor;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.webtoon.content.dto.request.ContentSaveSet.*;
import static com.webtoon.content.dto.request.ContentUpdateSet.toContentUpdateSet;
import static com.webtoon.content.dto.request.EpisodeGetSet.toEpisodeGetSet;
import static com.webtoon.global.error.BindingException.validate;


@RequiredArgsConstructor
@RestController
public class ContentController {

    private final ContentService contentService;

    @PostMapping("/content/{cartoonId}")
    public ResponseEntity<Void> save(@LoginForAuthor AuthorSession authorSession,
                                     @PathVariable Long cartoonId,
                                     @RequestBody @Valid ContentSave contentSave) {
        ContentSaveSet contentSaveSet = toContentSaveSet(authorSession, cartoonId, contentSave);
        contentService.save(contentSaveSet);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/content")
    public ResponseEntity<ContentListResult> getContentList(@ModelAttribute @Valid ContentGet contentGet,
                                                            BindingResult bindingResult) {

        validate(bindingResult);
        List<ContentResponse> contentResponseList = contentService.findAllForCartoon(contentGet);
        return ResponseEntity.ok(new ContentListResult(contentResponseList.size(), contentResponseList));
    }

    @GetMapping("/content/episode")
    public ResponseEntity<ContentResponse> getEpisode(@LoginForMember MemberSession memberSession,
                                                      @ModelAttribute @Valid EpisodeGet episodeGet,
                                                      BindingResult bindingResult) {

        validate(bindingResult);
        EpisodeGetSet episodeGetSet = toEpisodeGetSet(memberSession, episodeGet);
        ContentResponse contentResponse = contentService.getEpisode(episodeGetSet);
        return ResponseEntity.ok(contentResponse);
    }

    @PatchMapping("/content/{cartoonId}/{contentEpisode}")
    public ResponseEntity<ContentResponse> update(@LoginForAuthor AuthorSession authorSession,
                                                  @PathVariable Long cartoonId, @PathVariable int contentEpisode,
                                                  @RequestBody @Valid ContentUpdate contentUpdate) {

        ContentUpdateSet contentUpdateSet = toContentUpdateSet(authorSession, cartoonId, contentEpisode, contentUpdate);
        ContentResponse contentResponse = contentService.update(contentUpdateSet);
        return ResponseEntity.ok(contentResponse);
    }
}
