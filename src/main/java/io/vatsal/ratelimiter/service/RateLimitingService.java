package io.vatsal.ratelimiter.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class RateLimitingService {

  @Value("${limit.per.minute}")
  private long LIMIT_PER_MINUTE;

  @Value("${limit.per.hour}")
  private long LIMIT_PER_HOUR;

  /** Map <userId, Map< minute in epoch, count>> uuid-123 : { 1688232: 1, 1688260: 3, } */
  Map<String, Map<Long, Long>> rateLimiter = new HashMap<>();

  public boolean isLimitExceeded(String userId) {
    if (rateLimiter.containsKey(userId)) {
      final Map<Long, Long> userMap = rateLimiter.get(userId);
      final long currentTime = getStartOfCurrentMinute();
      if (userMap.containsKey(currentTime)) {
        if (userMap.get(currentTime) >= LIMIT_PER_MINUTE) {
          log.error("Limit per minute exceeded");
          return Boolean.TRUE;
        }
        userMap.put(currentTime, userMap.get(currentTime) + 1);
      } else {
        userMap.put(currentTime, 1L);
      }
      long requestsInLastHour = userMap.values().stream().mapToLong(Long::longValue).sum();
      if (requestsInLastHour > LIMIT_PER_HOUR) {
        log.error("Hourly limit exceeded");
        return Boolean.TRUE;
      }
    } else {
      final long currentTime = getStartOfCurrentMinute();
      rateLimiter.put(
          userId,
          new HashMap<>() {
            {
              put(currentTime, 1L);
            }
          });
    }
    return Boolean.FALSE;
  }

  private long getStartOfCurrentMinute() {
    LocalDateTime currentTime = LocalDateTime.now();
    LocalDateTime startOfMinute = currentTime.minusSeconds(currentTime.getSecond());
    return startOfMinute.toEpochSecond(ZoneOffset.UTC);
  }
}
