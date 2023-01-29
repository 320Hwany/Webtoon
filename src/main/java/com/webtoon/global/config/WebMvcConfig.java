package com.webtoon.global.config;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import com.webtoon.author.repository.AuthorRepository;
import com.webtoon.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthorRepository authorRepository;
    private final MemberRepository memberRepository;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthorArgumentResolver(authorRepository));
        resolvers.add(new MemberArgumentResolver(memberRepository));
    }

    @Bean
    Hibernate5Module hibernate5Module(){
        return new Hibernate5Module();
    }
}
