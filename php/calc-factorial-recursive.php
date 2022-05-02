<?php
function calcFactorialRecursive($num)
{
	if ($num <= 1) {
		return 1;
	}

	return $num * calcFactorialRecursive($num - 1);
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

echo calcFactorialRecursive($number);


$diff = hrtime(true) - $start;

if(!file_exists("./stats/factorial_recursive_times.csv")){
	if(!file_exists("./stats")){
		mkdir("./stats", 0777, true);
	}
	$fp = fopen('stats/factorial_recursive_times.csv', "w");
	fwrite($fp, "Factorial Recursive Times (ns);\n" . $diff . ";\n");
	fclose($fp);
}else{
	$fp = fopen('stats/factorial_recursive_times.csv', "a");
	fwrite($fp, $diff . ";\n");
	fclose($fp);
}