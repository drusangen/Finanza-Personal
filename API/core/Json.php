<?php
class Json{

	private $result;

	function __construct(){ 
	}

	function body(){
		require_once _PATH.'/template/json.phtml'; 
	}

	function ajax($data = null,$code = null,$total = null){

		if(is_null($code)){
			$code = 0;
		}

		if(is_null($data)){
			$data = null;
		}

		$data =	array(
					'code' 	=> $code, 
					'data' 	=> $data,
					'total' => $total
				);
		$this->result = json_encode($data);
	}

	function getResult(){
		return $this->result;
	}
}
?>