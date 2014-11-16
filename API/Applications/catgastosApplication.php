<?php
class catgastos extends Application{
	
	function __construct(){
		parent::__construct();
	}


	function listar(){
		$json_array = null;
		$rs = $this->model->listar();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

}
?>