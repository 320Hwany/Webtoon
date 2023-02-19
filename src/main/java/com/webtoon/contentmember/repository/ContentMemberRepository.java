package com.webtoon.contentmember.repository;


import com.webtoon.contentmember.domain.ContentMember;


public interface ContentMemberRepository {

    ContentMember save(ContentMember contentMember);

    long count();

    void deleteAll();
}
