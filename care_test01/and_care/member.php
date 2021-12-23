<?php 

include 'and_db.php'

?>

<?php 

 switch ($_POST["mode"]) {
     case 'check_id':
        $response=array();
        $id=preg_replace("/\s+/","",$_POST["id"]);  
       //id 값들어오는거확인
       $sql = mq("select * from care_client WHERE c_id = '$id'"); // 게시판 총 레코드 수 
       $checkMail = filter_Var($id, FILTER_VALIDATE_EMAIL);       

       if ($checkMail == true) {
        if(mysqli_num_rows($sql)>=1){
               
            $response['success'] = false;
        }else{
            $response['success'] = true;
        
        }

        } else {
            $response['success'] = false;
        }
     
    
             echo json_encode($response);

       break;


       case 'register':
        $response=array();
        $id=preg_replace("/\s+/","",$_POST["id"]);  
        $pw=$_POST["pw"];
        $encrypted_passwd  = password_hash($pw, PASSWORD_DEFAULT); 
        $number=$_POST["number"];
       

        $sql2= "insert into care_client ( c_id  ,c_pw ,c_tell) 
        values
        ( '$id' ,'$encrypted_passwd','$number' ) "; 
        $result_set=mysqli_query($conn,$sql2);
        if($result_set){
            $response['success'] = true;
        }else{
            $response['success'] = false;
        }

  

        echo json_encode($response);
        break;

        case 'login':

            $id = $_POST["id"];
            $pw = $_POST["pw"];
         
            $select_query = "SELECT  * FROM care_client WHERE c_id = '$id' ";
            $result_set = mysqli_query($conn, $select_query);       
            $response = array();
            while($row=mysqli_fetch_array($result_set)){
                 $mb_pw=$row["c_pw"];
                 $match = password_verify($pw,$mb_pw);
               
                 if($match){
                    $response["success"] = true;
                }else{
                    $response["success"] = false;
                }
                break;
            }
            
             if($row == null){
                 $response["success"] = false;
                
             }
    
    
            echo json_encode($response);
    
            break;
    
            
         case 'find_id':   

            $number = $_POST["number"];
            $select_query = "SELECT  * FROM care_client WHERE c_tell = '$number' ";
            $result_set = mysqli_query($conn, $select_query);     
      
            $response = array();     
          
    
            while($row=mysqli_fetch_array($result_set)){
                $id=$row["c_id"];
                array_push($response, array('id'=>$id ));
                break;
            }
            if($row==null){
                array_push($response, array('id'=>" 존재하지 않음" ));
          
            }
            echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE);
            
             break;


             case 'new_pw':

                $id = $_POST["id"];
                $pw = $_POST["pw"];
                $pw2 = $_POST["pw2"];
                $encrypted_passwd  = password_hash($pw2, PASSWORD_DEFAULT);
                $mb_status = 'N';
                $response = array();

                $select_query = "SELECT  * FROM care_client WHERE c_id = '$id' ";
                $result_set = mysqli_query($conn, $select_query);       
                $response = array();
                while($row=mysqli_fetch_array($result_set)){
                     $mb_pw=$row["c_pw"];
                     $match = password_verify($pw,$mb_pw);
                   
                     if($match){

                      $fet = mq("update care_client set c_pw = '".$encrypted_passwd."' where c_id = '".$id."'");
                      $response["success"] = true;
                    }else{
                        $response["success"] = false;
                    }
                    break;
                }
                
                 if($row == null){
                     $response["success"] = false;
                    
                 }
        
        
                echo json_encode($response);
        


                break;


                 case 'like_center':
                 
                    $id = $_POST["id"];
                    $ce_name = $_POST["ce_name"];
                    $ce_address = $_POST["ce_address"];
                    $ce_tel = $_POST["ce_tel"];
                    $x = $_POST["x"];
                    $y = $_POST["y"];
                    $response = array();
                    if("0"==$_POST["mStatus"]){
                        $sd = mq(" DELETE FROM c_like_center WHERE c_id = '".$id."' AND c_x = '".$x."' AND c_y = '".$y."' ");
                        if($sd){
                            echo "즐찾해지성공";    
                        }
                      
                        
                    }else{


                    $sl = mq("insert into c_like_center ( c_id , ce_name , ce_address , ce_tel , c_x , c_y ) 
                    values ( '$id' , '$ce_name' , '$ce_address' , '$ce_tel' , '$x' , '$y' )"); // 게시판 총 레코드 수 
                     
                    if($sl){
                        $response["success"] = true;
                      }  else{
                        $response["success"] = false;
                      }
                     
                       //즐찾누름
                    }    

                    echo json_encode($response);

                    break;



                   case 'like_list' :
                    
                    $id = $_POST["id"];
                    $x = $_POST["x"];
                    $y = $_POST["y"];
                    $response = array();     
                     $like_load = mq("select * from c_like_center where c_id ='".$id."' AND c_x = '".$x."' AND c_y = '".$y."' ");
                       while($like = $like_load ->fetch_array()){
                        $response["success"] = true;
                           break; 
                       }
                       if($like==null){
                        $response["success"] = false;
                       }

                    
                    echo json_encode($response);
                    break;

                    case 'mypage_like_list' :
                        $response = array();   
                        $id = $_POST["id"];

                        $like_list =mq("select DISTINCT ce_name , ce_address , ce_tel , c_x ,c_y from c_like_center where c_id ='".$id."'");
                        while($like = $like_list ->fetch_array()){
                              $name = $like["ce_name"];  
                              $address = $like["ce_address"];  
                              $tel = $like["ce_tel"]; 
                              $x = $like["c_x"];  
                              $y = $like["c_y"];  
                             
                              array_push($response, array('name'=>$name, 'address'=>$address , 
                              'tel'=>$tel ,'x'=>$x , 'y' => $y)); 
                            
                        }
                        
                echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
                      break;


                      case 'client_request':
                        $id = $_POST["id"];
                        $type = $_POST["type"];
                        $날짜 = $_POST["날짜"];
                        $place = $_POST["place"];
                        $timeout = $_POST["timeout"];
                        
                        $timestart = $_POST["timestart"];
                        $care_level = $_POST["care_level"];   
                        $sex = $_POST["sex"];   
                        $age = $_POST["age"];   
                        $adress = $_POST["adress"];

                        $wash = $_POST["wash"];   
                        $meal = $_POST["meal"];   
                        $move = $_POST["move"];  
                        $remark = $_POST["remark"];  
                        $write_date = $_POST["write_date"];  
                        $response = array();

                        $sl = mq("insert into client_request 
                        ( c_id , c_type , c_date ,c_place ,c_timeout ,
                        c_timestart , c_level , c_sex , c_age ,
                        c_adress , c_wash , c_meal , c_move ,c_remark ,
                        c_write_date
                    
                        ) 
                        values
                         ( '$id' , '$type' , '$날짜' ,'$place' , '$timeout' ,
                         '$timestart' ,'$care_level' , '$sex' , '$age' ,
                         '$adress' , '$wash' , '$meal' , '$move' ,'$remark' ,
                         '$write_date'
                        
                         
                       )"); // 게시판 총 레코드 수 
                         
                         
                        if($sl){
                            $response["success"] = true;
                          }  else{
                            $response["success"] = false;
                          }


                        echo json_encode($response);


                        break;


                        case 'request_list':
                            $id = $_POST["id"];
                            $response = array(); 
                            $status = 'N';
           

                            $sql = mq("select * from client_request where c_id='".$id."' AND c_status = '".$status."' ");
                            while($list = $sql->fetch_array()){  
                                $index = $list["c_index"]; 
                                $write_date = $list["c_write_date"]; 
                                $date = $list["c_date"]; 
                                $deadline = $list["c_deadline"]; 

                              array_push($response, array('index'=>$index, 'write_date'=>$write_date , 'date' =>$date , 'deadline' => $deadline )); 
                            }
                                     
                      echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
                            break;


                            case 'request_detail':
                                $id = $_POST["id"];
                                $index = $_POST["index"];
                                $status = 'N';
                                $response = array(); 
                               
               
    
                                $sql = mq("select * from client_request where c_id='".$id."' AND c_status = '".$status."' AND c_index = '".$index."' ");
                                while($list = $sql->fetch_array()){  
                                    $type = $list["c_type"]; 
                                    $date = $list["c_date"];
                                    $place = $list["c_place"];
                                    $timeout = $list["c_timeout"];
                                    $timestart = $list["c_timestart"];
                                    $level = $list["c_level"];
                                    $sex = $list["c_sex"];
                                    $age = $list["c_age"];
                                    $adress = $list["c_adress"];
                                    $wash = $list["c_wash"];
                                    $meal = $list["c_meal"];
                                    $move = $list["c_move"];
                                    $remark = $list["c_remark"];
                                    $write_date = $list["c_write_date"];
                                    



                                  array_push($response, 
                                  array('type'=>$type, 
                                  'date'=>$date , 
                                  'place' =>$place , 
                                  'timeout' => $timeout ,
                                  'timestart' => $timestart,
                                  'level' => $level,
                                  'sex' => $sex,
                                  'age' => $age,
                                  'adress' => $adress,
                                  'wash' => $wash,
                                  'meal' => $meal,
                                  'move' => $move,
                                  'remark' => $remark,
                                  'writedate' => $write_date
                                
                                )); 
                                }
                                         
                          echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
                                break;


 }

?>