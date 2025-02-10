<?php
require_once 'EmailGenarator.php';
class NewRegistrationEmails
{	
	//For User Registration
	public function SendNewUserRegistrationEmail($name,$email,$mobileNo,$numOfRows){
				
		//email for User		
		$returnEmailForUser = new NewRegistrationEmails();
        $returnEmailForUser -> GenarateEmailForUser($name,$email,$mobileNo);	
		
		//email to mobitechs
		$returnEmailForMobitechs= new NewRegistrationEmails();		
        $returnEmailForMobitechs -> GenarateUserRegistrationEmailForMobitechs($name,$email,$mobileNo,$numOfRows);	
		
		$returnEmailSuccessMessage = "EMAIL_SUCCESSFULLY_SENT";
		return $returnEmailSuccessMessage;
    }
	
	// send email to User for New Registration..
	public function GenarateEmailForUser($name,$email,$mobileNo){
        $emailSender = new EmailGenarator();
        $emailSender->setTo($email);//write user mail id
        $emailSender->setFrom('From: mobitechs17@gmail.com' . "\r\n" . 'Reply-To:  mobitechs17@gmail.com' . "\r\n" . 'X-Mailer: PHP/' . phpversion());
        $emailSender->setMessage($this->createMessageToSendUser($name,$email));
        $emailSender->setSubject("Welcome to DailyExpense");// from petapp email      
		$returnEmailForVendor =  $emailSender->sendEmail($emailSender);		
		if($returnEmailForVendor==true){
			return $returnEmailForVendor;
		}else {
			$emailSender->sendEmail($emailSender);
		}      
    } 
    public function createMessageToSendUser($name,$email){
        $emailMessage="Hey there !\n\n\nThank you so much for downloading and Registering with DailyExpense. We hope to keep you updated with your device. \nWe are constantly striving hard to aggregate all possible information for you. \nPlease do let us know if you have any suggestions or feedback regarding our offerings as we are always open to new ideas and suggestions. \n\nThanking you,\nTeam MobiTechs";	      
		return $emailMessage;
    }
	
	//email to vbags for New User Registration.
	public function GenarateUserRegistrationEmailForMobitechs($name,$email,$mobileNo,$numOfRows){
        $emailSender = new EmailGenarator();
        $emailSender->setTo('mobitechs17@gmail.com');//write user mail id
        $emailSender->setFrom('From: dailyexpense@gmail.com' . "\r\n" . 'Reply-To: no-reply@app.com' . "\r\n" . 'X-Mailer: PHP/' . phpversion());
        $emailSender->setMessage($this->createUserReistrationMessageToSendVBags($name,$email,$mobileNo,$numOfRows));
        $emailSender->setSubject("New DailyExpense User Registration");// from petapp email      
		$returnEmailForVendor =  $emailSender->sendEmail($emailSender);		
		if($returnEmailForVendor==true){
			return $returnEmailForVendor;
		}else {
			$emailSender->sendEmail($emailSender);
		}      
    } 
    public function createUserReistrationMessageToSendVBags($name,$email,$mobileNo,$numOfRows){
        $emailMessage="New User Registration Done,\n Username = $name.\nEmail = $email \nMobile No = $mobileNo \nTotal User = $numOfRows.";	 
		return $emailMessage;
    }
    
}
?>