<?php
require_once '../dao/UsersDetailsDAO.php';

function deliver_response($format, $api_response, $isSaveQuery)
{

    // Define HTTP responses
    $http_response_code = array(200 => 'OK', 400 => 'Bad Request', 401 => 'Unauthorized', 403 => 'Forbidden', 404 => 'Not Found');

    // Set HTTP Response
    header('HTTP/1.1 ' . $api_response['status'] . ' ' . $http_response_code[$api_response['status']]);

    // Process different content types
    if (strcasecmp($format, 'json') == 0) {

        ignore_user_abort();
        ob_start();

        // Set HTTP Response Content Type
        header('Content-Type: application/json; charset=utf-8');

        // Format data into a JSON response
        $json_response = json_encode($api_response);

        // Deliver formatted data
        echo $json_response;

        ob_flush();

    } elseif (strcasecmp($format, 'xml') == 0) {

        // Set HTTP Response Content Type
        header('Content-Type: application/xml; charset=utf-8');

        // Format data into an XML response (This is only good at handling string data, not arrays)
        $xml_response = '<?xml version="1.0" encoding="UTF-8"?>' . "\n" . '<response>' . "\n" . "\t" . '<code>' . $api_response['code'] . '</code>' . "\n" . "\t" . '<data>' . $api_response['data'] . '</data>' . "\n" . '</response>';

        // Deliver formatted data
        echo $xml_response;

    } else {

        // Set HTTP Response Content Type (This is only good at handling string data, not arrays)
        header('Content-Type: text/html; charset=utf-8');

        // Deliver formatted data
        echo $api_response['data'];

    }

    // End script process
    exit;
}

// Define whether an HTTPS connection is required
$HTTPS_required = FALSE;

// Define whether user authentication is required
$authentication_required = FALSE;

// Define API response codes and their related HTTP response
$api_response_code = array(0 => array('HTTP Response' => 400, 'Message' => 'Unknown Error'), 1 => array('HTTP Response' => 200, 'Message' => 'Success'), 2 => array('HTTP Response' => 403, 'Message' => 'HTTPS Required'), 3 => array('HTTP Response' => 401, 'Message' => 'Authentication Required'), 4 => array('HTTP Response' => 401, 'Message' => 'Authentication Failed'), 5 => array('HTTP Response' => 404, 'Message' => 'Invalid Request'), 6 => array('HTTP Response' => 400, 'Message' => 'Invalid Response Format'));

// Set default HTTP response of 'ok'
$response['code'] = 0;
$response['status'] = 404;

// --- Step 2: Authorization

// Optionally require connections to be made via HTTPS
if ($HTTPS_required && $_SERVER['HTTPS'] != 'on') {
    $response['code'] = 2;
    $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
    $response['data'] = $api_response_code[$response['code']]['Message'];

    // Return Response to browser. This will exit the script.
    deliver_response("json", $response);
}

// Optionally require user authentication
if ($authentication_required) {

    if (empty($_POST['username']) || empty($_POST['password'])) {
        $response['code'] = 3;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $response['data'] = $api_response_code[$response['code']]['Message'];

        // Return Response to browser
        deliver_response("json", $response);

    }

    // Return an error response if user fails authentication. This is a very simplistic example
    // that should be modified for security in a production environment
    elseif ($_POST['username'] != 'foo' && $_POST['password'] != 'bar') {
        $response['code'] = 4;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $response['data'] = $api_response_code[$response['code']]['Message'];

        // Return Response to browser
        deliver_response("json", $response);
    }
}

// --- Step 3: Process Request

// Switch based on incoming method
$checkmethod = $_SERVER['REQUEST_METHOD'];
$var = file_get_contents("php://input");
$string = json_decode($var, TRUE);
$method = $string['method'];

