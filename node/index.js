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

const logRequest = (queryString) => {
  console.log(`[${new Date().toISOString()}] - Request: ${queryString}`);
};
