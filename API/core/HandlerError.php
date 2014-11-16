<?php
set_error_handler('ErrorHandler');
register_shutdown_function('ShutdownHandler');

function ErrorHandler($code, $message, $file, $line) {
 	header('Content-Type: application/json');
	echo json_encode(array(
						'Code' 		=> $code,
						'Message' 	=> $message,
						'File' 		=> $file,
						'Line'		=> $line
					),JSON_PRETTY_PRINT);
}

function ShutdownHandler()
{
	$error = error_get_last();
	$error['type'];
	if (!empty($error['type'])) {
	    ErrorHandler(E_ERROR, $error['message'], $error['file'], $error['line']);
	    exit;
	}
}


?>