package com.webtoon.comment.application;

import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.dto.request.CommentSave;
import com.webtoon.comment.dto.request.CommentUpdate;
import com.webtoon.comment.dto.response.CommentResponse;
import com.webtoon.comment.exception.CommentForbiddenException;
import com.webtoon.comment.repository.CommentRepository;
import com.webtoon.content.domain.Content;
import com.webtoon.content.repository.ContentRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final ContentRepository contentRepository;

    @Transactional
    public CommentResponse save(CommentSave commentSave, Long memberSessionId, Long contentId) {
        Member member = memberRepository.getById(memberSessionId);
        Content content = contentRepository.getById(contentId);
        Comment comment = commentSave.toEntity(member, content);
        commentRepository.save(comment);
        return CommentResponse.getFromEntity(comment);
    }

    @Transactional
    public CommentResponse update(Long commentId, CommentUpdate commentUpdate) {
        Comment comment = commentRepository.getById(commentId);
        comment.update(commentUpdate);
        return CommentResponse.getFromEntity(comment);
    }

    public void validateAuthorization(Long commentId, Long memberSessionId) {
        Comment comment = commentRepository.getById(commentId);
        Member member = memberRepository.getById(memberSessionId);
        if (comment.getMemberId().longValue() != member.getId().longValue()) {
            throw new CommentForbiddenException();
        }
    }

    @Transactional
    public void delete(Long commentId) {
        Comment comment = commentRepository.getById(commentId);
        commentRepository.delete(comment);
    }

    public List<CommentResponse> findAllForMember(Long memberId, Pageable pageable) {
        return commentRepository.findAllForMember(memberId, pageable);
    }
}
