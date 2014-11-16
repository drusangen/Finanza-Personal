<?php
require_once 'Mysql.php';

class Model{
	function __construct(){
		$this->db = Mysql::getInstance();
		$this->db->set_charset("utf8");	
	}
}
?>