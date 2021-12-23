<?php 

include 'and_db.php'

?>

<?php 

switch ($_POST["mode"]){
    
    case "resum_up": 
    $ppyn=$_POST["ppyn"];
    $df=$_POST["df"];
    //이거 첨부서류있는지 없는지 확인    
    
    $id=$_POST["id"];
    $sx=$_POST["sx"];
    $name=$_POST["name"];
    $age=$_POST["age"];
    $lo=$_POST["lo"];
    $level=$_POST["level"];
    $cf=$_POST["cf"];
    $wh=$_POST["wh"];
    $ml=$_POST["ml"];
    $mv=$_POST["mv"];
    $resum = 'Y';
    $response = array();

    if($ppyn=="0"){
        $profile= $_POST["profile"];
        $fet = mq("insert into care_cs_resum 
        ( cs_id , cs_pp, cs_sx, 
        cs_name, cs_age, cs_level, 
        cs_wh , cs_lo, cs_cf ,
        cs_ml ,cs_mv ) 
        values
        ( '$id' ,'$profile' , '$sx' , 
        '$name' , '$age' , '$level' , 
        '$wh' , '$lo' , '$cf',
        '$ml' , '$mv' )");
    

        $index = mysqli_insert_id($conn);
     
    

        $lil = mq("update care_cs set cs_resum = '".$resum."' where cs_id = '".$id."'");
     
     

        $response["success"] = true;
    
    }else{
        $profile= $_FILES["profile"];
        $srcName= $profile['name'];
        $tmpName= $profile['tmp_name'];
        $dstName= "document/".date('Ymd_his').$srcName;
        $result=move_uploaded_file($tmpName, $dstName);
        if($result){
            $save_img = "document/".date('Ymd_his').$srcName;
            $st = mq("insert into care_cs_resum 
            ( cs_id , cs_pp, cs_sx, 
            cs_name, cs_age, cs_level, 
            cs_wh , cs_lo, cs_cf ,
            cs_ml ,cs_mv ) 
            values
            ( '$id' ,'$save_img' , '$sx' , 
            '$name' , '$age' , '$level' , 
            '$wh' , '$lo' , '$cf',
            '$ml' , '$mv' )");
          
            $index = mysqli_insert_id($conn);
            
        
            $lill = mq("update care_cs set cs_resum = '".$resum."' where cs_id = '".$id."'");
          
        }
        $response["success"] = true;
    }

   
    if($df=="1"){
        //첨부서류 갯수
        $df_count=$_POST["df_count"];
     
        $fet = mq("update care_cs_resum set cs_df = '".$df_count."' where cs_id = '".$id."'");
        $int = (int)$df_count;

        for ($i=0; $i <$int ; $i++) { 
     
            $string1 = strval($i);
            $file= $_FILES['df'.$string1];
            $srcName= $file['name'];
            $tmpName= $file['tmp_name'];
            $dstName= "document/".date('Ymd_his').$srcName;
            $res=move_uploaded_file($tmpName, $dstName);
           
            if($res){
                $save_img = "document/".date('Ymd_his').$srcName;
                $img_update = "insert into cs_document ( cs_id , cs_document ,cs_resum_index  )
                values
                ( '$id' ,'$save_img' ,'$index' ) ";
                    

                   $res_ok=mysqli_query($conn,$img_update);
                   if($res_ok){

                    //유저 상태값 업데이트 
                    $li = mq("update care_cs set cs_resum = '".$resum."' where cs_id = '".$id."'");
                        $response["success"] = true;
                   }else{
                      
                    $response["success"] = false;
                   }
            }
        }

    }

 echo json_encode($response);     
        break;
    
case "load_cs_resum":
    $id=$_POST["id"];
    $status = 'N';  
    $response = array();     
    $sql = mq("select * from care_cs_resum where cs_id='".$id."' AND cs_status = '".$status."'");
    while($resum = $sql->fetch_array()){  
        $id=$resum["cs_id"];
        $index=$resum["cs_index"];
        $name=$resum["cs_name"];
        $sx=$resum["cs_sx"];
        $age=$resum["cs_age"];
        $lo=$resum["cs_lo"];
        $cf=$resum["cs_cf"];
        $wh=$resum["cs_wh"];
        $ml=$resum["cs_ml"];
        $mv=$resum["cs_mv"];
        $ok=$resum["cs_ok"];
        $df=$resum["cs_df"];
        $pp=$resum["cs_pp"];
        $level=$resum["cs_level"];

        array_push($response, array('name'=>$name, 'sx'=>$sx , 
        'age'=>$age, 'lo'=>$lo ,'cf'=>$cf ,'wh'=>$wh, 'level'=>$level,
        'ml'=>$ml , 'mv'=>$mv , 'ok'=>$ok , 'df'=>$df , 'pp'=>$pp ,'index'=>$index ,'id'=>$id  ));
    }


    echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
    break;


        
case "ad_resum_list":
    $index=$_POST["index"];
    $status = 'N';  
    $response = array();     
    $sql0 = mq("select * from care_cs_resum where cs_index='".$index."' AND cs_status = '".$status."'");
    while($resum = $sql0->fetch_array()){  
        $index=$resum["cs_index"];
        $name=$resum["cs_name"];
        $id=$resum["cs_id"];
        $sx=$resum["cs_sx"];
        $age=$resum["cs_age"];
        $lo=$resum["cs_lo"];
        $cf=$resum["cs_cf"];
        $wh=$resum["cs_wh"];
        $ml=$resum["cs_ml"];
        $mv=$resum["cs_mv"];
        $ok=$resum["cs_ok"];
        $df=$resum["cs_df"];
        $pp=$resum["cs_pp"];
        $level=$resum["cs_level"];

        array_push($response, array('name'=>$name, 'sx'=>$sx , 
        'age'=>$age, 'lo'=>$lo ,'cf'=>$cf ,'wh'=>$wh, 'level'=>$level,
        'ml'=>$ml , 'mv'=>$mv , 'ok'=>$ok , 'df'=>$df , 'pp'=>$pp ,'index'=>$index ,'id'=>$id  ));
    }


    echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
    break;




 case "load_list":
    $status = 'N';  
    $response = array();     
    $sq0 = mq("select * from care_cs_resum where cs_status = '".$status."'");

    while($rm = $sq0->fetch_array()){  
        $index=$rm["cs_index"];
        $lo=$rm["cs_lo"];
        $ok=$rm["cs_ok"];
        $id=$rm["cs_id"];

        array_push($response, array('index'=>$index, 'receipt'=>$ok , 
        'lo'=>$lo, 'index'=>$index , 'id' =>$id ));
    }

    echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 

    break;

case "resum_df":
    $id=$_POST["id"];
    $status = 'N';  
    $sql2 = mq("select * from cs_document where cs_id='".$id."' AND cs_status ='".$status."'");
    $response = array();   

    while($resum = $sql2->fetch_array()){  
        $image=$resum["cs_document"];
        
        array_push($response,array('image'=>$image));    

    
    }
    echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
    break;

case "delete":
    $index=$_POST["index"];
    $id=$_POST["id"];
    $status = 'Y';
    $cs = 'N';  
    $delete = mq("update care_cs_resum set cs_status = '".$status."' where cs_index = '".$index."'");     
    $delete_image = mq("update cs_document set cs_status = '".$status."' where cs_resum_index = '".$index."'");  
    $delete_rs = mq("update care_cs set cs_resum = '".$cs."' where cs_id = '".$id."'");  
   
    
   break;



   case "ad_resum_st":
    $index=$_POST["index"];
    $ok_status=$_POST["ok_status"];
    $resum_status="N";

    $delete = mq("update care_cs_resum set cs_ok = '".$ok_status."' where cs_index = '".$index."'");     

   
    
   break;

   
   case "request_list":
    $id = $_POST["id"];
    //간병인아이디
    $status = 'N';
    //간병인이 올린 공고 삭제여부상태
    $cs_ok = 'Y';
    //올린공고 통과여부
    $deadline='N';
    //보호자 신청서 삭제여부상태 
    $deadline='N';
    //response 값
    $response = array();


    $sql = mq ("select * from care_cs_resum where cs_id = '".$id."' and cs_ok = '".$cs_ok."' 
    ");

    if($sql->num_rows >0){
        //공고 자료가존재함 
        while($list = $sql ->fetch_array()){
            $cs_lo = trim($list["cs_lo"]);
          
            //지역뽑아옴
            $sqls = mq("select * from client_request where c_status = '".$status."' and c_deadline ='".$deadline."'");   
                //삭제안되고 마감안된거
            while($client_list = $sqls ->fetch_array()){
                $c_adress = trim($client_list["c_adress"]);
                 $c_index = $client_list["c_index"];

                 //지역이랑 index 값 뽑아옴 
                if($cs_lo==$c_adress){
                    //서로 지역맞는값만
                    $sql_accept = mq("select  * from matching_tb where cs_id = '".$id."' and cl_index = '".$c_index."'");
                    if($sql_accept->num_rows > 0){
                     //   echo "존재하는 인덱스 !!".$c_index."\n";
                    }else{
             
                        //   echo "존재하지 않는 !!".$c_index."\n";
             
                    $index = $client_list["c_index"]; 
                    $write_date = $client_list["c_write_date"];
                    $date = $client_list["c_date"];
                   $deadline = $client_list["c_deadline"];
                    $ids = $client_list["c_id"];
                  array_push($response, array('index'=>$index, 'write_date'=>$write_date , 'date' =>$date , 'deadline' => $deadline , 'id' => $ids )); 
            
             
                    }

                 
                }

              }
        }
    }
    echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 


   break;

   case 'matching_refuse':

    $id = $_POST["id"];
    //간병인id
    $user_id = $_POST["user_id"];
    //보호자 id
    $index = $_POST["index"];
    //보호자가 작성한글 
    $match = $_POST["match"];
    

    $resum_list = mq("select * from care_cs_resum where cs_id = '".$id."' ");
    while($r_list = $resum_list ->fetch_array()){
        $name = $r_list["cs_name"]; 

        if($match=="1"){ 
            $status = 'N';
             $fet = mq("insert into matching_tb 
             ( cs_id , cl_id , cl_index , cs_name , cs_accept , request_status) 
             values
             ( '$id' , '$user_id' , '$index' , '$name' , '2' ,'0')");
           
        }else{
             $status = 'Y';
             $fet = mq("insert into matching_tb 
             ( cs_id , cl_id , cl_index , cs_name , cs_accept , request_status) 
             values
             ( '$id' , '$user_id' , '$index' , '$name' , '1' ,'0')");

            }

        }
    
  

    break;


    case 'match_list' :
        $id = $_POST["id"];
        $response = array(); 
        $tb_status = "0";    

        $match_list = mq("select * from chating_room where cs_id = '".$id."' AND tb_status = '".$tb_status."' ");
         while($m_list = $match_list ->fetch_array()){
            $index = $m_list["room_index"]; 
            $cs_id = $m_list["cs_id"];
            $cs_name = $m_list["cs_name"];
            $cs_resum_index = $m_list["cs_resum_index"];
            $cl_id = $m_list["cl_id"];
            $cl_request_index = $m_list["cl_request_index"];
        

            array_push($response, array(
                'index'=>$index, 'cs_id'=>$cs_id , 'cs_name'=>$cs_name , 
                'cs_resum_index'=>$cs_resum_index , 'cl_id'=>$cl_id , 'cl_request_index' =>$cl_request_index)); 
                            
         }   
         echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
        break;
 
        case 'msg_list':
            $room_index = $_POST["room_index"];
            $response = array(); 
            $msg_list = mq("select * from chating_msg where room_index = '".$room_index."' ");

            while($list = $msg_list ->fetch_array()){
                    $user=$list["from_user"];
                    $msg=$list["room_msg"];

                    
                    array_push($response, array(
                        'user'=>$user, 'msg'=>$msg )); 

            }
            echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 

              break; 
}

?>