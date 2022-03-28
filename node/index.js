const express = require("express");
const app = express();
const port = 8080;

app.get("/", (req, res) => {
  res.send("Hello from Node!");
});

app.listen(port, () => {
  console.log(`Server started on http://localhost:${port}`);
});
