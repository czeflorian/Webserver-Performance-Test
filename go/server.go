package main

import (
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"os"
	"os/signal"
	"strconv"
	"time"
)

var okCpuTimes []int64
var factorialIterativeTimes []int64
var factorialRecursiveTimes []int64
var readFileTimes []int64
var calcPermsTimes []int64

func main() {
	okCpuTimes = make([]int64, 0)
	factorialIterativeTimes = make([]int64, 0)
	factorialRecursiveTimes = make([]int64, 0)
	readFileTimes = make([]int64, 0)
	calcPermsTimes = make([]int64, 0)

	c := make(chan os.Signal, 1)
	signal.Notify(c, os.Interrupt)
	go func() {
		<-c
		writeStatsToFile()
		os.Exit(1)
	}()
	// request handler that returns empty 200 status code --> baseline for measurements
	http.HandleFunc("/ok", func(w http.ResponseWriter, r *http.Request) {
		timeStart := time.Now()
		logToConsole(r.URL.RequestURI())
		w.WriteHeader(http.StatusOK)
		timeEnd := time.Now()

		diff := timeEnd.Sub(timeStart).Nanoseconds()
		okCpuTimes = append(okCpuTimes, diff)
	})

	http.HandleFunc("/calc-factorial-iterative", func(w http.ResponseWriter, r *http.Request) {
		timeStart := time.Now()
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
		timeEnd := time.Now()

		diff := timeEnd.Sub(timeStart).Nanoseconds()
		factorialIterativeTimes = append(factorialIterativeTimes, diff)
	})

	http.HandleFunc("/calc-factorial-recursive", func(w http.ResponseWriter, r *http.Request) {
		timeStart := time.Now()
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

		timeEnd := time.Now()

		diff := timeEnd.Sub(timeStart).Nanoseconds()
		factorialRecursiveTimes = append(factorialRecursiveTimes, diff)
	})

	http.HandleFunc("/read-file", func(w http.ResponseWriter, r *http.Request) {
		timeStart := time.Now()
		w.Header().Add("Content-Type", "text/plain")
		logToConsole(r.URL.RequestURI())

		content, err := ioutil.ReadFile("./lorem-ipsum.txt")

		if err != nil {
			w.WriteHeader(http.StatusInternalServerError)
			return
		}

		w.Write(content)

		timeEnd := time.Now()
		diff := timeEnd.Sub(timeStart).Nanoseconds()
		readFileTimes = append(readFileTimes, diff)
	})

	http.HandleFunc("/calc-string-permutations", func(w http.ResponseWriter, r *http.Request) {
		timeStart := time.Now()
		inputString := r.URL.Query().Get("string")
		w.Header().Add("Content-Type", "application/json")

		if inputString == "" {
			w.WriteHeader(http.StatusBadRequest)
			return
		}

		result := permutations(inputString)

		w.WriteHeader(http.StatusOK)

		logToConsole(r.URL.RequestURI())
		jsonResult, _ := json.Marshal(result)
		w.Write(jsonResult)

		timeEnd := time.Now()
		diff := timeEnd.Sub(timeStart).Nanoseconds()
		calcPermsTimes = append(calcPermsTimes, diff)
	})

	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		w.Header().Add("Content-Type", "text/plain")
		logToConsole(r.URL.RequestURI())
		fmt.Fprintf(w, "Hello from Go!")
	})

	fmt.Println("Server running on http://localhost:8080")
	log.Fatal(http.ListenAndServe(":8080", nil))
}

func permutations(input string) []string {
	var arr = []rune(input)
	var helper func([]rune, int)
	res := []string{}

	helper = func(arr []rune, size int) {
		if size == 1 {
			tmp := make([]rune, len(arr))
			copy(tmp, arr)
			res = append(res, string(tmp))
		} else {
			for i := 0; i < size; i++ {
				helper(arr, size-1)
				if size%2 == 1 {
					tmp := arr[0]
					arr[0] = arr[size-1]
					arr[size-1] = tmp
				} else {
					tmp := arr[i]
					arr[i] = arr[size-1]
					arr[size-1] = tmp
				}
			}
		}
	}
	helper(arr, len(arr))
	return res
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

func writeStatsToFile() {
	os.Mkdir("./stats", 0777)

	okFile, err := os.Create("./stats/ok_times.csv")
	if err != nil {
		fmt.Println(err)
		return
	}
	okFile.WriteString("OK Endpoint Times (ns);\n")
	for _, cpuTime := range okCpuTimes {
		okFile.WriteString(fmt.Sprintf("%d;\n", cpuTime))
	}

	facIterFile, err := os.Create("./stats/factorial_iterative_times.csv")
	if err != nil {
		fmt.Println(err)
		return
	}
	facIterFile.WriteString("Iterative Factorial Endpoint Times (ns);\n")
	for _, cpuTime := range factorialIterativeTimes {
		facIterFile.WriteString(fmt.Sprintf("%d;\n", cpuTime))
	}

	facRecFile, err := os.Create("./stats/factorial_recursive_times.csv")
	if err != nil {
		fmt.Println(err)
		return
	}
	facRecFile.WriteString("Recursive Factorial Endpoint Times (ns);\n")
	for _, cpuTime := range factorialRecursiveTimes {
		facRecFile.WriteString(fmt.Sprintf("%d;\n", cpuTime))
	}

	readFileFile, err := os.Create("./stats/read_file_times.csv")
	if err != nil {
		fmt.Println(err)
		return
	}
	readFileFile.WriteString("Read File Endpoint Times (ns);\n")
	for _, cpuTime := range readFileTimes {
		readFileFile.WriteString(fmt.Sprintf("%d;\n", cpuTime))
	}

	calcPermsFile, err := os.Create("./stats/calc_permutations_times.csv")
	if err != nil {
		fmt.Println(err)
		return
	}
	calcPermsFile.WriteString("String Permutations Endpoint Times (ns);\n")
	for _, cpuTime := range calcPermsTimes {
		calcPermsFile.WriteString(fmt.Sprintf("%d;\n", cpuTime))
	}
}
