package com.florianczeczil.performanceserver;

public class FactorialCalculator {

  public static long calcFactorialIterative(long num) {
    long factorial = 1;

    for (long i = 1; i <= num; i++) {
      factorial *= i;
    }

    return factorial;
  }

  public static long calcFactorialRecursive(long num) {
    if (num <= 1) {
      return 1;
    }

    return num * calcFactorialIterative(num - 1);
  }
}
