package io.vatsal.ratelimiter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Counter {
    long timeStamp; //in epoch second
    long count;
}