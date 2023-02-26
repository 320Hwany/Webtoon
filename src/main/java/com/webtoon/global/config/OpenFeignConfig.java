package com.webtoon.global.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients("com.webtoon.global.openfeign")
public class OpenFeignConfig {

}