<?php
class RandomNoGenarator {
    public function GenarateCode($str){ 
    
        $TotalCharacters = 'ABCDEFGHIJKLMPQRSTUVXWYZ123456789'; 
        $characterLength = strlen($TotalCharacters); 
        $characterLength--; 
        $genaratedcode=NULL;
         
            for($x=1; $x<=$str; $x++){ 
                $genarateRandomChar = rand(0,$characterLength); 
                $genaratedcode .= substr($TotalCharacters,$genarateRandomChar,1); 
            } 
        return $genaratedcode; 
    }
}
?>