package com.gigster.skymarket.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.net.http.HttpResponse;
@Getter
@Setter
public class ResponseDto {
    private HttpStatus status;
    private String description;
    private Object payload;
}
