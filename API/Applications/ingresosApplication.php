<?php
class ingresos extends Application{
	
	function __construct(){
		parent::__construct();
	}

	function ultimosingresos(){
		$json_array = null;
		$rs = $this->model->listar_ultimos_ingresos();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function ingresosagrupados(){
		$json_array = null;
		$rs = $this->model->ingresos_agrupados();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function agregar(){
		if($_POST){
			
			$idcategoria_ingresos = $_POST['idcategoria_ingresos'];
			$idusuarios 		= $_POST['idusuarios'];
			$idmedio_de_pago 	= $_POST['idmedio_de_pago'];
			$monto 				= $_POST['monto'];
			$fecha 				= date("Y-m-d H:i:s");

			$id = $this->model->agregar($idcategoria_ingresos,$idusuarios,$idmedio_de_pago,$monto,$fecha);
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
			
			$idcategoria_ingresos 	= $_POST['idcategoria_ingresos'];
			$idusuarios 			= $_POST['idusuarios'];
			$idmedio_de_pago 		= $_POST['idmedio_de_pago'];
			$monto 					= $_POST['monto'];
			$idingresos 			= $_POST['idingresos'];

			$id = $this->model->actualizar($idcategoria_ingresos,$idusuarios,$idmedio_de_pago,$monto,$idingresos);
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
			$idingresos 			= $_POST['idingresos'];

			$id = $this->model->eliminar($idingresos);
			if($id > 0){
				$this->json->ajax(null,201);
			}else{
				$this->json->ajax(null,202);
			}
		}else{
			$this->json->ajax(null,202);
		}
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