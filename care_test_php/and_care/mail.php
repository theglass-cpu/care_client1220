<?php 
include 'and_db.php' ?>


<?php

/**
 * This example shows settings to use when sending via Google's Gmail servers.
 * This uses traditional id & password authentication - look at the gmail_xoauth.phps
 * example to see how to use XOAUTH2.
 * The IMAP section shows how to save this message to the 'Sent Mail' folder using IMAP commands.
 */



include "PHPMailer.php";
include "SMTP.php";

//Create a new PHPMailer instance
$mail = new PHPMailer();

//Tell PHPMailer to use SMTP
$mail->isSMTP();

//Enable SMTP debugging
//SMTP::DEBUG_OFF = off (for production use)
//SMTP::DEBUG_CLIENT = client messages
//SMTP::DEBUG_SERVER = client and server messages
$mail->SMTPDebug = SMTP::DEBUG_OFF;

//Set the hostname of the mail server
$mail->Host = 'smtp.naver.com';
//Use `$mail->Host = gethostbyname('smtp.gmail.com');`
//if your network does not support SMTP over IPv6,
//though this may cause issues with TLS

//Set the SMTP port number - 587 for authenticated TLS, a.k.a. RFC4409 SMTP submission
$mail->Port = 465;

//Set the encryption mechanism to use - STARTTLS or SMTPS
$mail->SMTPSecure = "ssl";

//Whether to use SMTP authentication
$mail->SMTPAuth = true;

//Username to use for SMTP authentication - use full email address for gmail
$mail->Username = 'dfdbfg';

//Password to use for SMTP authentication
$mail->Password = 'PSMU71U6PJLZ';

$mail->CharSet = 'UTF-8';


//Set who the message is to be sent from
$mail->setFrom('dfdbfg@naver.com', '케어팜');

switch ($_POST["mode"]){
    
    case 'care_client': 

        $id = $_POST["id"];
        $pw = $_POST["pw"];
        $encrypted_passwd  = password_hash($pw, PASSWORD_DEFAULT); 
        $select_query = "UPDATE care_client SET c_pw = '$encrypted_passwd' WHERE c_id = '$id'";
        $result_set = mysqli_query($conn, $select_query);    
        
        $query = "SELECT  * FROM care_client WHERE c_id = '$id' ";
        $set = mysqli_query($conn, $query);   
        while($row=mysqli_fetch_array($set)){
            $email=$row["c_id"];
            $mail->addAddress($email, '회원님');
        }
        break;
    
   case 'care_cs': 

    $id = $_POST["id"];
    $pw = $_POST["pw"];
    $encrypted_passwd  = password_hash($pw, PASSWORD_DEFAULT); 
    $select_query = "UPDATE care_cs SET cs_pw = '$encrypted_passwd' WHERE cs_id = '$id'";
    $result_set = mysqli_query($conn, $select_query);    
    
    $query = "SELECT  * FROM care_cs WHERE cs_id = '$id' ";
    $set = mysqli_query($conn, $query);   
    while($row=mysqli_fetch_array($set)){
        $email=$row["cs_id"];
        $mail->addAddress($email, '간병인 회원님');
    }


           break;
}



//Set an alternative reply-to address
$mail->addReplyTo('dfdbfg@naver.com', '정유리');


//Set who the message is to be sent to



//$mail->addAddress('paradis001@naver.com', '회원님');

//Set the subject line
$mail->Subject = '케어팜 임시비밀번호 입니다.';

//Read an HTML message body from an external file, convert referenced images to embedded,
//convert HTML into a basic plain-text alternative body


//echo $rand_number ;
//$mail->msgHTML(file_get_contents('contents.php'), __DIR__);
$mail->msgHTML("$pw");



//Replace the plain text body with one created manually
$mail->AltBody = '메일내용부분';

//Attach an image file
$mail->addAttachment('');

//send the message, check for errors
if (!$mail->send()) {
   // echo 'Mailer Error: ' . $mail->ErrorInfo;
} else {

    echo "이메일전송완료";

   
   // header('Location: home.php');
    
    //Section 2: IMAP
    //Uncomment these to save your message in the 'Sent Mail' folder.
    #if (save_mail($mail)) {
    #    echo "Message saved!";
    #}
}

?>