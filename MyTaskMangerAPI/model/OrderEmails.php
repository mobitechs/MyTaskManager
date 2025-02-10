<?php
require_once 'EmailGenarator.php';
class OrderEmails
{	
	public $orderId;
	public $orderDetails;public $address;public $userName;public $mobile;public $email;public $adminEmail;public $buisnessName;public$ownerMobile;
	//For User Registration
	public function OrderGeneration($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile){
		
		$orderId = $orderId;	
		$orderDetails = $orderDetails;
		$address = $address;
		$userName = $userName;
		$mobile = $mobile;
		$email = $email;
		$adminEmail = $adminEmail;
		$buisnessName = $buisnessName;
		$ownerMobile = $ownerMobile;
		
		
		//email for User		
		$returnEmailForUser = new OrderEmails();
        $returnEmailForUser -> OrderEmailToUser($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile);	
		
		//email to Admin
		$returnEmailForMobitechs= new OrderEmails();		
        $returnEmailForMobitechs -> OrderEmailToAdmin($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile);	
		
		$returnEmailSuccessMessage = "EMAIL_SUCCESSFULLY_SENT";
		return $returnEmailSuccessMessage;
    }
	
	// send email to User for New Registration..
	public function OrderEmailToUser($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile){
	    
        $emailSender = new EmailGenarator();
        $emailSender->setTo($email);//write user mail id
        $emailSender->setFrom("From: $adminEmail" . "\r\n" . 'Reply-To: no-reply@app.com' . "\r\n" . 'X-Mailer: PHP/' . phpversion());
        $emailSender->setMessage($this->createMessageForUser($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile));
        $emailSender->setSubject("Order Generate");// from petapp email      
		$returnEmailForVendor =  $emailSender->sendEmail($emailSender);		
		if($returnEmailForVendor==true){
			return $returnEmailForVendor;
		}else {
			$emailSender->sendEmail($emailSender);
		}      
    }
	
    public function createMessageForUser($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile){
		$emailMessage="Order Genetate,\n OrderId = '$orderId' .\n OrderDetails = '$orderDetails' . \n\nAddress = $address . \n\nThanking you,\n$buisnessName \n$ownerMobile";	 
		return $emailMessage;
    }
	
	//email to vbags for New User Registration.
	public function OrderEmailToAdmin($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile){
	     
        $emailSender = new EmailGenarator();
        $emailSender->setTo($adminEmail);//write user mail id
        $emailSender->setFrom('From: mobitechs17@gmail.com' . "\r\n" . 'Reply-To: no-reply@app.com' . "\r\n" . 'X-Mailer: PHP/' . phpversion());
        $emailSender->setMessage($this->createMessageForAdmin($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile));
        $emailSender->setSubject("New Order Genetated");// from petapp email      
		$returnEmailForVendor =  $emailSender->sendEmail($emailSender);		
		if($returnEmailForVendor==true){
			return $returnEmailForVendor;
		}else {
			$emailSender->sendEmail($emailSender);
		}      
    } 
    public function createMessageForAdmin($orderId,$orderDetails,$address,$userName,$mobile,$email,$adminEmail,$buisnessName,$ownerMobile){
        $emailMessage="New Order Genetate,\n OrderId = $orderId.\nOrderDetails = $orderDetails.\nUsername = $userName.\nEmail = $email \nMobile No = $mobile \nAddress = $address.";	 
		return $emailMessage;
    }
    
}
?>