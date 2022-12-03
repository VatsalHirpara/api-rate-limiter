package io.vatsal.ratelimiter.exception;

public class ApiLimitExceededException extends RuntimeException {
  public ApiLimitExceededException(String msg) {
    super(msg);
  }
}
