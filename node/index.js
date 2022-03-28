const express = require("express");
const app = express();
const port = 8080;

app.get("/ok", (req, res) => {
  res.status(200).setHeader("Content-Type", "text/plain").send();
});

app.get("/", (req, res) => {
  res.setHeader("Content-Type", "text/plain").send("Hello from Node!");
});

app.listen(port, () => {
  console.log(`Server started on http://localhost:${port}`);
});
