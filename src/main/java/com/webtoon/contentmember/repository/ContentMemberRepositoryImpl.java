package com.webtoon.contentmember.repository;

import com.webtoon.contentmember.domain.ContentMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ContentMemberRepositoryImpl implements ContentMemberRepository {

    private final ContentMemberJpaRepository contentMemberJpaRepository;

    @Override
    public ContentMember save(ContentMember contentMember) {
        return contentMemberJpaRepository.save(contentMember);
    }

    @Override
    public long count() {
        return contentMemberJpaRepository.count();
    }

    @Override
    public void deleteAll() {
        contentMemberJpaRepository.deleteAll();
    }
}
