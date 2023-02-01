package com.webtoon.cartoonmember.repository;


import com.webtoon.cartoonmember.domain.CartoonMember;

public interface CartoonMemberRepository {

    CartoonMember save(CartoonMember cartoonMember);
}
