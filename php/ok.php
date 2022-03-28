<?php

header("Content-Type: text/plain");

$method = $_SERVER["REQUEST_METHOD"];

if ($method !== 'GET') {
	header("HTTP/1.1 404 Not Found");
}
