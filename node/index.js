const express = require("express");
const fs = require("fs");
const util = require("util");

const app = express();

const port = 8080;
const readFilePromise = util.promisify(fs.readFile);

app.get("/ok", (req, res) => {
  logRequest(req.url);
  res.status(200).setHeader("Content-Type", "text/plain").send();
});

app.get("/calc-factorial-iterative", (req, res) => {
  const number = parseInt(req.query.num);

  if (!number || isNaN(number)) return res.sendStatus(400);

  const calcRes = calcFactorialIterative(number);

  if (calcRes === -1) return res.sendStatus(400);

  logRequest(req.url);
  res
    .status(200)
    .setHeader("Content-Type", "application/json")
    .send(calcRes.toString());
});

app.get("/calc-factorial-recursive", (req, res) => {
  const number = parseInt(req.query.num);

  if (!number || isNaN(number)) return res.sendStatus(400);

  const calcRes = calcFactorialRecursive(number);

  if (calcRes === -1) return res.sendStatus(400);

  logRequest(req.url);
  res
    .status(200)
    .setHeader("Content-Type", "application/json")
    .send(calcRes.toString());
});

app.get("/calc-string-permutations", (req, res) => {
  const input = req.query.string;

  if (!input) return res.sendStatus(400);

  const perms = permutations(input);

  if (!perms) return res.sendStatus(400);

  logRequest(req.url);
  res.status(200).setHeader("Content-Type", "application/json").send(perms);
});

app.get("/read-file", async (req, res) => {
  logRequest(req.url);
  try {
    const fileContent = await readFilePromise("./lorem-ipsum.txt");
    res.status(200).setHeader("Content-Type", "text/plain").send(fileContent);
  } catch {
    res.status(500).send("Could not find file!");
  }
});

app.get("/", (req, res) => {
  logRequest(req.url);
  res.setHeader("Content-Type", "text/plain").send("Hello from Node!");
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
  if (typeof num !== "number") return -1;
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
  if (typeof num !== "number") return -1;
  if (num <= 1) return 1;

  return num * calcFactorialRecursive(num - 1);
};

/**
 *
 * @param {string} inputString
 * @returns {string[]} permutations
 */
const permutations = (inputString) => {
  if (typeof inputString !== "string") return undefined;

  const permutations = [];
  const inputChars = [...inputString];
  const inputLength = inputString.length;

  const helper = (arr, size) => {
    if (size === 1) {
      //push a copy of the array to the permutations
      permutations.push([...arr].join(""));
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

const logRequest = (queryString) => {
  console.log(`[${new Date().toISOString()}] - Request: ${queryString}`);
};
