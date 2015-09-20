<?php

header('Content-type=application/json; charset=utf-8');

require_once 'db_config.php';
$db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

if (mysqli_connect_errno()) {
    printf("Connect failed: %s\n", mysqli_connect_error());
    exit();
}

$charset = "SET character_set_results=utf8";
$db->query($charset);

$response = array();

if ($result = $db->query("SELECT * FROM books ORDER BY _id")) {

    $rowCount = $result->num_rows;
    $response["books"] = array();

    if ($rowCount > 0) {
        while ($row = $result->fetch_assoc()) {
            $book = array();
            $book["_id"] = $row["_id"];
            $book["title"] = $row["title"];
            $book["subtitle"] = $row["subtitle"];
            $book["isbn"] = $row["isbn"];
            $book["description"] = $row["description"];
            $book["cover_image_filename"] = $row["cover_image_filename"];
     
            array_push($response["books"], $book);
        }
        $response["success"] = 1;
    } else {
        $response["success"] = 1;
        $response["message"] = "The database is empty.";
    }

    $result->close();
    
} else {
    $response["success"] = 0;
    $response["message"] = "An error occurred while retrieving data.";
}

$db->close();
echo json_encode($response);

?>