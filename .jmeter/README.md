# JMeter Load Tests

This directory contains the Apache JMeter test files. To run them, you need to have the tool installed locally. For information how it works, how to install it and how to create/modify tests, check out the [Documentation](https://jmeter.apache.org/usermanual/get-started.html)

## Running the tests
The tests in the file `tests.jmx` can be run in CLI Mode using the following Command:
```sh
jmeter -n -t tests.jmx -l csv/Raspi_Go.csv -e -o html/Raspi_Go -Jserver_port=8081 -Jserver_host=raspberrypi.local
```
Where the value of `-Jserver_port` is the port of the server that should be tested on a specific run and `Jserver_host` is the host that the tests should be run against (the one running the docker-compose file at this projects root)

## Results
Test results are being written into `.jmeter/results`. The existing test files have been run on both the raspberrypi and the windows host and were organized into the respective folders manually.