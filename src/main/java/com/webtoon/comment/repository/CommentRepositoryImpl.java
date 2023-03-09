package com.webtoon.comment.repository;

import com.webtoon.comment.domain.Comment;
import com.webtoon.comment.exception.CommentNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepository {

    private final CommentJpaRepository commentJpaRepository;

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
    public long count() {
        return commentJpaRepository.count();
    }
}
