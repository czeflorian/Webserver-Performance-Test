<?php
function calcFactorialIterative($num)
{
	$factorial = 1;

	for ($i = 1; $i <= $num; $i++) {
		$factorial = $factorial * $i;
	}

	return $factorial;
}

$start = hrtime(true);

header("Content-Type: application/json");

$method = $_SERVER["REQUEST_METHOD"];

if ($method !== 'GET') {
	header("HTTP/1.1 404 Not Found");
	return;
}

$number = intval($_GET["num"]);

if ($number === 0) {
	header("HTTP/1.1 400 Bad Request");
	return;
}

echo calcFactorialIterative($number);

$diff = hrtime(true) - $start;

if(!file_exists("./stats/factorial_iterative_times.csv")){
	if(!file_exists("./stats")){
		mkdir("./stats", 0777, true);
	}
	$fp = fopen('stats/factorial_iterative_times.csv', "w");
	fwrite($fp, "Factorial Iterative Times (ns);\n" . $diff . ";\n");
	fclose($fp);
}else{
	$fp = fopen('stats/factorial_iterative_times.csv', "a");
	fwrite($fp, $diff . ";\n");
	fclose($fp);
}