package com.webtoon.rest_template;

import com.webtoon.content.dto.request.ContentGet;
import com.webtoon.content.dto.response.ContentListResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;


import static com.webtoon.util.constant.ConstantUrl.getContentImg;
import static com.webtoon.util.constant.ConstantUrl.getContentListUrl;

@RequiredArgsConstructor
@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public ContentListResult getContentList(ContentGet contentGet) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getContentListUrl)
                .queryParam("page", contentGet.getPage())
                .queryParam("size", contentGet.getSize())
                .queryParam("cartoonId", contentGet.getCartoonId());

        String finalUrl = builder.build().toString();
        return restTemplate.getForObject(finalUrl, ContentListResult.class);
    }

    public ResponseEntity<byte[]> getContentImg(Long contentId) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(getContentImg)
                .queryParam("contentId", contentId);

        String finalUrl = builder.build().toString();
        ResponseEntity<byte[]> response = restTemplate.getForEntity(finalUrl, byte[].class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(response.getHeaders().getContentType());
        return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
    }
}
