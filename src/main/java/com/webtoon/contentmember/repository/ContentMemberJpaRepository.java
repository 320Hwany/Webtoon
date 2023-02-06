package com.webtoon.contentmember.repository;

import com.webtoon.contentmember.domain.ContentMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentMemberJpaRepository extends JpaRepository<ContentMember, Long> {
}
