package com.florianczeczil.performanceserver;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

/**
 * MetricsCollector
 */
public class MetricsCollector {

  private static ArrayList<Double> cpuUsages = new ArrayList<Double>();
  private static ArrayList<Long> ramUsages = new ArrayList<Long>();
  private static ArrayList<Long> okTimes = new ArrayList<>();
  private static ArrayList<Long> factIterTimes = new ArrayList<>();
  private static ArrayList<Long> factRecTimes = new ArrayList<>();
  private static ArrayList<Long> readFileTimes = new ArrayList<>();
  private static ArrayList<Long> stringPermsTimes = new ArrayList<>();

  public static void queryCpuUsage() {
    double cpuLoad = ManagementFactory
      .getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class)
      .getCpuLoad();
    cpuUsages.add(cpuLoad);
  }

  public static void queryMemoryUsage() {
    MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
    ramUsages.add(memBean.getHeapMemoryUsage().getUsed());
  }

  public static void addTimeToList(String listName, long data) {
    switch (listName) {
      case "ok":
        okTimes.add(data);
        break;
      case "facIter":
        factIterTimes.add(data);
        break;
      case "facRec":
        factRecTimes.add(data);
        break;
      case "readFile":
        readFileTimes.add(data);
        break;
      case "stringPerms":
        stringPermsTimes.add(data);
        break;
      default:
        break;
    }
  }

  public static void writeDataToFiles() {
    new File("./stats").mkdir();
    Path cpuMemPath = Paths.get("./stats/cpu_mem_java.csv");
    Path okPath = Paths.get("./stats/ok_times.csv");
    Path facIterPath = Paths.get("./stats/factorial_iterative_times.csv");
    Path facRecPath = Paths.get("./stats/factorial_recursive_times.csv");
    Path stringPermsPath = Paths.get("./stats/calc_permutations_times.csv");
    Path readFilePath = Paths.get("./stats/read_file_times.csv");

    try {
      Files.write(
        cpuMemPath,
        "CPU Usage (%), Memory Used (bytes);\n".getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      );
      for (int i = 0; i < cpuUsages.size(); i++) {
        Files.write(
          cpuMemPath,
          (
            cpuUsages.get(i).toString() +
            "," +
            ramUsages.get(i).toString() +
            ";\n"
          ).getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
        );
      }

      Files.write(
        okPath,
        "OK Endpoint Times (ns);\n".getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      );
      for (long time : okTimes) {
        Files.write(
          okPath,
          (time + ";\n").getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
        );
      }

      Files.write(
        facIterPath,
        "Factorial Iterative Times (ns);\n".getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      );
      for (long time : factIterTimes) {
        Files.write(
          facIterPath,
          (time + ";\n").getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
        );
      }

      Files.write(
        facRecPath,
        "Factorial Recursive Times (ns);\n".getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      );
      for (long time : factRecTimes) {
        Files.write(
          facRecPath,
          (time + ";\n").getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
        );
      }

      Files.write(
        readFilePath,
        "Read File Times (ns);\n".getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      );
      for (long time : readFileTimes) {
        Files.write(
          readFilePath,
          (time + ";\n").getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
        );
      }

      Files.write(
        stringPermsPath,
        "String Permutation Times (ns);\n".getBytes(),
        StandardOpenOption.CREATE,
        StandardOpenOption.APPEND
      );
      for (long time : stringPermsTimes) {
        Files.write(
          stringPermsPath,
          (time + ";\n").getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND
        );
      }
    } catch (IOException exception) {}
  }

  public static ArrayList<Double> getCpuUsages() {
    return cpuUsages;
  }

  public static ArrayList<Long> getRamUsages() {
    return ramUsages;
  }

  public static ArrayList<Long> getFactIterTimes() {
    return factIterTimes;
  }

  public static ArrayList<Long> getFactRecTimes() {
    return factRecTimes;
  }

  public static ArrayList<Long> getOkTimes() {
    return okTimes;
  }

  public static ArrayList<Long> getReadFileTimes() {
    return readFileTimes;
  }

  public static ArrayList<Long> getStringPermsTimes() {
    return stringPermsTimes;
  }
}
