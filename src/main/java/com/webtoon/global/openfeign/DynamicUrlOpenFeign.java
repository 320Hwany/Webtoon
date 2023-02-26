package com.webtoon.global.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name = "ExchangeRateOpenFeign", url = "http://localhost:8080/content")
public interface DynamicUrlOpenFeign {

    @GetMapping("/{cartoonId}")
    void findContentList(
            @PathVariable Long cartoonId,
            @RequestParam Pageable pageable);
}
