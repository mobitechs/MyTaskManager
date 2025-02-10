<?php
class EmailGenarator {
    
		private $to;
		private $from;
		private $message;
		private $subject;
		
		
		public function setTo($to) {
			$this->to = $to;
		}
		
		public function getTo() {
			return $this->to;
		}
		
		public function setFrom($from) {
			$this->from = $from;
		}
		
		public function getFrom() {
			return $this->from;
		}
		
		public function setMessage($message) {
			$this->message = $message;
		}
		
		public function getMessage() {
			return $this->message;
		}
		
		public function setSubject($subject) {
			$this->subject = $subject;
		}
		
		public function getSubject() {
			return $this->subject;
		}
		
		function sendEmail() {
			$success = mail($this->to,$this->subject,$this->message,$this->from);
			return $success;			
		}
	}
?>