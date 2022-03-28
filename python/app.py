from flask import Flask
from flask import Response
app = Flask(__name__)


@app.route("/ok")
def ok():
    return Response("", status=200, content_type="text/plain")


@app.route("/")
def home():
    return Response("Hello from Python/Flask!", content_type="text/plain")


if __name__ == "__main__":
    from waitress import serve
    print("Server now running on http://0.0.0.0:8080")
    serve(app, host="0.0.0.0", port=8080)
