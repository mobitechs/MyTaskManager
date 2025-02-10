<?php
// class BaseDAO {


// 	private $db_host = 'localhost'; //hostname
//     private $db_user = 'bonsirpq_my_task_manager'; // username
//     private $db_password = 'MyTaskManager@10'; // password
//     private $db_name = 'bonsirpq_my_task_manager'; //database name

// // for localhost
// 	// private $db_host = 'localhost'; //hostname    
//     // private $db_user = 'root'; // username
//     // private $db_password = ''; // password
//     // private $db_name = 'bonsirpq_my_task_manager'; //database name
	

		
// 		private $imagePathURL =  "https://mobitechs.in/MyTaskMangerAPI/images/";
//     private $con = null;


//     private $googleAPIKey = 'AIzaSyCmwKfKS6M75W814jOQ0r3o8bpVdYCoD8A';

//     public function getConnection() {
    
//         $this->con=mysqli_connect($this->db_host,$this->db_user,$this->db_password,$this->db_name) or die("Failed to connect to MySQL:".mysql_error());
        


//         if (mysqli_connect_errno()) {
//             echo "Failed to connect to MySQL: " . mysqli_connect_error();
//         }
//         return $this->con;
//     }

//     public function getGoogleAPIKey() {
//         return $this->googleAPIKey;
//     }
//     public function getimagePathURL() {
//         return $this->imagePathURL;
//     }
// }


class BaseDAO {
    private $host = "localhost"; // Example: "localhost"
    private $username = "bonsirpq_my_task_manager";
    private $password = "MyTaskManager@10";
    private $dbname = "bonsirpq_my_task_manager";
    public $con;

    public function __construct() {
        $this->con = new mysqli($this->host, $this->username, $this->password, $this->dbname);

        if ($this->con->connect_error) {
            die("Database Connection Failed: " . $this->con->connect_error);
        }
    }

    public function getConnection() {
        return $this->con;
    }
}

?>
