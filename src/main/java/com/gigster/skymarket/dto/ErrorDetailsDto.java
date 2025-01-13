package com.gigster.skymarket.dto;

import java.util.Date;

public record ErrorDetailsDto(Date timestamp, String message, String details) {

}
