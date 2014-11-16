<?php
class usuariosModel extends Model{

	function saldo_ingresos(){
		$stmt = $this->db->prepare("SELECT SUM(monto) AS 'total_ingresos' FROM ingresos WHERE MONTH(fecha) = MONTH(NOW())");
		$stmt->execute();

		$total 	= $stmt->get_result();
		$total  = $total->fetch_array(MYSQLI_ASSOC);

	    $stmt->close();
	    return $total;
	}

	function saldo_gastos(){
		$stmt = $this->db->prepare("SELECT SUM(monto) AS 'total_gasto' FROM gastos WHERE MONTH(fecha) = MONTH(NOW())");
		$stmt->execute();

		$total 	= $stmt->get_result();
		$total  = $total->fetch_array(MYSQLI_ASSOC);

	    $stmt->close();
	    return $total;
	}

	function anios_balance(){
		$stmt = $this->db->prepare("SELECT YEAR(fecha) AS 'anio' FROM (
									    SELECT fecha
									    FROM gastos
									    UNION ALL
									    SELECT fecha
									    FROM ingresos ) AS T
									GROUP BY YEAR(fecha)");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function balance_tabla($anio){
		$stmt = $this->db->prepare("SELECT nro_mes,mes,total_gastos,total_ingresos, (total_ingresos-total_gastos) AS 'resumen'
									FROM(
									    SELECT `key` AS 'nro_mes', mes, IFNULL((SELECT SUM(monto) FROM gastos WHERE month(fecha) = `key` AND year(fecha) = ? GROUP BY month(fecha) ),0) AS 'total_gastos',
									    IFNULL((SELECT SUM(monto) FROM ingresos WHERE month(fecha) = `key` AND year(fecha) = ? GROUP BY month(fecha) ),0) AS 'total_ingresos'
									    FROM mes
									    ORDER BY `key` ASC
									) AS T");
		$stmt->bind_param('ii',$anio,$anio); 
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function buscar($SQL){
		
		
		$stmt = $this->db->prepare("SELECT * FROM(
										SELECT idgastos,monto,fecha,nom_cat_gastos AS 'categoria',nom_mediopago, 'Gastos' AS 'tipo'
										FROM gastos G
										INNER JOIN categoria_gastos C ON C.idcategoria_gastos = G.idcategoria_gastos
										INNER JOIN medio_de_pago M ON M.idmedio_de_pago = G.idmedio_de_pago
										UNION ALL 
										SELECT idingresos,monto,fecha,nom_cat_ingresos AS 'categoria',nom_mediopago, 'Ingresos' AS 'tipo'
										FROM ingresos I
										INNER JOIN categoria_ingresos C ON C.idcategoria_ingresos = I.idcategoria_ingresos
										INNER JOIN medio_de_pago M ON M.idmedio_de_pago = I.idmedio_de_pago
									) AS T
									WHERE 1 ".$SQL."
									ORDER BY fecha DESC
									");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function login($usuario,$contrasena){
		$stmt = $this->db->prepare("SELECT * FROM usuarios WHERE email = ? AND password = md5(?) ");
		$stmt->bind_param('ss',$usuario,$contrasena); 
		$stmt->execute();

		$total 	= $stmt->get_result();
		$total  = $total->fetch_array(MYSQLI_ASSOC);

	    $stmt->close();
	    return $total;
	}

	function comboboxfechasmensual(){
		$stmt = $this->db->prepare("SELECT CONCAT(date_format(fecha,'%Y-%m'),'-01') as 'fecha', DATE_FORMAT(fecha,'%m') as 'mes', DATE_FORMAT(fecha,'%Y') as 'anio'
									FROM gastos
									GROUP BY date_format(fecha,'%Y-%m')");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function editar($nombre,$email,$edad,$tipo,$idUsuario){
		$stmt = $this->db->prepare("UPDATE usuario SET nombre = ?,email = ?,edad = ?,idTipo = ?
									WHERE idUsuario = ? ");
		$stmt->bind_param('ssiii',$nombre,$email,$edad,$tipo,$idUsuario); 
		$stmt->execute();
		$stmt->close();  
		return 1;
	}

	function listarUsuarios(){
		$stmt = $this->db->prepare("SELECT idUsuario,email,nombre,edad,T.descripcion,T.idTipo 
									FROM usuario U 
									INNER JOIN tipo T on T.idTipo = u.idTipo ");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function eliminar($idUsuario){
		$stmt = $this->db->prepare("DELETE FROM usuario WHERE idUsuario = ?");
		$stmt->bind_param('i',$idUsuario); 
		$stmt->execute();
		$stmt->close();
		return 1;
	}
}
?>