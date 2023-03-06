package com.webtoon.global.openfeign;

import com.webtoon.content.dto.response.ContentListResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name = "ExchangeRateOpenFeign", url = "http://localhost:8080")
public interface DynamicUrlOpenFeign {

    @GetMapping(value = "/content/{cartoonId}")
    ResponseEntity<ContentListResult> findContentList(@PathVariable Long cartoonId, Pageable pageable);
}
