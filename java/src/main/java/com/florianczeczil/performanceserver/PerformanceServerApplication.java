package com.florianczeczil.performanceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PerformanceServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(PerformanceServerApplication.class, args);

    Runtime
      .getRuntime()
      .addShutdownHook(
        new Thread(
          new Runnable() {
            public void run() {
              MetricsCollector.writeDataToFiles();
            }
          }
        )
      );
  }
}
