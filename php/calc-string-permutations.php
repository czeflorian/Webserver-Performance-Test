<?php
function generateStringPermutations($inputString)
{
	$permutations = [];
	$inputChars = str_split($inputString);
	$inputLength = count($inputChars);

	function helper(&$arr, $size, &$permutations)
	{
		if ($size === 1) {
			$arrCopy = implode("", $arr);
			array_push($permutations, $arrCopy);
		}

		for ($i = 0; $i < $size; $i++) {
			helper($arr, $size - 1, $permutations);

			// if size is odd, swap 0th i.e (first) and
			// (size-1)th i.e (last) element
			if ($size % 2 == 1) {
				$tmp = $arr[0];
				$arr[0] = $arr[$size - 1];
				$arr[$size - 1] = $tmp;
			}

			// If size is even, swap ith
			// and (size-1)th i.e last element
			else {
				$tmp = $arr[$i];
				$arr[$i] = $arr[$size - 1];
				$arr[$size - 1] = $tmp;
			}
		}
	}
	helper($inputChars, $inputLength, $permutations);
	return $permutations;
}

$start = hrtime(true);

header("Content-Type: application/json");

$method = $_SERVER["REQUEST_METHOD"];

if ($method !== 'GET') {
	header("HTTP/1.1 404 Not Found");
	return;
}

$input = $_GET["string"];

if ($input === "") {
	header("HTTP/1.1 400 Bad Request");
	return;
}

echo json_encode(generateStringPermutations($input)) . "\n";

$diff = hrtime(true) - $start;

if(!file_exists("./stats/string_permutations_times.csv")){
	$fp = fopen('stats/string_permutations_times.csv', "w");
	fwrite($fp, "String Permutation Times (ns);\n" . $diff . ";\n");
	fclose($fp);
}else{
	$fp = fopen('stats/string_permutations_times.csv', "a");
	fwrite($fp, $diff . ";\n");
	fclose($fp);
}
