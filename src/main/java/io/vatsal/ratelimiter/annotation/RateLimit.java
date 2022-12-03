package io.vatsal.ratelimiter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RateLimit {
  long limitPerMinute() default Long.MAX_VALUE;

  long limitPerHour() default Long.MAX_VALUE;
}
