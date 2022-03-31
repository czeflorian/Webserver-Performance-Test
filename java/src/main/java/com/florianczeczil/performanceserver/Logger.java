package com.florianczeczil.performanceserver;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

  public static void log(String requestPath) {
    String dateString = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    .format(new Date());
    System.out.printf("[%s] - Request: \"%s\"\n", dateString, requestPath);
  }
}
