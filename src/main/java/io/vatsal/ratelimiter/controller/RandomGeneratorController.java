package io.vatsal.ratelimiter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.vatsal.ratelimiter.exception.ApiLimitExceededException;
import io.vatsal.ratelimiter.service.RandomGenerationService;
import io.vatsal.ratelimiter.service.impl.RateLimitingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/random")
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RandomGeneratorController {

    private final RandomGenerationService randomGenerationService;
    private final RateLimitingServiceImpl rateLimitingService;

    @GetMapping("/integer")
    @ApiOperation(value = "Generate random number for range")
    public ResponseEntity<Number> generateRandomNumberInRange(@ApiParam(value = "start of range(inclusive)",defaultValue = "10") @RequestParam int start,
                                                              @ApiParam(value = "end of range(exclusive)", defaultValue = "15") @RequestParam int end,
                                                              @RequestParam String userId) throws InterruptedException {
        if(rateLimitingService.isLimitExceeded(userId)) throw new ApiLimitExceededException("Limit Exceeded");
        return ResponseEntity.ok(randomGenerationService.generateRandomNumberForRange(start,end));
    }
}