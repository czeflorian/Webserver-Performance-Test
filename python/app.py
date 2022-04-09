import logging
import datetime
import json
from flask import Flask, request
from flask import Response
from numpy import *
app = Flask(__name__)

logger = logging.getLogger('waitress')
logger.setLevel(logging.INFO)


def calc_factorial_iterative(num):
    factorial = 1

    for i in range(num):
        factorial *= (i + 1)

    return factorial


def calc_factorial_recursive(num):
    if num <= 1:
        return 1

    return num * calc_factorial_recursive(num - 1)


def permutations(input_string):
    permutations = []
    input_chars = list(input_string)
    input_length = len(input_chars)

    def helper(arr, size):
        if size == 1:
            arr_copy = arr.copy()
            str_val = ''.join(arr_copy)
            permutations.append(str_val)

        for i in range(size):
            helper(arr, size-1)

            # if size is odd, swap 0th i.e (first)
            # and (size-1)th i.e (last) element
            # else If size is even, swap ith
            # and (size-1)th i.e (last) element
            if size & 1:
                arr[0], arr[size-1] = arr[size-1], arr[0]
            else:
                arr[i], arr[size-1] = arr[size-1], arr[i]

    helper(input_chars, input_length)
    return permutations


@app.route("/calc-factorial-iterative")
def iterative():
    args = request.args
    num_arg = args.get("num")

    try:
        num_arg = int(num_arg)
    except ValueError:
        return Response("Invalid Query Parameter num!", status=400, content_type="text/plain")

    logger.debug(f"num arg is: {num_arg}; with type: {type(num_arg)}")
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")

    result = calc_factorial_iterative(num_arg)
    return Response(str(result), status=200, content_type="application/json")


@app.route("/calc-factorial-recursive")
def recursive():
    args = request.args
    num_arg = args.get("num")

    try:
        num_arg = int(num_arg)
    except ValueError:
        return Response("Invalid Query Parameter num!", status=400, content_type="text/plain")

    logger.debug(f"num arg is: {num_arg}; with type: {type(num_arg)}")
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")

    result = calc_factorial_recursive(num_arg)
    return Response(str(result), status=200, content_type="application/json")


@app.route("/calc-string-permutations")
def string_perms():
    args = request.args
    string_arg = args.get("string")

    logger.debug(f"num arg is: {string_arg}; with type: {type(string_arg)}")
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")

    perms = permutations(string_arg)
    return Response(json.dumps(perms), status=200, content_type="application/json")


@app.route("/read-file")
def read():

    with open('./lorem-ipsum.txt') as f:
        contents = f.read()

        logger.info(
            f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")
        return Response(contents, status=200, content_type="text/plain")


@app.route("/ok")
def ok():
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")
    return Response("", status=200, content_type="text/plain")


@app.route("/")
def home():
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")
    return Response("Hello from Python/Flask!", content_type="text/plain")


if __name__ == "__main__":
    from waitress import serve
    serve(app, host="0.0.0.0", port=8080)
