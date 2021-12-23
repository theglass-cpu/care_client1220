<?php

$host = 'localhost';
$user = 'root';
$pw = '985621aA';
$dbName = 'care';
$conn = new mysqli($host, $user, $pw, $dbName);


function errMsg($msg){
    echo "<script>
        window.alert('$msg');
        history.back(1);
    </script>";

 
}

function mq($sql)
{
    global $conn;
    return $conn->query($sql);
}

// 좀외워라 개행 ->\n
//ALTER TABLE write_tb AUTO_INCREMENT=1; index 값
?>

