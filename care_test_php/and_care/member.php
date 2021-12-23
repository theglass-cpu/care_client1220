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
                        echo $adress;

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
                            $match = '0';
                            $cs_accept = '1';

                            $sql = mq("select * from matching_tb where cl_id ='".$id."' AND cl_accept = '".$match."' AND  cs_accept = '".$cs_accept."'");
                            while($list = $sql->fetch_array()){  
                                $index = $list["cl_index"]; 
                                $cs_id = $list["cs_id"]; 
                                $cs_name = $list["cs_name"]; 

                              array_push($response, array('index'=>$index, 'cs_id'=>$cs_id , 'cs_name'=>$cs_name )); 
                            }
                                     
                      echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
                            break;


                            case 'request_detail':
                                //$id = $_POST["id"];
                                $index = $_POST["index"];
                                $status = 'N';
                                $response = array(); 
                               
               
    
                                $sql = mq("select * from client_request where c_status = '".$status."' AND c_index = '".$index."' ");
                                while($list = $sql->fetch_array()){  
                                    $user = $list["c_id"]; 
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
                                  'user'=>$user, 
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
                              
                            case 'caregiver_info' :
                                $id = $_POST["id"];
                                $response = array(); 
                                $status = 'N';
                                $sql = mq("select * from care_cs_resum where cs_id='".$id."' AND cs_status = '".$status."'");
                                while($list = $sql->fetch_array()){
                                    $index=$list["cs_index"];
                                    $name=$list["cs_name"];
                                    $id=$list["cs_id"];
                                    $sx=$list["cs_sx"];
                                    $age=$list["cs_age"];
                                    $lo=$list["cs_lo"];
                                    $cf=$list["cs_cf"];
                                    $wh=$list["cs_wh"];
                                    $ml=$list["cs_ml"];
                                    $mv=$list["cs_mv"];
                                    $ok=$list["cs_ok"];
                                    $df=$list["cs_df"];
                                    $pp=$list["cs_pp"];
                                    $level=$list["cs_level"];
                            
                                    array_push($response, array('name'=>$name, 'sx'=>$sx , 
                                    'age'=>$age, 'lo'=>$lo ,'cf'=>$cf ,'wh'=>$wh, 'level'=>$level,
                                    'ml'=>$ml , 'mv'=>$mv , 'ok'=>$ok , 'df'=>$df , 'pp'=>$pp ,'index'=>$index ,'id'=>$id  ));
                             
                                
                                } 
                                echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
                                
                                
                                break;


                                case 'matching_refuse':
                                    $id = $_POST["id"];
                                    $cs_id = $_POST["cs_id"];
                                    $cs_name = $_POST["cs_name"];
                                    $cs_accept="Y";
                                    $index = $_POST["index"];
                                    $cs_resum_index = $_POST["cs_resum_index"];
                                    $match = $_POST["match"];
                                    $today = date("Y/m/d");
                                
                                    if($match=="1"){
                                        $delete = mq("update matching_tb set cl_accept = '".$match."' , cs_resum_index = '".$cs_resum_index."'  where cl_index = '".$index."'");  
                                                        
                                    $sl = mq("insert into chating_room (
                                         cs_id , cs_name  , cs_resum_index , 
                                         cl_id , cl_request_index , room_date ) 
                                    values 
                                    ( '$cs_id' , '$cs_name' , '$cs_resum_index' , 
                                    '$id' , '$index' ,'$today' )"); // 게시판 총 레코드 수 
                                        
                                    }else{
                                        $delete = mq("update matching_tb set cl_accept = '".$match."' , cs_resum_index = '".$cs_resum_index."'  where cl_index = '".$index."'");
                                    }
                                     
                                    case 'match_list' :
                                        $id = $_POST["id"];
                                        $tb_status = "0";    
                                        $response = array(); 
                                        $match_list = mq("select * from chating_room where cl_id = '".$id."' AND tb_status = '".$tb_status."' ");
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

                                          case 'cl_use_list':
                                            $id = $_POST["id"];
                                            $response = array(); 
                                            $msg_list = mq("select * from chating_room where cl_id = '".$id."' ");
    
                                            while($list = $msg_list ->fetch_array()){
                                                    $cs_id=$list["cs_id"];
                                                    $cs_resum_index=$list["cs_resum_index"];
                                                    $cs_review=$list["cs_review"];
                                                    $cs_name=$list["cs_name"];

                                                    array_push($response, array(
                                                        'cs_id'=>$cs_id, 'cs_resum_index'=>$cs_resum_index ,
                                                         'cs_review'=>$cs_review , 'cs_name'=>$cs_name)); 
    
                                            }
                                            echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
    
                                              break;   
                                              
                                              
                                            case 'write_review':
                                                $cs_id = $_POST["cs_id"];
                                                $cl_id = $_POST["cl_id"];
                                                $cs_review = $_POST["cs_review"];
                                                $cs_resum = $_POST["cs_resum"];
                                                $cs_rating = $_POST["cs_rating"];
                                                $date = $_POST["date"];

                                                $response = array(); 
                                                $sl = mq("insert into cs_review_tb ( cs_id , cl_id , cs_rating , cs_review , cs_resum_index , review_data ) 
                                                values ( '$cs_id' , '$cl_id' , '$cs_rating' , '$cs_review' ,'$cs_resum' ,'$date')"); 
                                                
                                                $review_status = mq("update chating_room set cs_review = '1'  where cl_id = '".$cl_id."' AND cs_id = '".$cs_id."'");
                                                echo json_encode($response);
                                                break;


                                             case 'review_list':
                                                $cs_id = $_POST["cs_id"];
                                                (int)$page = $_POST['page'];
                                                $sql0 = mq("select * from cs_review_tb where cs_id ='".$cs_id."'"); // 
                                                $max= mysqli_num_rows($sql0); //총 리뷰수 
                                                $list = 20;
                                                $total_page = ceil($max/$list); //20으로 나눔 
                                                $page_start = (($page-1)* $list); 
                                                $response = array(); 
                                                $review_list = mq("select * from cs_review_tb where cs_id = '".$cs_id."' ORDER BY cs_review_index limit  $page_start, 20");
                                                    while($review = $review_list ->fetch_array()){
                                                        $cs_rating=$review["cs_rating"];
                                                        $review_data=$review["review_data"];
                                                        $cs_review=$review["cs_review"];
                                                        $cl_id=$review["cl_id"];
                                                        $cs_id=$review["cs_id"];
                                                        $review_status=$review["review_status"];
                                                        $review_index=$review["cs_review_index"];


                                                        array_push($response, array(
                                                            'cs_rating'=>$cs_rating, 'review_data'=>$review_data ,
                                                             'cs_review'=>$cs_review , 'cl_id'=>$cl_id , 'cs_review_index' =>$review_index ,'cs_id' =>$cs_id , 'review_status' =>$review_status )); 

                                                    }

                                              
                                                    echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
                                                break;


                                             case 'review_edit' :
                                                
                                                $index = $_POST["index"];
                                                $rating = $_POST["rating"];
                                                $msg = $_POST["msg"];
                                                $date = $_POST["date"];
                                                $response = array();

                                                      
                                                $review_status = mq("update cs_review_tb 
                                                 set cs_rating = '".$rating."' , cs_review = '".$msg."' , review_data = '".$date."' 
                                                 where cs_review_index = '".$index."' ");
                                                echo json_encode($response);


                                                break;

                                                case 'review_delite' :
                                                    $index = $_POST["index"];
                                                    $status = "Y";
                                                    $response = array();
             
                                                    $review_status = mq("update cs_review_tb 
                                                    set review_status = '".$status."'
                                                    where cs_review_index = '".$index."' ");
                                                   echo json_encode($response);

                                                    break;
                                                
                                                    case 'client_request_list' :
                                                
                                                        $id = $_POST["id"];

        
                                                        $response = array(); 
                                                        $client_list = mq("select * from client_request where c_id = '".$id."' ");
                
                                                        while($list = $client_list ->fetch_array()){
                                                                $cl_id=$list["c_id"];
                                                                $cl_index=$list["c_index"];
                                                                $cl_write_date=$list["c_write_date"];
                                                                $cl_request_date=$list["c_date"];
            
                                                                array_push($response, array(
                                                                    'cl_id'=>$cl_id, 'cl_index'=>$cl_index ,
                                                                     'cl_write_date'=>$cl_write_date , 'cl_request_date'=>$cl_request_date)); 
                
                                                        }
                                                        echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
                
        
                                                        break;      
                                                        
                                                        

                                                        case 'request_deadline' :
                                                
                                                            $index = $_POST["index"];
                                                            $status = "Y";    
                                                            $response = array();
            
                                                                  
                                                            $review_status = mq("update client_request 
                                                             set c_deadline = '".$status."' 
                                                             where c_index = '".$index."' ");
                                                            echo json_encode($response);
            
            
                                                            break;


                                         
                                         
 }

?>