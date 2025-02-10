<?php
require_once '../dao/UsersDetailsDAO.php';
require_once 'RandomNoGenarator.php';
require_once 'EmailGenarator.php';

class UsersDetailsEmails
{
   
    public function GenarateOTPForForgoPassword($email)
    {
        $randomno = new RandomNoGenarator();
        $genaratedRandomNo = $randomno->GenarateCode(6);

        // print_r($email);
        // print_r($genaratedRandomNo);

        // call GenarateEmailForUSer to send Otp to user
		$returnSuccessRandomNo = $this->SendOTPEmailToUSer($email,$genaratedRandomNo);


        //call UsersDetailsDAO to save otp of user 
        $saveRandomNoDAO = new UsersDetailsDAO();
        $returnSuccessRandomNo = $saveRandomNoDAO->SavingOTPToUserDetails($genaratedRandomNo,$email);
        return $returnSuccessRandomNo;
    }

    //Call Email Class to create Email for user
    public function SendOTPEmailToUSer($email,$genaratedRandomNo)
    {
        $EmailSender = new EmailGenarator();
        $EmailSender->setTo($email);//write user mail id
        $EmailSender->setFrom('From: noreply@mobitechs.in' . "\r\n" . 'Reply-To: no-reply@mobitechs.in' . "\r\n" . 'X-Mailer: PHP/' . phpversion());//write pet App mail id
        $EmailSender->setMessage($this->createMessageToSendUser($genaratedRandomNo));
        $EmailSender->setSubject("OTP My Task Manager");
        return $EmailSender->sendEmail($EmailSender);
    }

    public function createMessageToSendUser($genaratedRandomNo)
    {
        $EmailMessage = "Your OTP code is " . $genaratedRandomNo;
        return $EmailMessage;
    }


}

?>