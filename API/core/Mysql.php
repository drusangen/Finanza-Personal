<?php 
class Mysql extends Mysqli{

    protected static $instance;
    private function __construct(){ 
        parent::__construct(DB_HOST,DB_USER,DB_PASS,DB_NAME);
        if(mysqli_connect_errno()){
            trigger_error("Datos de conexión son incorrectos!", E_USER_NOTICE);
            exit;
        }
    }

    public static function getInstance(){
        if(!self::$instance){
            self::$instance = new self();
        }
        return self::$instance;
    }

    public function query($query){
        if($this->real_query($query)) {
            if ($this->field_count > 0) {
                return new mysqli_result($this);
            }
            return true;
        }
    }

    public function ultimo_insert(){
        return $this->insert_id;
    }

    public function params($var){
            $var = preg_replace("/[^A-ZñÑa-z0-9\s]/", "", $var);
            return $var;
    }


    public function __destruct(){
        $this->close();
    }

}
?>