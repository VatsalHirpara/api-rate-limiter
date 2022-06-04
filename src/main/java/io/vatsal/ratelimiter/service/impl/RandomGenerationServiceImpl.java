package io.vatsal.ratelimiter.service.impl;

import io.vatsal.ratelimiter.service.RandomGenerationService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomGenerationServiceImpl implements RandomGenerationService {

    @Override
    public Integer generateRandomNumberForRange(int start, int end) {
        return new Random().nextInt(end - start) + start;
    }
}
