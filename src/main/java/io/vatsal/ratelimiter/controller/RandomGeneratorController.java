package io.vatsal.ratelimiter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.vatsal.ratelimiter.annotation.RateLimit;
import io.vatsal.ratelimiter.service.RandomGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/random")
@Api(produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class RandomGeneratorController {

  private final RandomGenerationService randomGenerationService;

  @GetMapping("/integer")
  @ApiOperation(value = "Generate random number")
  @RateLimit(limitPerMinute = 3, limitPerHour = 8)
  public ResponseEntity<Number> generateRandomNumberInRange(@RequestParam String userId) {
    return ResponseEntity.ok(randomGenerationService.generateRandomNumberForRange());
  }
}
