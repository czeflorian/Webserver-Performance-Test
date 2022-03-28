from flask import Flask
app = Flask(__name__)


@app.route("/")
def home():
    return "Hello Flask!"


if __name__ == "__main__":
    from waitress import serve
    print("Server now running on http://0.0.0.0:8080")
    serve(app, host="0.0.0.0", port=8080)
