package com.webtoon.global.resttemplate;

import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.response.ContentListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.webtoon.util.constant.ConstantUrl.getContentListUrl;

@RequiredArgsConstructor
@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public ContentListResult getContentList(ContentGet contentGet) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getContentListUrl);
        builder.queryParam("page", contentGet.getPage())
                .queryParam("size", contentGet.getSize())
                .queryParam("cartoonId", contentGet.getCartoonId());
        String finalUrl = builder.build().toString();
        return restTemplate.getForObject(finalUrl, ContentListResult.class);
    }
}
