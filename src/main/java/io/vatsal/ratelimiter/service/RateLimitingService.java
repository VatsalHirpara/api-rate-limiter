package io.vatsal.ratelimiter.service;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
@Slf4j
public class RateLimitingService {

  @Value("${limit.per.minute}")
  private long LIMIT_PER_MINUTE;

  @Value("${limit.per.hour}")
  private long LIMIT_PER_HOUR;

  RedissonClient client = Redisson.create();

  /** Map <userId, Map< minute in epoch, count>> uuid-123 : { 1688232: 1, 1688260: 3, } */
  RMapCache<String, RMapCache<Long, Long>> rateLimiter;

  public boolean isLimitExceeded(String userId, String methodName) {
    RMapCache<String, RMapCache<Long, Long>> rateLimiter = client.getMapCache(userId);
    if (rateLimiter.containsKey(userId)) {
      final RMapCache<Long, Long> userMap = rateLimiter.get(userId);
      final long currentTime = getStartOfCurrentMinute();
      if (userMap.containsKey(currentTime)) {
        if (userMap.get(currentTime) >= LIMIT_PER_MINUTE) {
          log.error("Limit per minute exceeded");
          return Boolean.TRUE;
        }
      }
      long requestsInLastHour = userMap.values().stream().mapToLong(Long::longValue).sum();
      if (requestsInLastHour >= LIMIT_PER_HOUR) {
        log.error("Hourly limit exceeded");
        return Boolean.TRUE;
      }
      userMap.put(currentTime, userMap.getOrDefault(currentTime, 0L) + 1);
    } else {
      long currentTime = getStartOfCurrentMinute();
      RMapCache<Long, Long> userCount = client.getMapCache("");
      userCount.put(currentTime, 1L);
      rateLimiter.put(userId, userCount);
    }
    return Boolean.FALSE;
  }

  private long getStartOfCurrentMinute() {
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime startOfMinute = currentTime.minusSeconds(currentTime.getSecond());
    return startOfMinute.toEpochSecond(ZoneOffset.UTC);
  }
}
