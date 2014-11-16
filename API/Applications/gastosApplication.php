<?php
class gastos extends Application{
	
	function __construct(){
		parent::__construct();
	}


	function listarpagos(){
		$json_array = null;
		$rs = $this->model->listar_pagos();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function agregar(){
		if($_POST){
			
			$idcategoria_gastos = $_POST['idcategoria_gastos'];
			$idusuarios 		= $_POST['idusuarios'];
			$idmedio_de_pago 	= $_POST['idmedio_de_pago'];
			$monto 				= $_POST['monto'];
			$fecha 				= date("Y-m-d H:i:s");

			$id = $this->model->agregar($idcategoria_gastos,$idusuarios,$idmedio_de_pago,$monto,$fecha);
			if($id > 0){
				$this->json->ajax(null,201);
			}else{
				$this->json->ajax(null,202);
			}
		}else{
			$this->json->ajax(null,202);
		}
	}

	function actualizar(){
		if($_POST){
			
			$idcategoria_gastos = $_POST['idcategoria_gastos'];
			$idusuarios 		= $_POST['idusuarios'];
			$idmedio_de_pago 	= $_POST['idmedio_de_pago'];
			$monto 				= $_POST['monto'];
			$idgastos 			= $_POST['idgastos'];

			$id = $this->model->actualizar($idcategoria_gastos,$idusuarios,$idmedio_de_pago,$monto,$idgastos);
			if($id > 0){
				$this->json->ajax(null,201);
			}else{
				$this->json->ajax(null,202);
			}
		}else{
			$this->json->ajax(null,202);
		}
	}

	function eliminar(){
		if($_POST){
			$idgastos 			= $_POST['idgastos'];

			$id = $this->model->eliminar($idgastos);
			if($id > 0){
				$this->json->ajax(null,201);
			}else{
				$this->json->ajax(null,202);
			}
		}else{
			$this->json->ajax(null,202);
		}
	}

	function ultimosgastos(){
		$json_array = null;
		$rs = $this->model->listar_ultimos_gastos();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function gastosagrupados(){
		$json_array = null;
		$rs = $this->model->gastos_agrupados();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function ingresosvsgastos(){
		if($_POST){

			$desde = $_POST['fecha'];
			$hasta = strtotime ( '+1 month' , strtotime ( $desde ) ) ;
			$hasta = date ( 'Y-m-d' , $hasta );


			$json_array = null;
			$rs = $this->model->ingresos_vs_gastos($desde,$hasta);
			while($row = $rs->fetch_array(MYSQL_ASSOC)) {
	            $json_array[] = $row;
		    }

		    $this->json->ajax($json_array,201);

		}else{
			$this->json->ajax(null,202);
		}

	}

}
?>