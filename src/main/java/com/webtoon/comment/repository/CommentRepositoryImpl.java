package com.webtoon.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.domain.QComment;
import com.webtoon.comment.dto.response.CommentContentResp;
import com.webtoon.comment.dto.response.CommentResponse;
import com.webtoon.comment.dto.response.QCommentContentResp;
import com.webtoon.comment.dto.response.QCommentResponse;
import com.webtoon.comment.exception.CommentNotFoundException;
import com.webtoon.content.domain.QContent;
import com.webtoon.member.domain.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.webtoon.comment.domain.QComment.comment;
import static com.webtoon.content.domain.QContent.content;
import static com.webtoon.member.domain.QMember.member;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Comment save(Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public Comment getById(Long commentId) {
        return commentJpaRepository.findById(commentId)
                .orElseThrow(CommentNotFoundException::new);
    }

    @Override
    public List<CommentResponse> findAllForMember(Long memberId, Pageable pageable) {
        return jpaQueryFactory.select(new QCommentResponse(
                        comment.id,
                        content.id,
                        comment.commentContent,
                        member.nickname,
                        comment.likes
                ))
                .from(comment)
                .leftJoin(comment.member, member)
                .leftJoin(comment.content, content)
                .where(member.id.eq(memberId))
                .orderBy(comment.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<CommentContentResp> findAllForContent(Long contentId, Pageable pageable) {
        return jpaQueryFactory.select(new QCommentContentResp(
                        comment.id,
                        comment.commentContent,
                        member.nickname,
                        comment.likes,
                        comment.createDateTime
                ))
                .from(comment)
                .leftJoin(comment.member, member)
                .leftJoin(comment.content, content)
                .where(content.id.eq(contentId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.id.desc())
                .fetch();
    }

    @Override
    public long count() {
        return commentJpaRepository.count();
    }

    @Override
    public void delete(Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public void saveAll(List<Comment> commentList) {
        commentJpaRepository.saveAll(commentList);
    }
}
