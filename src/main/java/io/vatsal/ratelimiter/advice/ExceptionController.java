package io.vatsal.ratelimiter.advice;

import io.vatsal.ratelimiter.exception.ApiLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

  @ExceptionHandler(ApiLimitExceededException.class)
  public ResponseEntity<Object> handleCityNotFoundException(
      ApiLimitExceededException ex, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("message", "API limit has been exceeded");
    return new ResponseEntity<>(body, HttpStatus.TOO_MANY_REQUESTS);
  }
}
