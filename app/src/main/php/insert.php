<?php
 
//header('Content-type=application/json; charset=utf-8');

$response = array();
 
if (isset($_POST['title']) && isset($_POST['subtitle']) && isset($_POST['isbn']) && isset($_POST['description']) 
    && isset($_FILES['image'])) {
 
    $title = $_POST['title'];
    $subTitle = $_POST['subtitle'];
    $isbn = $_POST['isbn'];
    $description = $_POST['description'];

    $randomFilename = createRandomString(32) . '.jpg';
    $destination = './images/' . $randomFilename;

    if (move_uploaded_file($_FILES['image']['tmp_name'], $destination)) {
        require_once 'db_config.php';
        $db = new mysqli(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE);

        if (mysqli_connect_errno()) {
            printf("Connect failed: %s\n", mysqli_connect_error());
            exit();
        }

        $db->query("SET character_set_results=utf8");
        $db->query('SET character set utf8'); // กำหนดเพื่อให้ข้อมูลที่เขียนลงฐานข้อมูลเป็นภาษาไทย

        $insertSql = "INSERT INTO books(title, subtitle, isbn, description, cover_image_filename) VALUES('$title', '$subTitle', '$isbn', '$description', '$randomFilename')";
     
        if ($result = $db->query($insertSql)) {
            $response["success"] = 1;
            $response["message"] = "Data added successfully.";
        } else {
            $response["success"] = 0;
            $response["message"] = "An error occurred while adding data: $insertSql";
        }
        
        $db->close();
    } else {
        $response["success"] = 0;
        $response["message"] = "There's a problem with the uploaded file.";
    }
} else {
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing.";
}

echo json_encode($response);

function createRandomString($length) {
    $key = '';
    $keys = array_merge(range(0, 9), range('a', 'z'));

    for ($i = 0; $i < $length; $i++) {
        $key .= $keys[array_rand($keys)];
    }

    return $key;
}

?>