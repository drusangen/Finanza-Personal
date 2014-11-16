<?php
class Application{

	function __construct(){
		$this->json = new Json();
	}

	public function Model($param){
		$dir = 'Models/'.$param.'Model.php';
		if(file_exists($dir)){
			require_once $dir;
			$modelName = $param . 'Model';
			$this->model = new $modelName();
			
		}
	}
}
?>