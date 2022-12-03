package io.vatsal.ratelimiter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Counter {
  long timeStamp;
  long count;
}
