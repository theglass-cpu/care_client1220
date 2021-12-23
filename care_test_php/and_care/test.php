<?php 

include 'and_db.php'

?>
<?php 

$image = $_FILES["image"];

 $srcName= $image['name'];
 $tmpName= $image['tmp_name'];
 //서버에 저장된 업로드된 파일의 임시 파일 이름
 $dstName= "document/".date('Ymd_his').$srcName;
//$dstName: 이동할 위치


$upload=move_uploaded_file($tmpName, $dstName);
 //move_uploaded_file =>업로드된 파일을 새 위치로 옮기는 함수
 //$tmpName: 업로드된 파일명
 //$dstName: 이동할 위치


 if($upload){
     echo "이미지 업로드 성공 하였습니다";
 }else{
    echo "이미지 업로드 실패 하였습니다";
 }

?>