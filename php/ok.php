<?php
$start = hrtime(true);

header("Content-Type: text/plain");

$method = $_SERVER["REQUEST_METHOD"];

if ($method !== 'GET') {
	header("HTTP/1.1 404 Not Found");
}
$diff = hrtime(true) - $start;

if(!file_exists("./stats/ok_times.csv")){
	$fp = fopen('stats/ok_times.csv', "w");
	fwrite($fp, "OK Endpoint Times (ns);\n" . $diff . ";\n");
	fclose($fp);
}else{
	$fp = fopen('stats/ok_times.csv', "a");
	fwrite($fp, $diff . ";\n");
	fclose($fp);
}
