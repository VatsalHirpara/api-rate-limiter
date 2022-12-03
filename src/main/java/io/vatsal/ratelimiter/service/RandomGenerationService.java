package io.vatsal.ratelimiter.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGenerationService {
  public Integer generateRandomNumberForRange() {
    return new Random().nextInt(100);
  }
}
