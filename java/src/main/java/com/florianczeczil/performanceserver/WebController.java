package com.florianczeczil.performanceserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

  @GetMapping("/")
  public ResponseEntity<String> index(HttpServletRequest req) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "text/plain");

    Logger.log(req.getRequestURI());
    return ResponseEntity
      .ok()
      .headers(responseHeaders)
      .body("Greetings from Spring Boot!");
  }

  // endpoint that returns empty status 200 --> baseline for measurements
  @GetMapping("/ok")
  public ResponseEntity<String> ok(HttpServletRequest req) {
    Logger.log(req.getRequestURI());
    return ResponseEntity.ok().body("");
  }

  @GetMapping("/calc-factorial-iterative")
  public ResponseEntity<String> calcFactorialIterative(
    HttpServletRequest req,
    @RequestParam("num") long num
  ) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");

    long res = FactorialCalculator.calcFactorialIterative(num);

    Logger.log(req.getRequestURI());
    return ResponseEntity
      .ok()
      .headers(responseHeaders)
      .body(Long.toUnsignedString(res));
  }

  @GetMapping("/calc-factorial-recursive")
  public ResponseEntity<String> calcFactorialRecursive(
    HttpServletRequest req,
    @RequestParam("num") long num
  ) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");

    long res = FactorialCalculator.calcFactorialRecursive(num);

    Logger.log(req.getRequestURI());
    return ResponseEntity
      .ok()
      .headers(responseHeaders)
      .body(Long.toUnsignedString(res));
  }

  @GetMapping("/read-file")
  public ResponseEntity<String> readFile(HttpServletRequest req) {
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "text/plain");

    Logger.log(req.getRequestURI());
    try {
      String content = Files.readString(Path.of("lorem-ipsum.txt"));
      return ResponseEntity.ok().headers(responseHeaders).body(content);
    } catch (IOException ex) {
      return ResponseEntity
        .internalServerError()
        .headers(responseHeaders)
        .body("File not found!");
    }
  }
}
