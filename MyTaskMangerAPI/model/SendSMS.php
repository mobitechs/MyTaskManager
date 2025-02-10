<?php
class SendSMS
{	
   
			
	//For User Registration
	public function SendMessage($messageType,$mobileNo){
				
		$templateId="";
		$message="";
		
		if($messageType == "Registration"){
			$templateId="1207161762994507815";
			$message="Your Slash Discount Card registration is now complete, Check detailed list of our vendors on the app. For more info contact 7786004004 www.slashdiscounts.in";
		}
		else{
			$templateId="1207161762963385961";
			$message="Thank you for buying Slash Discounts Card, Please register your membership on our mobile app once your card is delivered. Get in touch at 7786004004 for order.";
		}
		
			
		$returnEmailForUser = new SendSMS();
        $returnEmailForUser -> sendMessagesOnUsersMobile($mobileNo,$message,$templateId);
		

    }
	public function SendOTPMessage($otp,$mobileNo){
	    
	   // print_r("Hello there");
				
		$templateId="";
		$message="";
		
		$templateId="1207162454593199412";
		$message="Dear User, Your OTP for registration to Slash Discounts App is $otp Please do not share this OTP. Regards, Team Slash Discounts ";
		
			
		$returnEmailForUser = new SendSMS();
        	$returnEmailForUser -> sendMessagesOnUsersMobile($mobileNo,$message,$templateId);
		

    }
public function sendMessagesOnUsersMobile($mobileNo,$message,$templateId){
   // print_r("Good one");
  		$url = 'http://sms.adservs.co.in/vb/apikey.php?';
    	//

			$fields = array(
				'apikey'      => "3W2YPRBJLsA4gQlO",
				'senderid'      	 => "SLASHD",
				'number'      => $mobileNo,
				'message' => $message,
				'templateid' => $templateId
			);
			
			//open connection
			$ch = curl_init();

			//set the url, number of POST vars, POST data
			curl_setopt($ch, CURLOPT_URL, $url);
			curl_setopt($ch, CURLOPT_POST, count($fields));
			curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($fields));
            curl_setopt($ch, CURLOPT_RETURNTRANSFER,1);
			//execute post
        //   $result=curl_exec($ch);
            curl_exec($ch);
// exit;
	//close connection
	//curl_close($ch);

//	var_dump($result);
    
    // return "Success";
}    


    
	

	
    
}
?>