if (isset($_POST['method']) || $checkmethod == 'POST') {

 if (strcasecmp($method, 'AddDeviceDetails') == 0) {
		$response['code'] = 1;
		$response['status'] = $api_response_code[$response['code']]['HTTP Response'];
		$objModel = new DeviceDetailsDAO();
		$deviceId = $string['deviceId'];
		$tokenNo = $string['tokenNo'];
		$userId = $string['userId'];
		$model = $string['model'];
		$manufacturer = $string['manufacturer'];
		date_default_timezone_set('Asia/Kolkata');
		$entryDate = date("Y-m-d H:i:s");
		$response['Response'] = $objModel->AddDeviceDetails($deviceId, $tokenNo,$userId,$model,$manufacturer,$entryDate);
		deliver_response("json", $response, false);
	}
	else if (strcasecmp($method, 'userRegister') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();       
		$name = $string['name'];
        $email = $string['email'];
        $phone = $string['phone'];        
        $password = $string['password'];
        $response['Response'] = $objModel->userRegister($name,$email,$phone,$password);
        deliver_response("json", $response, false);
    }
	
	else if (strcasecmp($method, 'userLogin') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();       
        $userLoginId = $string['loginId'];
        $password = $string['password'];
        $response['Response'] = $objModel->userLogin($userLoginId, $password);
        deliver_response("json", $response, false);
    }
	
    else if (strcasecmp($method, 'forgotPassword') == 0) {	
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();        
        $email = $string['email'];
        $response['Response'] = $objModel->forgotPassword($email);
        deliver_response("json", $response, false);
    }
	
    else if (strcasecmp($method, 'setPassword') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();       
        $email = $string['email'];
		$otp = $string['otp'];
        $password = $string['password'];
        $response['Response'] = $objModel->setPassword($email, $otp, $password);
        deliver_response("json", $response, false);
    }

    else if (strcasecmp($method, 'getOTPOnEmail') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();
        $email = $string['email'];       
        $response['Response'] = $objModel->getOTPOnEmail($email);
        deliver_response("json", $response, false);
    }

	
	else if (strcasecmp($method, 'getUserDetails') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();
        $userId = $string['userId'];
        $response['Response'] = $objModel->getUserDetails($userId);
        deliver_response("json", $response, false);
    }
	
	
    
    else if (strcasecmp($method, 'createTeam') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$teamName = $string['teamName'];		
		$description = $string['description'];
		$image = $string['image'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->createTeam($teamName,$description,$image,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'editTeam') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$teamId = $string['teamId'];		
		$teamName = $string['teamName'];		
		$description = $string['description'];
		$image = $string['image'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->editTeam($teamId,$teamName,$description,$image,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'deleteTeam') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$teamId = $string['teamId'];	
        $response['Response'] = $objModel->deleteTeam($teamId);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'addTeamMember') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$teamId = $string['teamId'];
		$teamMemberId = $string['teamMemberId'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->addTeamMember($teamId,$teamMemberId,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'deleteTeamMember') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$teamId = $string['teamId'];
		$teamMemberId = $string['teamMemberId'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->deleteTeamMember($teamId,$teamMemberId,$updatedBy);
        deliver_response("json", $response, false);
    }
    else if (strcasecmp($method, 'addTask') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$taskName = $string['taskName'];
		$taskDescription = $string['taskDescription'];
		$kpi = $string['kpi'];	
		$ownerId = $string['ownerId'];	
		$assigneeId = $string['assigneeId'];	
		$teamId = $string['teamId'];	
		$expectedDate = $string['expectedDate'];	
		$status = $string['status'];	
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->addTask($taskName,$taskDescription,$kpi,$ownerId,$assigneeId,$teamId,$expectedDate,$status,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'editTask') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$taskId = $string['taskId'];
		$taskName = $string['taskName'];
		$taskDescription = $string['taskDescription'];
		$kpi = $string['kpi'];	
		$ownerId = $string['ownerId'];	
		$assigneeId = $string['assigneeId'];	
		$teamId = $string['teamId'];	
		$expectedDate = $string['expectedDate'];	
		$status = $string['status'];	
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->editTask($taskId,$taskName,$taskDescription,$kpi,$ownerId,$assigneeId,$teamId,$expectedDate,$status,$updatedBy);
        deliver_response("json", $response, false);
    }
	
	else if (strcasecmp($method, 'deleteTask') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$taskId = $string['taskId'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->deleteTask($taskId,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'updateStatus') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$taskId = $string['taskId'];
		$status = $string['status'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->updateStatus($taskId,$status,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'addReminderForTask') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$taskId = $string['taskId'];
		$noOfReminder = $string['noOfReminder'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->addReminderForTask($taskId,$noOfReminder,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'addComment') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$taskId = $string['taskId'];
		$comment = $string['comment'];
		$expectedDate = $string['expectedDate'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->addComment($taskId,$comment,$expectedDate,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'editComment') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO();      
		$taskId = $string['taskId'];
		$comment = $string['comment'];
		$expectedDate = $string['expectedDate'];
		$updatedBy = $string['updatedBy'];	
        $response['Response'] = $objModel->editComment($taskId,$comment,$expectedDate,$updatedBy);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'getTaskListAssignedToMe') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO(); 
		$userId = $string['userId'];	
        $response['Response'] = $objModel->getTaskListAssignedToMe($userId);
        deliver_response("json", $response, false);
    }
	else if (strcasecmp($method, 'getTaskListAssignedByMe') == 0) {
        $response['code'] = 1;
        $response['status'] = $api_response_code[$response['code']]['HTTP Response'];
        $objModel = new UsersDetailsDAO(); 
		$userId = $string['userId'];	
        $response['Response'] = $objModel->getTaskListAssignedByMe($userId);
        deliver_response("json", $response, false);
    }
   

} else {

 
	
	

}
?>
