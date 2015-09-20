<?php

echo 'Hello ' . htmlspecialchars($_POST["title3"]) . '!' . "\n";

if ($_FILES["image"]["error"] > 0) {
	echo "Error: " . $_FILES["image"]["error"];
} else {
	$fileName = $_FILES["image"]["name"];
	$fileType = $_FILES["image"]["type"];
	$fileSize = $_FILES["image"]["size"];

	echo "File name: $fileName\n";
	echo "File type: $fileType\n";
	echo "File size: $fileSize\n";

	print_r($_FILES);

	//echo $_POST["imagefile"];
}

?>