<?php
function calcFactorialRecursive($num)
{
	if ($num <= 1) {
		return 1;
	}

	return $num * calcFactorialRecursive($num - 1);
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

echo calcFactorialRecursive($number);
