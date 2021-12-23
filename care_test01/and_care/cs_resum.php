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
        'ml'=>$ml , 'mv'=>$mv , 'ok'=>$ok , 'df'=>$df , 'pp'=>$pp ,'index'=>$index  ));
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

   
   case 'matching_list':
    $id = $_POST["id"];
    $response = array(); 
    $status = 'N';


    $sql = mq("select * from client_request where c_status = '".$status."' ");
    while($list = $sql->fetch_array()){  
        $index = $list["c_index"]; 
        $write_date = $list["c_write_date"]; 
        $date = $list["c_date"]; 
        $deadline = $list["c_deadline"]; 
        $id = $list["c_id"]; 

      array_push($response, array('index'=>$index, 'write_date'=>$write_date , 'date' =>$date , 'deadline' => $deadline , 'id' => $id )); 
    }
             
echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
    break;
  

}

?>