<?php

class Core{
	function __construct(){
		self::boot();
	}

	private function boot(){
        $url = self::htaccess();
        if(empty($url[0])){
                require_once _PATH.'/template/index.phtml';
                exit;
        }

        $application = array_slice($url, 0, 1);
        if(!self::file_applications($application[0])){
                $parameter 	= array_slice($url, 1, count($url));
                self::router($application[0],$parameter);
        }else{
                self::error();
        }
	}

	private function error(){
		echo "application null";
	}


	private function file_applications($application){
        $file = _PATH.'/Applications/'.$application.'Application.php';
        if(file_exists($file)){
            require_once $file;
        }else{ return true; }
		
	}

	private function router($application,$param){
		
		$object = new $application;
		$object->Model($application);

		$count = count($param); 

		switch ($count) {
			case 5:
				$object->{$param[0]}($param[1],$param[2],$param[3],$param[4]);
			break;

			case 4:
				$object->{$param[0]}($param[1],$param[2],$param[3]);
			break;

			case 3:
				$object->{$param[0]}($param[1],$param[2]);
			break;

			case 2:
				if(method_exists($object, $param[0])){
					$object->{$param[0]}($param[1]);
				}else{
					self::error();
				}
			break;

			case 1:
				if(method_exists($object, $param[0])){
					$object->{$param[0]}();
				}else{
					self::error();
				}
			break;
			
			default:
				$object->index();
			break;
		}
		$object->json->body();
		
	}

	private function htaccess(){
		$url = isset($_GET['json']) ? $_GET['json'] : null;
		$url = rtrim($url, '/');
		$url = explode('/', $url);
		return $url;
	}

}
?>