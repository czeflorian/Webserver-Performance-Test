package com.florianczeczil.performanceserver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
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
    long start = System.nanoTime();
    Logger.log(req.getRequestURI());
    long elapsed = System.nanoTime() - start;
    MetricsCollector.addTimeToList("ok", elapsed);
    return ResponseEntity.ok().body("");
  }

  @GetMapping("/calc-factorial-iterative")
  public ResponseEntity<String> calcFactorialIterative(
    HttpServletRequest req,
    @RequestParam("num") long num
  ) {
    long start = System.nanoTime();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");

    long res = FactorialCalculator.calcFactorialIterative(num);

    Logger.log(req.getRequestURI());
    long elapsed = System.nanoTime() - start;
    MetricsCollector.addTimeToList("facIter", elapsed);
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
    long start = System.nanoTime();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");

    long res = FactorialCalculator.calcFactorialRecursive(num);

    Logger.log(req.getRequestURI());
    long elapsed = System.nanoTime() - start;
    MetricsCollector.addTimeToList("facRec", elapsed);
    return ResponseEntity
      .ok()
      .headers(responseHeaders)
      .body(Long.toUnsignedString(res));
  }

  @GetMapping("/calc-string-permutations")
  public ResponseEntity<ArrayList<String>> calcStringPermutations(
    HttpServletRequest req,
    @RequestParam("string") String input
  ) {
    long start = System.nanoTime();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "application/json");

    PermutationsGenerator pg = new PermutationsGenerator(input);
    pg.generatePermutations();
    ArrayList<String> perms = pg.getPermutations();

    Logger.log(req.getRequestURI());
    long elapsed = System.nanoTime() - start;
    MetricsCollector.addTimeToList("stringPerms", elapsed);
    return ResponseEntity.ok().headers(responseHeaders).body(perms);
  }

  @GetMapping("/read-file")
  public ResponseEntity<String> readFile(HttpServletRequest req) {
    long start = System.nanoTime();
    HttpHeaders responseHeaders = new HttpHeaders();
    responseHeaders.set("Content-Type", "text/plain");

    Logger.log(req.getRequestURI());
    try {
      String content = Files.readString(Path.of("lorem-ipsum.txt"));
      long elapsed = System.nanoTime() - start;
      MetricsCollector.addTimeToList("readFile", elapsed);
      return ResponseEntity.ok().headers(responseHeaders).body(content);
    } catch (IOException ex) {
      return ResponseEntity
        .internalServerError()
        .headers(responseHeaders)
        .body("File not found!");
    }
  }
}
