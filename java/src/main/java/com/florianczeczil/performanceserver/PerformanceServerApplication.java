package com.florianczeczil.performanceserver;

import java.util.Timer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PerformanceServerApplication {

  public static void main(String[] args) {
    SpringApplication.run(PerformanceServerApplication.class, args);

    Timer timer = new Timer();
    timer.schedule(new MetricsCollectorTask(), 0, 1000);

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
