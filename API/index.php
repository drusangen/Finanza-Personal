<?php
session_start();
require_once 'core/HandlerError.php';
require_once 'config.php';
function __autoload($class) {
	
	$file = LIBS . $class .".php";
	if(file_exists($file)){
		require $file;
	}
}
$core = new Core();
?>