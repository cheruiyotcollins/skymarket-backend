package com.gigster.skymarket.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Rating {
   public double  rate;
   public int count;
}