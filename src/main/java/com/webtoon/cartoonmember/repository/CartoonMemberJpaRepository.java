package com.webtoon.cartoonmember.repository;

import com.webtoon.cartoonmember.domain.CartoonMember;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CartoonMemberJpaRepository extends JpaRepository<CartoonMember, Long> {
}
