const express = require('express');
const fs = require('fs');
const util = require('util');
const osUtils = require('os-utils');
const path = require('path');

const app = express();

const port = 8080;
const readFilePromise = util.promisify(fs.readFile);
const writeFilePromise = util.promisify(fs.writeFile);
const mkDirPromise = util.promisify(fs.mkdir);

const cpuUsages = [];
const ramUsages = [];
const okTimes = [];
const facIterTimes = [];
const facRecTimes = [];
const readFileTimes = [];
const stringPermsTimes = [];

app.get('/ok', (req, res) => {
  const start = process.hrtime.bigint();
  logRequest(req.url);
  res.status(200).setHeader('Content-Type', 'text/plain').send();
  const diff = process.hrtime.bigint() - start;
  okTimes.push(diff);
});

app.get('/calc-factorial-iterative', (req, res) => {
  const start = process.hrtime.bigint();
  const number = parseInt(req.query.num);

  if (!number || isNaN(number)) return res.sendStatus(400);

  const calcRes = calcFactorialIterative(number);

  if (calcRes === -1) return res.sendStatus(400);

  logRequest(req.url);
  res
    .status(200)
    .setHeader('Content-Type', 'application/json')
    .send(calcRes.toString());
  const diff = process.hrtime.bigint() - start;
  facIterTimes.push(diff);
});

app.get('/calc-factorial-recursive', (req, res) => {
  const start = process.hrtime.bigint();
  const number = parseInt(req.query.num);

  if (!number || isNaN(number)) return res.sendStatus(400);

  const calcRes = calcFactorialRecursive(number);

  if (calcRes === -1) return res.sendStatus(400);

  logRequest(req.url);
  res
    .status(200)
    .setHeader('Content-Type', 'application/json')
    .send(calcRes.toString());
  const diff = process.hrtime.bigint() - start;
  facRecTimes.push(diff);
});

app.get('/calc-string-permutations', (req, res) => {
  const start = process.hrtime.bigint();
  const input = req.query.string;

  if (!input) return res.sendStatus(400);

  const perms = permutations(input);

  if (!perms) return res.sendStatus(400);

  logRequest(req.url);
  res.status(200).setHeader('Content-Type', 'application/json').send(perms);
  const diff = process.hrtime.bigint() - start;
  stringPermsTimes.push(diff);
});

app.get('/read-file', async (req, res) => {
  const start = process.hrtime.bigint();
  logRequest(req.url);
  try {
    const fileContent = await readFilePromise('./lorem-ipsum.txt');
    res.status(200).setHeader('Content-Type', 'text/plain').send(fileContent);
    const diff = process.hrtime.bigint() - start;
    readFileTimes.push(diff);
  } catch {
    res.status(500).send('Could not find file!');
  }
});

app.get('/', (req, res) => {
  logRequest(req.url);
  res.setHeader('Content-Type', 'text/plain').send('Hello from Node!');
});

app.listen(port, () => {
  console.log(`Server started on http://localhost:${port}`);
});

/**
 *
 * @param {number} num
 * @returns {number} factorial
 */
const calcFactorialIterative = (num) => {
  if (typeof num !== 'number') return -1;
  let factorial = 1;

  for (let i = 1; i <= num; i++) {
    factorial *= i;
  }

  return factorial;
};

/**
 *
 * @param {number} num
 * @returns {number} factorial
 */
const calcFactorialRecursive = (num) => {
  if (typeof num !== 'number') return -1;
  if (num <= 1) return 1;

  return num * calcFactorialRecursive(num - 1);
};

/**
 *
 * @param {string} inputString
 * @returns {string[]} permutations
 */
const permutations = (inputString) => {
  if (typeof inputString !== 'string') return undefined;

  const permutations = [];
  const inputChars = [...inputString];
  const inputLength = inputString.length;

  const helper = (arr, size) => {
    if (size === 1) {
      //push a copy of the array to the permutations
      permutations.push([...arr].join(''));
    }

    for (let i = 0; i < size; i++) {
      helper(arr, size - 1);

      // if size is odd, swap 0th i.e (first) and
      // (size-1)th i.e (last) element
      if (size % 2 == 1) {
        let temp = arr[0];
        arr[0] = arr[size - 1];
        arr[size - 1] = temp;
      }

      // If size is even, swap ith
      // and (size-1)th i.e last element
      else {
        let temp = arr[i];
        arr[i] = arr[size - 1];
        arr[size - 1] = temp;
      }
    }
  };
  helper(inputChars, inputLength);
  return permutations;
};

const logCpuAndRam = () => {
  ramUsages.push(process.memoryUsage().heapUsed);
  osUtils.cpuUsage((usage) => {
    cpuUsages.push(usage);
  });
};

const logRequest = (queryString) => {
  console.log(`[${new Date().toISOString()}] - Request: ${queryString}`);
};

const writeFiles = async () => {
  await mkDirPromise(path.join('./', 'stats'), { recursive: true });

  //write cpu and ram usages to file
  let cpuRamFileString = 'CPU Usage (%), Memory Used (bytes);\n';
  for (let i = 0; i < cpuUsages.length; i++) {
    cpuRamFileString += `${cpuUsages[i]},${ramUsages[i]};\n`;
  }
  await writeFilePromise(
    path.join('./', 'stats', 'cpu_mem_node.csv'),
    cpuRamFileString
  );

  //write all the endpoint files
  let okTimesString = 'OK Endpoint Times (ns);\n';
  okTimes.forEach((time) => (okTimesString += `${time};\n`));
  await writeFilePromise(
    path.join('./', 'stats', 'ok_times.csv'),
    okTimesString
  );

  let facIterString = 'Iterative Factorial Endpoint Times (ns);\n';
  facIterTimes.forEach((time) => (facIterString += `${time};\n`));
  await writeFilePromise(
    path.join('./', 'stats', 'factorial_iterative_times.csv'),
    facIterString
  );

  let facRecString = 'Recursive Factorial Endpoint Times (ns);\n';
  facRecTimes.forEach((time) => (facRecString += `${time};\n`));
  await writeFilePromise(
    path.join('./', 'stats', 'factorial_recursive_times.csv'),
    facRecString
  );

  let readFileString = 'Read File Endpoint Times (ns);\n';
  readFileTimes.forEach((time) => (readFileString += `${time};\n`));
  await writeFilePromise(
    path.join('./', 'stats', 'read_file_times.csv'),
    readFileString
  );

  let stringPermsString = 'String Permutations Endpoint Times (ns);\n';
  stringPermsTimes.forEach((time) => (stringPermsString += `${time};\n`));
  await writeFilePromise(
    path.join('./', 'stats', 'string_permutations_times.csv'),
    stringPermsString
  );
};

const interval = setInterval(logCpuAndRam, 1000);

process.on('SIGINT', async function () {
  console.log('Caught interrupt signal');

  clearInterval(interval);
  await writeFiles();

  process.exit();
});
