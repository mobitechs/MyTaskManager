<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

header("Access-Control-Allow-Origin: *");
header("Content-Type: application/json; charset=UTF-8");
header("Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS");
header("Access-Control-Allow-Headers: Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");

require_once '../dao/UsersDetailsDAO.php';

$requestUri = explode("/", trim($_SERVER['REQUEST_URI'], "/"));
$apiIndex = array_search("api", $requestUri);
$method = isset($requestUri[$apiIndex + 1]) ? $requestUri[$apiIndex + 1] : null;
$data = json_decode(file_get_contents("php://input"), true);

switch ($_SERVER['REQUEST_METHOD']) {
    case 'POST':
        if ($method == "userLogin") {
            userLogin($data);
        } elseif ($method == "userRegister") {
            userRegister($data);
        } elseif ($method == "forgotPassword") {
            forgotPassword($data);
        } elseif ($method == "setPassword") {
            setPassword($data);
        } elseif ($method == "getOTPOnEmail") {
            getOTPOnEmail($data);
        } elseif ($method == "getUserDetails") {
            getUserDetails($data);
        } elseif ($method == "createTeam") {
            createTeam($data);
        } elseif ($method == "editTeam") {
            editTeam($data);
        } elseif ($method == "deleteTeam") {
            deleteTeam($data);
        } elseif ($method == "addTeamMember") {
            addTeamMember($data);
        } elseif ($method == "deleteTeamMember") {
            deleteTeamMember($data);
        } elseif ($method == "addTask") {
            addTask($data);
        } elseif ($method == "editTask") {
            editTask($data);
        } elseif ($method == "deleteTask") {
            deleteTask($data);
        } elseif ($method == "updateStatus") {
            updateStatus($data);
        } elseif ($method == "addReminderForTask") {
            addReminderForTask($data);
        } elseif ($method == "addComment") {
            addComment($data);
        } elseif ($method == "editComment") {
            editComment($data);
        } elseif ($method == "getTaskListAssignedToMe") {
            getTaskListAssignedToMe($data);
        } elseif ($method == "getTaskListAssignedByMe") {
            getTaskListAssignedByMe($data);
        } elseif ($method == "addDeviceDetails") {
            addDeviceDetails($data);
        } else {
            response(400, "Invalid API endpoint.");
        }
        break;
    default:
        response(405, "Method Not Allowed.");
        break;
}

function response($statusCode,$status, $message, $data = null) {
    http_response_code($statusCode);
    echo json_encode(["statusCode" => $statusCode,"status" => $status, "message" => $message, "data" => $data]);
}

function userRegister($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->userRegister($data['name'], $data['email'], $data['phone'], $data['password']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function userLogin($data) {
    $dao = new UsersDetailsDAO();
    $result = $dao->userLogin($data['loginId'], $data['password']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}



function forgotPassword($data) {
    $dao = new UsersDetailsDAO();
    
    $result = $dao->forgotPassword($data['email']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);
}

function setPassword($data) {
    $dao = new UsersDetailsDAO();
    $result = $dao->setPassword($data['email'], $data['otp'], $data['password']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function getOTPOnEmail($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->getOTPOnEmail($data['email']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function getUserDetails($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->getUserDetails($data['userId']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);
}

function addDeviceDetails($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->AddDeviceDetails($data['deviceId'], $data['tokenNo'], $data['userId'], $data['model'], $data['manufacturer'], date("Y-m-d H:i:s"));
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}


// Team details API

function createTeam($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->createTeam($data['teamName'], $data['description'], $data['image'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function editTeam($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->editTeam($data['teamId'], $data['teamName'], $data['description'], $data['image'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function deleteTeam($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->deleteTeam($data['teamId']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function addTeamMember($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->addTeamMember($data['teamId'], $data['teamMemberId'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function deleteTeamMember($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->deleteTeamMember($data['teamId'], $data['teamMemberId'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

// Task Details API

function addTask($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->addTask($data['taskName'], $data['taskDescription'], $data['kpi'], $data['ownerId'], $data['assigneeId'], $data['teamId'], $data['expectedDate'], $data['status'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function editTask($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->editTask($data['taskId'], $data['taskName'], $data['taskDescription'], $data['kpi'], $data['ownerId'], $data['assigneeId'], $data['teamId'], $data['expectedDate'], $data['status'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function deleteTask($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->deleteTask($data['taskId'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);
}

function updateStatus($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->updateStatus($data['taskId'], $data['status'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function addReminderForTask($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->addReminderForTask($data['taskId'], $data['noOfReminder'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function addComment($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->addComment($data['taskId'], $data['comment'], $data['expectedDate'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function editComment($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->editComment($data['taskId'], $data['comment'], $data['expectedDate'], $data['updatedBy']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function getTaskListAssignedToMe($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->getTaskListAssignedToMe($data['userId']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);

}

function getTaskListAssignedByMe($data) {
    $dao = new UsersDetailsDAO();
    $result =  $dao->getTaskListAssignedByMe($data['userId']);
    response($result['statusCode'],$result['status'], $result['message'], $result['data']);
}

?>