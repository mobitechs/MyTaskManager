<?php
error_reporting(E_ALL);
ini_set('display_errors', 1);

require_once 'BaseDAO.php';
require_once '../model/NewRegistrationEmails.php';
require_once '../model/SendSMS.php';
require_once '../model/UsersDetailsEmails.php';

class UsersDetailsDAO {
    private $con;

    public function __construct() {
        $baseDAO = new BaseDAO();
        $this->con = $baseDAO->getConnection();
        mysqli_set_charset($this->con, 'utf8'); // For Hindi content
    }

    public function userRegister($name, $email, $phone, $password) {
        try {
            $sql = "SELECT userId FROM users WHERE email = ? OR phone = ?";
            $stmt = $this->con->prepare($sql);
            $stmt->bind_param("ss", $email, $phone);
            $stmt->execute();
            $stmt->store_result();

            if ($stmt->num_rows > 0) {
                 return ["statusCode" => 401, "status" => "error", "message" => "ser already exists", "data" => null];
            }

            $sql = "INSERT INTO users (name, email, phone, password) VALUES (?, ?, ?, ?)";
            $stmt = $this->con->prepare($sql);
            $stmt->bind_param("ssss", $name, $email, $phone, $password);

            if ($stmt->execute()) {
                $userId = $stmt->insert_id;
                $sql = "SELECT * FROM users WHERE userId = ?";
                $stmt = $this->con->prepare($sql);
                $stmt->bind_param("i", $userId);
                $stmt->execute();
                $result = $stmt->get_result();
                $userData = $result->fetch_assoc();

                $sendNewRegistrationemailIds = new NewRegistrationEmails();
                $sendNewRegistrationemailIds->SendNewUserRegistrationEmail($name, $email, $phone, $userId);

                return ["statusCode" => 200, "status" => "success", "message" => "User Registered Successfully", "data" => $userData];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Registration failed", "data" => null];
            }
        } catch (Exception $e) {
             return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function userLogin($userLoginId, $password) {
    try {
        $sql = "SELECT * FROM users WHERE (email = ? OR phone = ?) AND password = ?";
        $stmt = $this->con->prepare($sql);
        $stmt->bind_param("sss", $userLoginId, $userLoginId, $password);
        $stmt->execute();
        $result = $stmt->get_result();

        if ($result->num_rows > 0) {
            $userData = [];
            while ($row = $result->fetch_assoc()) {
                $userData[] = $row;
            }
            return ["statusCode" => 200, "status" => "success", "message" => "Login Success", "data" => $userData];
        } else {
            return ["statusCode" => 401, "status" => "error", "message" => "Login Failed", "data" => null];
        }
    } catch (Exception $e) {
        return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
    }
}

    public function forgotPassword($email) {
        try {
            $sql = "SELECT * FROM users WHERE email = ?";
            $stmt = $this->con->prepare($sql);
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $result = $stmt->get_result();

            if ($result->num_rows == 1) {
                $resetPassword = new UsersDetailsEmails();
                 return ["statusCode" => 200, "status" => "success", "message" => "Valid Email", "data" => $resetPassword->GenarateOTPForForgoPassword($email)];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Email not found", "data" => null];
            }
        } catch (Exception $e) {
             return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }
    public function SavingOTPToUserDetails($generatedRandomNo, $email) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "UPDATE users SET otp = ? WHERE email = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("ss", $generatedRandomNo, $email);
            if ($stmt->execute()) {
                 return ["RANDOM_NO_SUCCESSFULLY_UPDATED"];
            } else {            
                return ["RANDOM_NO_NOT_UPDATED"];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function setPassword($email, $otp, $password) {
        try {
            $sql = "UPDATE users SET password = ? WHERE email = ? AND otp = ?";
            $stmt = $this->con->prepare($sql);
            $stmt->bind_param("sss", $password, $email, $otp);
            $stmt->execute();

            if ($stmt->affected_rows > 0) {
                 return ["statusCode" => 200, "status" => "success", "message" => "Password Set Successfully", "data" => null];
            } else {            
                return ["statusCode" => 401, "status" => "error", "message" => "Enter Valid OTP", "data" => null];
            }
        } catch (Exception $e) {
             return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function getOTPOnEmail($email) {
        try {
            $sql = "SELECT * FROM users WHERE email = ?";
            $stmt = $this->con->prepare($sql);
            $stmt->bind_param("s", $email);
            $stmt->execute();
            $result = $stmt->get_result();

            if ($result->num_rows == 1) {
                $resetPassword = new UsersDetailsEmails();
                return ["statusCode" => 200, "status" => "success", "message" => "Valid Email", "data" => $resetPassword->GenarateOTPForForgoPassword($email)];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Invalid Email", "data" => null];
            }
        } catch (Exception $e) {
             return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function getUserDetails($userId) {
        try {
            $sql = "SELECT * FROM users WHERE userId = ?";
            $stmt = $this->con->prepare($sql);
            $stmt->bind_param("i", $userId);
            $stmt->execute();
            $result = $stmt->get_result();

            if ($result->num_rows == 1) {
                return ["statusCode" => 200, "status" => "success", "message" => "User Details", "data" =>  $result->fetch_assoc()];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "User details not available", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => 'SQL Exception: ' . $e->getMessage()];
        }
    }

    public function createTeam($teamName, $description, $image, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "SELECT teamId FROM teamDetails WHERE teamName = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("s", $teamName);
            $stmt->execute();
            $stmt->store_result();
            
            if ($stmt->num_rows > 0) {
                return ["statusCode" => 401, "status" => "error", "message" => "Team already exists", "data" => null];
            }
            
            $sql = "INSERT INTO teamDetails (teamName, description, image, updatedBy) VALUES (?, ?, ?, ?)";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("ssss", $teamName, $description, $image, $updatedBy);
            if ($stmt->execute()) { 
                return ["statusCode" => 200, "status" => "success", "message" => "Team created successfully", "data" => null];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Team creation failed", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function editTeam($teamId, $teamName, $description, $image, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "SELECT teamId FROM teamDetails WHERE teamName = ? AND teamId != ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("si", $teamName, $teamId);
            $stmt->execute();
            $stmt->store_result();
            
            if ($stmt->num_rows > 0) {
                return ["statusCode" => 401, "status" => "error", "message" => "Team name already exists", "data" => null];
            }
            
            $sql = "UPDATE teamDetails SET teamName = ?, description = ?, image = ?, updatedBy = ? WHERE teamId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("ssssi", $teamName, $description, $image, $updatedBy, $teamId);
            if ($stmt->execute()) { 
                return ["statusCode" => 200, "status" => "success", "message" => "Team updated successfully", "data" => null];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to update team details", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function deleteTeam($teamId) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "DELETE FROM teamDetails WHERE teamId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("i", $teamId);
            if ($stmt->execute()) { 
                return ["statusCode" => 200, "status" => "success", "message" => "Team deleted successfully", "data" => null];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to delete team", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function addTeamMember($teamId, $teamMemberId, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "SELECT teamId FROM teamMemberDetails WHERE teamId = ? AND teamMemberId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("ii", $teamId, $teamMemberId);
            $stmt->execute();
            $stmt->store_result();
            
            if ($stmt->num_rows > 0) {
                return ["statusCode" => 401, "status" => "error", "message" => "Member already exists", "data" => null];
            }
            
            $sql = "INSERT INTO teamMemberDetails (teamId, teamMemberId, updatedBy) VALUES (?, ?, ?)";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("iis", $teamId, $teamMemberId, $updatedBy);
            if ($stmt->execute()) {
                return ["statusCode" => 200, "status" => "success", "message" => "Member added successfully", "data" => null];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to add member", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function deleteTeamMember($teamId, $teamMemberId) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "DELETE FROM teamMemberDetails WHERE teamId = ? AND teamMemberId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("ii", $teamId, $teamMemberId);
            if ($stmt->execute()) {
                return ["statusCode" => 200, "status" => "success", "message" => "Team member deleted successfully", "data" => null];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to delete team member", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function addTask($taskName, $taskDescription, $kpi, $ownerId, $assigneeId, $teamId, $expectedDate, $status, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "INSERT INTO taskDetails (taskName, taskDescription, kpi, ownerId, assigneeId, teamId, expectedDate, status, updatedBy) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("sssiiisss", $taskName, $taskDescription, $kpi, $ownerId, $assigneeId, $teamId, $expectedDate, $status, $updatedBy);
            if ($stmt->execute()) {
                return ["statusCode" => 200, "status" => "success", "message" => "Task added successfully", "data" => null];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to add task", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function editTask($taskId, $taskName, $taskDescription, $kpi, $ownerId, $assigneeId, $teamId, $expectedDate, $status, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "UPDATE taskDetails SET taskName = ?, taskDescription = ?, kpi = ?, ownerId = ?, assigneeId = ?, teamId = ?, expectedDate = ?, status = ?, updatedBy = ? WHERE taskId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("sssiiisssi", $taskName, $taskDescription, $kpi, $ownerId, $assigneeId, $teamId, $expectedDate, $status, $updatedBy, $taskId);
            if ($stmt->execute() && $stmt->affected_rows > 0) {
                return ["statusCode" => 200, "status" => "success", "message" => "Task details updated successfully", "data" => null];
            } else {
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to update task details", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function deleteTask($taskId) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "DELETE FROM taskDetails WHERE taskId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("i", $taskId);
            if ($stmt->execute()) {
                return ["statusCode" => 200, "status" => "success", "message" => "Task deleted successfully", "data" => null];
            } else {                
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to delete task", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }
    public function updateStatus($taskId, $status, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "UPDATE taskDetails SET status = ?, updatedBy = ? WHERE taskId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("ssi", $status, $updatedBy, $taskId);
            if ($stmt->execute() && $stmt->affected_rows > 0) {
                return ["statusCode" => 200, "status" => "success", "message" => "Task status updated successfully", "data" => null];
            } else {                
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to update task status", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function addReminderForTask($taskId, $noOfReminder, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "UPDATE taskDetails SET noOfReminder = ?, updatedBy = ? WHERE taskId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("isi", $noOfReminder, $updatedBy, $taskId);
            if ($stmt->execute() && $stmt->affected_rows > 0) {
                return ["statusCode" => 200, "status" => "success", "message" => "Task reminder updated successfully", "data" => null];
            } else {                
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to update task reminder", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function addComment($taskId, $comment, $expectedDate, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "UPDATE taskDetails SET comment = ?, expectedDate = ?, updatedBy = ? WHERE taskId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("sssi", $comment, $expectedDate, $updatedBy, $taskId);
            if ($stmt->execute() && $stmt->affected_rows > 0) {
                return ["statusCode" => 200, "status" => "success", "message" => "Comment added successfully", "data" => null];
            } else {                
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to add comment", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function editComment($taskId, $comment, $expectedDate, $updatedBy) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "UPDATE taskDetails SET comment = ?, expectedDate = ?, updatedBy = ? WHERE taskId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("sssi", $comment, $dueDate, $updatedBy, $taskId);
            if ($stmt->execute() && $stmt->affected_rows > 0) {
                return ["statusCode" => 200, "status" => "success", "message" => "Comment updated successfully", "data" => null];
            } else {                
                return ["statusCode" => 401, "status" => "error", "message" => "Failed to update comment", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function getTaskListAssignedByMe($userId) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "SELECT * FROM task WHERE taskAssignerId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("i", $userId);
            $stmt->execute();
            $result = $stmt->get_result();
            if ($result->num_rows > 0) {
                $tasks = [];
                while ($row = $result->fetch_assoc()) {
                    $tasks[] = $row;
                }
                return ["statusCode" => 200, "status" => "success", "message" => "Task List", "data" => $tasks];
            } else {                
                return ["statusCode" => 401, "status" => "error", "message" => "Task not available", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

    public function getTaskListAssignedToMe($userId) {
        try {
            $baseDAO = new BaseDAO();
            $con = $baseDAO->getConnection();
            $sql = "SELECT * FROM task WHERE assigneeId = ?";
            $stmt = $con->prepare($sql);
            $stmt->bind_param("i", $userId);
            $stmt->execute();
            $result = $stmt->get_result();
            if ($result->num_rows > 0) {
                $tasks = [];
                while ($row = $result->fetch_assoc()) {
                    $tasks[] = $row;
                }            
                return ["statusCode" => 200, "status" => "success", "message" => "Task List", "data" => $tasks];
            } else {            
                return ["statusCode" => 401, "status" => "error", "message" => "Task not available", "data" => null];
            }
        } catch (Exception $e) {
            return ["statusCode" => 500, "status" => "error", "message" => "SQL Exception: " . $e->getMessage(), "data" => null];
        }
    }

}
?>