package com.webtoon.cartoonmember.application;

import com.webtoon.cartoon.domain.Cartoon;
import com.webtoon.cartoon.dto.response.CartoonCore;
import com.webtoon.cartoon.repository.CartoonRepository;
import com.webtoon.cartoonmember.domain.CartoonMember;
import com.webtoon.cartoonmember.dto.request.*;
import com.webtoon.cartoonmember.dto.response.CartoonMemberResponse;
import com.webtoon.cartoonmember.exception.CartoonMemberNotFoundException;
import com.webtoon.cartoonmember.repository.CartoonMemberRepository;
import com.webtoon.member.domain.Member;
import com.webtoon.member.repository.MemberRepository;
import com.webtoon.util.enumerated.Gender;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.webtoon.cartoonmember.domain.CartoonMember.*;
import static com.webtoon.util.constant.ConstantCommon.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartoonMemberService {

    private final CartoonMemberRepository cartoonMemberRepository;
    private final CartoonRepository cartoonRepository;
    private final MemberRepository memberRepository;
    private final CacheManager cacheManager;

    @Transactional
    public void save(CartoonMemberSave cartoonMemberSave) {
        Cartoon cartoon = cartoonRepository.getById(cartoonMemberSave.getCartoonId());
        Member member = memberRepository.getById(cartoonMemberSave.getMemberId());
        CartoonMember cartoonMember = toCartoonMember(cartoon, member);
        readCartoon(cartoonMemberSave, cartoonMember);
    }

    @Transactional
    public void readCartoon(CartoonMemberSave cartoonMemberSave, CartoonMember cartoonMember) {
        if (!validateAlreadyRead(cartoonMemberSave)) {
            CartoonMember psCartoonMember = cartoonMemberRepository.save(cartoonMember);
            psCartoonMember.updateReadDate(LocalDateTime.now());
        } else {
            CartoonMember findCartoonMember = cartoonMemberRepository.getByCartoonMemberSave(cartoonMemberSave);
            findCartoonMember.updateReadDate(LocalDateTime.now());
        }
    }

    protected boolean validateAlreadyRead(CartoonMemberSave cartoonMemberSave) {
        Optional<CartoonMember> cartoonMember = cartoonMemberRepository.findByCartoonMemberSave(cartoonMemberSave);
        return cartoonMember.isPresent();
    }

    @Scheduled(fixedRate = FIVE_MINUTES)
    @CacheEvict(value = CARTOON_LIKES, allEntries = true)
    @Transactional
    public void flushCacheToDB() {
        ConcurrentHashMap<Long, Long> likesMap = (ConcurrentHashMap<Long, Long>) Objects
                .requireNonNull(cacheManager.getCache(CARTOON_LIKES)).getNativeCache();
        for (Map.Entry<Long, Long> entry : likesMap.entrySet()) {
            Long cartoonId = entry.getKey();
            Long likes = entry.getValue();
            Cartoon cartoon = cartoonRepository.getById(cartoonId);
            cartoon.synchronizationLike(likes);
        }
    }

    @Cacheable(value = CARTOON_LIKES, key = "#cartoonId")
    public long getCartoonLikesFromCache(Long cartoonId) {
        ConcurrentHashMap<Long, Long> likesMap = (ConcurrentHashMap<Long, Long>) Objects
                .requireNonNull(cacheManager.getCache(CARTOON_LIKES)).getNativeCache();

        if (likesMap.containsKey(cartoonId)) {
            return likesMap.get(cartoonId);
        }
        return ZERO_OF_TYPE_LONG;
    }

    @Transactional
    public void thumbsUp(CartoonMemberThumbsUp cartoonMemberThumbsUp) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonMemberThumbsUp(cartoonMemberThumbsUp)
                .orElseThrow(CartoonMemberNotFoundException::new);
        cartoonMember.thumbsUp();
        Cartoon cartoon = cartoonMember.getCartoon();
        long likes = getCartoonLikesFromCache(cartoon.getId());
        Objects.requireNonNull(cacheManager.getCache(CARTOON_LIKES)).put(cartoon.getId(), likes + 1);
    }

    @Transactional
    public void rating(CartoonMemberRating cartoonMemberRating) {
        CartoonMember cartoonMember = cartoonMemberRepository.findByCartoonMemberRating(cartoonMemberRating)
                .orElseThrow(CartoonMemberNotFoundException::new);

        if (!cartoonMember.isRated()) {
            long cartoonListSize = cartoonMemberRepository.findCartoonSizeWhereRated(cartoonMemberRating.getCartoonId());
            Cartoon cartoon = cartoonMember.getCartoon();
            cartoon.rating(cartoonMemberRating.getRating(), cartoonListSize);
            cartoonMember.rated();
        }
    }

    public List<CartoonMemberResponse> findAllCartoonByMemberId(Long memberId) {
        return cartoonMemberRepository.findAllCartoonByMemberId(memberId);
    }

    public List<CartoonMemberResponse> findLikeListForMember(Long memberId) {
        return cartoonMemberRepository.findLikeListForMember(memberId);
    }

    public List<CartoonCore> findAllByMemberAge(CartoonSearchAge cartoonSearchAge) {
        return cartoonMemberRepository.findAllByMemberAge(cartoonSearchAge);
    }

    public List<CartoonCore> findAllByMemberGender(CartoonSearchGender cartoonSearchGender) {
        Gender.validateValid(cartoonSearchGender.getGender());
        return cartoonMemberRepository.findAllByMemberGender(cartoonSearchGender);
    }
}
