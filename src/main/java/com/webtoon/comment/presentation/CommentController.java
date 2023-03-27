package com.webtoon.comment.presentation;

import com.webtoon.comment.application.CommentService;
import com.webtoon.comment.dto.request.CommentSave;
import com.webtoon.comment.dto.request.CommentSaveSet;
import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.comment.dto.request.CommentUpdateSet;
import com.webtoon.comment.dto.response.CommentContentResp;
import com.webtoon.comment.dto.response.CommentResponse;
import com.webtoon.comment.dto.response.CommentResult;
import com.webtoon.member.domain.MemberSession;
import com.webtoon.util.annotation.LoginForMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.webtoon.comment.dto.request.CommentSaveSet.*;
import static com.webtoon.comment.dto.request.CommentUpdateSet.*;

@RequiredArgsConstructor
@RestController
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/{contentId}")
    public ResponseEntity<Void> save(@LoginForMember MemberSession memberSession,
                                     @PathVariable Long contentId,
                                     @RequestBody CommentSave commentSave) {
        CommentSaveSet commentSaveSet = toCommentSaveSet(commentSave, memberSession.getId(), contentId);
        commentService.save(commentSaveSet);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PatchMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponse> update(@LoginForMember MemberSession memberSession,
                                                  @PathVariable Long commentId,
                                                  @RequestBody CommentUpdate commentUpdate) {

        CommentUpdateSet commentUpdateSet = toCommentUpdateSet(memberSession.getId(), commentId, commentUpdate);
        CommentResponse commentResponse = commentService.update(commentUpdateSet);
        return ResponseEntity.ok(commentResponse);
    }

    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<Void> delete(@LoginForMember MemberSession memberSession,
                                       @PathVariable Long commentId) {
        commentService.delete(commentId, memberSession.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/comment/member")
    public ResponseEntity<CommentResult> findAllForMember(@LoginForMember MemberSession memberSession,
                                                          Pageable pageable) {
        List<CommentResponse> commentResponseList = commentService.findAllForMember(memberSession.getId(), pageable);
        return ResponseEntity.ok(new CommentResult(commentResponseList.size(), commentResponseList));
    }

    @GetMapping("/comment-newest/{contentId}")
    public ResponseEntity<CommentResult> findAllForContentNewest(@PathVariable Long contentId, Pageable pageable) {
        List<CommentContentResp> commentContentRespList = commentService.findAllForContentNewest(contentId, pageable);
        return ResponseEntity.ok(new CommentResult(commentContentRespList.size(), commentContentRespList));
    }

    @GetMapping("/comment-likes/{contentId}")
    public ResponseEntity<CommentResult> findAllForContentLikes(@PathVariable Long contentId, Pageable pageable) {
        List<CommentContentResp> commentContentRespList = commentService.findAllForContentLikes(contentId, pageable);
        return ResponseEntity.ok(new CommentResult(commentContentRespList.size(), commentContentRespList));
    }
}
