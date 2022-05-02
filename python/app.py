import logging
import datetime
import json
import os
import signal
import sys
import time
from flask import Flask, request
from flask import Response
from numpy import *
app = Flask(__name__)

logger = logging.getLogger('waitress')
logger.setLevel(logging.INFO)

ok_times = []
fac_iter_times = []
fac_rec_times = []
string_perms_times = []
read_file_times = []


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


def write_to_files():
    path = "./stats"
    exists = os.path.exists(path)

    if not exists:
        os.makedirs(path)

    ok_f = open("./stats/ok_times.csv", "w")
    ok_string = "OK Endpoint Times (ns);\n"
    for time in ok_times:
        ok_string += f"{time};\n"
    ok_f.write(ok_string)
    ok_f.close()

    fac_iter_f = open("./stats/factorial_iterative_times.csv", "w")
    fac_iter_string = "Factorial Iterative Times (ns);\n"
    for time in fac_iter_times:
        fac_iter_string += f"{time};\n"
    fac_iter_f.write(fac_iter_string)
    fac_iter_f.close()

    fac_rec_f = open("./stats/factorial_recursive_times.csv", "w")
    fac_rec_string = "Factorial Recursive Times (ns);\n"
    for time in fac_rec_times:
        fac_rec_string += f"{time};\n"
    fac_rec_f.write(fac_rec_string)
    fac_rec_f.close()

    read_file_f = open("./stats/read_file_times.csv", "w")
    read_file_string = "Read File Times (ns);\n"
    for time in read_file_times:
        read_file_string += f"{time};\n"
    read_file_f.write(read_file_string)
    read_file_f.close()

    string_perms_f = open("./stats/string_permutations_times.csv", "w")
    string_perms_string = "String Permutations Times (ns);\n"
    for time in string_perms_times:
        string_perms_string += f"{time};\n"
    string_perms_f.write(string_perms_string)
    string_perms_f.close()


def signal_handler(sig, frame):
    print("Caught SIGINT, exiting gracefully.")
    write_to_files()
    sys.exit(0)


@app.route("/calc-factorial-iterative")
def iterative():
    start = time.process_time_ns()
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

    diff = time.process_time_ns() - start
    fac_iter_times.append(diff)
    return Response(str(result), status=200, content_type="application/json")


@app.route("/calc-factorial-recursive")
def recursive():
    start = time.process_time_ns()
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

    diff = time.process_time_ns() - start
    fac_rec_times.append(diff)
    return Response(str(result), status=200, content_type="application/json")


@app.route("/calc-string-permutations")
def string_perms():
    start = time.process_time_ns()
    args = request.args
    string_arg = args.get("string")

    logger.debug(f"num arg is: {string_arg}; with type: {type(string_arg)}")
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")

    perms = permutations(string_arg)

    diff = time.process_time_ns() - start
    string_perms_times.append(diff)
    return Response(json.dumps(perms), status=200, content_type="application/json")


@app.route("/read-file")
def read():
    start = time.process_time_ns()
    with open('./lorem-ipsum.txt') as f:
        contents = f.read()

        logger.info(
            f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")

        diff = time.process_time_ns() - start
        read_file_times.append(diff)
        return Response(contents, status=200, content_type="text/plain")


@app.route("/ok")
def ok():
    start = time.process_time_ns()
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")

    diff = time.process_time_ns() - start
    ok_times.append(diff)
    return Response("", status=200, content_type="text/plain")


@app.route("/")
def home():
    logger.info(
        f"[{datetime.datetime.now().isoformat()}] - Request: {request.url}")
    return Response("Hello from Python/Flask!", content_type="text/plain")


signal.signal(signal.SIGINT, signal_handler)

if __name__ == "__main__":
    from waitress import serve
    serve(app, host="0.0.0.0", port=8080)
