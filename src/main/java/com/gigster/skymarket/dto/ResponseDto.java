package com.gigster.skymarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.net.http.HttpResponse;
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto {
    private HttpStatus status;
    private String description;
    private Object payload;
}
