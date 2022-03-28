package com.florianczeczil.performanceserver;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

  @GetMapping("/")
  public ResponseEntity<String> index() {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "text/plain");

    return ResponseEntity
      .ok()
      .headers(responseHeaders)
      .body("Greetings from Spring Boot!");
  }

  // endpoint that returns empty status 200 --> baseline for measurements
  @GetMapping("/ok")
  public ResponseEntity<String> ok() {
    return ResponseEntity.ok().body("");
  }
}
