<?php
$start = hrtime(true);
header("Content-Type: text/plain");

$method = $_SERVER["REQUEST_METHOD"];

if ($method !== 'GET') {
	header("HTTP/1.1 404 Not Found");
	return;
}

echo file_get_contents("./lorem-ipsum.txt");

$diff = hrtime(true) - $start;

if(!file_exists("./stats/read_file_times.csv")){
	$fp = fopen('stats/read_file_times.csv', "w");
	fwrite($fp, "Read File Times (ns);\n" . $diff . ";\n");
	fclose($fp);
}else{
	$fp = fopen('stats/read_file_times.csv', "a");
	fwrite($fp, $diff . ";\n");
	fclose($fp);
}
