<?php 

include 'and_db.php'

?>

<?php 

 switch ($_POST["mode"]) {
     case 'check_id':
        $response=array();
        $id=preg_replace("/\s+/","",$_POST["id"]);  
       //id 값들어오는거확인
       $sql = mq("select * from care_cs WHERE cs_id = '$id'"); // 게시판 총 레코드 수 
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
       

        $sql2= "insert into care_cs ( cs_id  ,cs_pw ,cs_tell) 
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
            //없는아이디 0 회원1 관리자2

            $id = $_POST["id"];
            $pw = $_POST["pw"];
         
            $select_query = "SELECT  * FROM care_cs WHERE cs_id = '$id' ";
            $result_set = mysqli_query($conn, $select_query);       
            $response = array();
            while($row=mysqli_fetch_array($result_set)){
                 $mb_pw=$row["cs_pw"];
                 $level = $row["cs_level"];
                 $resum = $row["cs_resum"];
                 $match = password_verify($pw,$mb_pw);
                 if($match){
                    
                    array_push($response, array('level'=>$level, 'resum'=>$resum ,
                     ));
                }else{
                   
                    $level = "0";
                    $resum = "N";
                    array_push($response, array('level'=>$level, 'resum'=>$resum ,
                ));
                }
                break;
            }
            
             if($row == null){
                $level = "0";
                $resum = "N";
                array_push($response, array('level'=>$level, 'resum'=>$resum ,
            ));
             }
    
             echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE); 
    
            break;
    
            
         case 'find_id':   

            $number = $_POST["number"];
            $select_query = "SELECT  * FROM care_cs WHERE cs_tell = '$number' ";
            $result_set = mysqli_query($conn, $select_query);     
      
            $response = array();     
          
    
            while($row=mysqli_fetch_array($result_set)){
                $id=$row["cs_id"];
                array_push($response, array('id'=>$id ));
                break;
            }
            if($row==null){
                array_push($response, array('id'=>" 존재하지 않음" ));
          
            }
            echo json_encode(array("response"=>$response ), JSON_UNESCAPED_UNICODE);
            
         


             break;


             case 'new_pw':
                    //현재비밀번호랑 맞지않음 
                $pw = $_POST["pw"];
                $pw2 = $_POST["pw2"];
                $id = $_POST["id"];
                $response = array();
                $encrypted_passwd  = password_hash($pw2, PASSWORD_DEFAULT); 
                $sq6 = mq("select * from care_cs where cs_id = '".$id."'");

                while($rm = $sq6->fetch_array()){  
                    $mb_pw=$rm["cs_pw"];
                    $match = password_verify($pw,$mb_pw);
                    if($match){
                        $response['success'] = true;
                    }else{
                        $response['success'] = false;
                    }

                }

                
                break;


     
 }

?>