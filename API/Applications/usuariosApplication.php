<?php
class usuarios extends Application{
	
	function __construct(){
		parent::__construct();
	}

	function saldos(){
		$rs_ingresos = $this->model->saldo_ingresos();
		$rs_gastos 	 = $this->model->saldo_gastos();

		$json_array[] = array('total_ingresos' => $rs_ingresos['total_ingresos'], 'total_gastos' => $rs_gastos['total_gasto']);
		$this->json->ajax($json_array,201);
	}

	function comboboxfechasmensual(){
		$json_array = null;
		$rs = $this->model->comboboxfechasmensual();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function anios(){
		$json_array = null;
		$rs = $this->model->anios_balance();
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function balance(){
		$json_array = null;
		if($_POST){

			$anio = $_POST['anio'];
			$rs = $this->model->balance_tabla($anio);
			while($row = $rs->fetch_array(MYSQL_ASSOC)) {
	            $json_array[] = $row;
		    }

		    $this->json->ajax($json_array,201);

		}else{
			$this->json->ajax(null,202);
		}
	}

	function buscar(){
		$json_array = null;
		$sql = null;
		if($_POST){
			
			$tipo = $_POST['tipo'];
			$pago = $_POST['pago'];
			$desde = $_POST['desde'];
			$hasta = $_POST['hasta'];

			if($tipo != "Todo"){
				$sql .= " AND tipo = '$tipo' ";
			}

			if($pago != "Todo"){
				$sql .= " AND nom_mediopago = '$pago' ";
			}


			if($desde != "" && $hasta != ""){
				$sql .= "AND fecha BETWEEN '$desde' AND '$hasta' ";
			}else if(!empty($desde)){
				$sql .= " AND fecha >= '$desde' ";
			}else if(!empty($hasta)){
				$sql .= " AND fecha <= '$hasta' ";
			}

		}
		$rs = $this->model->buscar($sql);
		while($row = $rs->fetch_array(MYSQL_ASSOC)) {
            $json_array[] = $row;
	    }

	    $this->json->ajax($json_array,201);
	}

	function login(){
		if($_POST){
			
			$usuario 		= $_POST['usuario'];
			$contrasena 	= $_POST['contrasena'];

			$rs = $this->model->login($usuario,$contrasena);
			if($rs != null){
				$json_array[] = array('id' => $rs['idusuarios'], 'email' => $rs['email'], 'nombre' => $rs['nombre']);
				$this->json->ajax($json_array,201);
			}else{
				$this->json->ajax(null,202);
			}
			
			
		}else{
			$this->json->ajax(null,202);
		}
	}


	function editar(){
		if($_POST){
			
			$idUsuario 	= $_POST['idUsuario'];
			$nombre 	= $_POST['nombre'];
			$email 		= $_POST['email'];
			$edad 		= $_POST['edad'];
			$tipo 		= $_POST['tipo'];

			$id = $this->model->editar($nombre,$email,$edad,$tipo,$idUsuario);
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
			$rs = $this->model->eliminar($_POST['idUsuario']);
			if(is_numeric($rs)){
				$this->json->ajax(null,201);
			}else{
				$this->json->ajax(null,202);
			}
		}else{
			$this->json->ajax(null,202);
		}
	}
}
?>