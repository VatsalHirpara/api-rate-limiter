package io.vatsal.ratelimiter.aspect;

import io.vatsal.ratelimiter.annotation.RateLimit;
import io.vatsal.ratelimiter.exception.ApiLimitExceededException;
import io.vatsal.ratelimiter.service.RateLimitingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RateLimitAspect {
  private final RateLimitingService rateLimitingService;

  @Before("@annotation(io.vatsal.ratelimiter.annotation.RateLimit) && args(userId,..)")
  public void beforeAPI(JoinPoint call, String userId) {
    MethodSignature signature = (MethodSignature) call.getSignature();
    Method method = signature.getMethod();
    RateLimit rateLimit = method.getAnnotation(RateLimit.class);
    log.info(
        "userId :{} limitPerMinute :{} limitPerHour: {}",
        userId,
        rateLimit.limitPerMinute(),
        rateLimit.limitPerHour());

    if (rateLimitingService.isLimitExceeded(userId, method.getName()))
      throw new ApiLimitExceededException("Limit Exceeded");
  }
}
