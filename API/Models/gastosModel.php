<?php
class gastosModel extends Model{


	function listar_pagos(){
		$stmt = $this->db->prepare("SELECT * FROM medio_de_pago ");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function agregar($idcategoria_gastos,$idusuarios,$idmedio_de_pago,$monto,$fecha){
		$stmt = $this->db->prepare("INSERT INTO gastos (idcategoria_gastos,idusuarios,idmedio_de_pago,monto,fecha)
								 	VALUES (?,?,?,?,?)");
		$stmt->bind_param('iiiis',$idcategoria_gastos,$idusuarios,$idmedio_de_pago,$monto,$fecha); 
		$stmt->execute();
		$insert = $stmt->insert_id;
		$stmt->close();  
		return $insert;
	}

	function actualizar($idcategoria_gastos,$idusuarios,$idmedio_de_pago,$monto,$idgastos){
		$stmt = $this->db->prepare("UPDATE gastos 
									SET idcategoria_gastos = ?, idmedio_de_pago = ?, monto = ?
									WHERE idusuarios = ? AND idgastos = ? ");
		$stmt->bind_param('iiiii',$idcategoria_gastos,$idmedio_de_pago,$monto,$idusuarios,$idgastos); 
		return $stmt->execute();
	}

	function eliminar($idgastos){
		$stmt = $this->db->prepare("DELETE FROM gastos WHERE idgastos = ? ");
		$stmt->bind_param('i',$idgastos); 
		return $stmt->execute();
	}

	function listar_ultimos_gastos(){
		$stmt = $this->db->prepare("SELECT idgastos,monto,fecha,nom_cat_gastos,nom_mediopago 
									FROM gastos G
									INNER JOIN categoria_gastos C ON C.idcategoria_gastos = G.idcategoria_gastos
									INNER JOIN medio_de_pago M ON M.idmedio_de_pago = G.idmedio_de_pago
									WHERE MONTH(fecha) = MONTH(NOW())
									ORDER BY idgastos DESC ");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function gastos_agrupados(){
		$stmt = $this->db->prepare("SELECT 
										ROUND((SUM(monto) * 100) / (SELECT SUM(monto) as 'monto' FROM gastos WHERE MONTH(fecha) = MONTH(NOW()) )) AS 'porcentaje',
										nom_cat_gastos
									FROM gastos G
									INNER JOIN categoria_gastos C ON C.idcategoria_gastos = G.idcategoria_gastos
									INNER JOIN medio_de_pago M ON M.idmedio_de_pago = G.idmedio_de_pago
									WHERE MONTH(fecha) = MONTH(NOW())
									GROUP BY G.idcategoria_gastos");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function ingresos_vs_gastos($desde,$hasta){
		$stmt = $this->db->prepare("SELECT ? + INTERVAL a + b DAY fechas,
										(SELECT COALESCE(SUM(monto),0) as 'monto' FROM gastos WHERE date_format(fecha,'%Y-%m-%d') = fechas ) AS 'monto'
									FROM
									 (SELECT 0 a UNION SELECT 1 a UNION SELECT 2 UNION SELECT 3
									    UNION SELECT 4 UNION SELECT 5 UNION SELECT 6 UNION SELECT 7
									    UNION SELECT 8 UNION SELECT 9 ) d,
									 (SELECT 0 b UNION SELECT 10 UNION SELECT 20 UNION SELECT 30) m
									WHERE ? + INTERVAL a + b DAY  <  ?
									ORDER BY a + b");
		$stmt->bind_param('sss',$desde,$desde,$hasta); 
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

}
?>