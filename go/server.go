package main

import (
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"strconv"
	"time"
)

func main() {
	// request handler that returns empty 200 status code --> baseline for measurements
	http.HandleFunc("/ok", func(w http.ResponseWriter, r *http.Request) {
		logToConsole(r.URL.RequestURI())
		w.WriteHeader(http.StatusOK)
	})

	http.HandleFunc("/calc-factorial-iterative", func(w http.ResponseWriter, r *http.Request) {
		numberParam := r.URL.Query().Get("num")
		w.Header().Add("Content-Type", "application/json")

		if numberParam == "" {
			w.WriteHeader(http.StatusBadRequest)
			return
		}

		numberToCalc, err := strconv.ParseUint(numberParam, 10, 64)

		if err != nil {
			w.WriteHeader(http.StatusBadRequest)
			return
		}

		result := calcFactorialIterative(numberToCalc)
		w.WriteHeader(http.StatusOK)

		logToConsole(r.URL.RequestURI())
		fmt.Fprintf(w, "%d", result)
	})

	http.HandleFunc("/calc-factorial-recursive", func(w http.ResponseWriter, r *http.Request) {
		numberParam := r.URL.Query().Get("num")
		w.Header().Add("Content-Type", "application/json")

		if numberParam == "" {
			w.WriteHeader(http.StatusBadRequest)
			return
		}

		numberToCalc, err := strconv.ParseUint(numberParam, 10, 64)

		if err != nil {
			w.WriteHeader(http.StatusBadRequest)
			return
		}

		result := calcFactorialRecursive(numberToCalc)
		w.WriteHeader(http.StatusOK)

		logToConsole(r.URL.RequestURI())
		fmt.Fprintf(w, "%d", result)
	})

	http.HandleFunc("/read-file", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Add("Content-Type", "text/plain")
		logToConsole(r.URL.RequestURI())

		content, err := ioutil.ReadFile("./lorem-ipsum.txt")

		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			return
		}

		w.Write(content)
	})

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Add("Content-Type", "text/plain")
		logToConsole(r.URL.RequestURI())
		fmt.Fprintf(w, "Hello from Go!")
	})

	fmt.Println("Server running on http://localhost:8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

func calcFactorialIterative(num uint64) uint64 {
	var factorial uint64
	factorial = 1

	for i := uint64(1); i <= num; i++ {
		factorial *= i
	}

	return factorial
}

func calcFactorialRecursive(num uint64) uint64 {
	if num <= 1 {
		return 1
	}

	return num * calcFactorialRecursive(num-1)
}

func logToConsole(query string) {
	t := time.Now()
	fmt.Fprintf(os.Stdout, "[%s] - Request: \"%s\"\n", t.Format(time.RFC3339), query)
}
