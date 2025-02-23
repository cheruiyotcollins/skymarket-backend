package com.gigster.skymarket.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ReviewConfig {

    @Value("${review.comment.limit}")
    private int commentLimit;

}
