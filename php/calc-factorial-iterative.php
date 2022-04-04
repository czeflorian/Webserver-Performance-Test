<?php
function calcFactorialIterative($num)
{
	$factorial = 1;

	for ($i = 1; $i <= $num; $i++) {
		$factorial = $factorial * $i;
	}

	return $factorial;
}

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
