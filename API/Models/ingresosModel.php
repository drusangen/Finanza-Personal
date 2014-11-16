<?php
class ingresosModel extends Model{


	function listar_ultimos_ingresos(){
		$stmt = $this->db->prepare("SELECT idingresos,monto,fecha,nom_cat_ingresos,nom_mediopago 
									FROM ingresos I
									INNER JOIN categoria_ingresos C ON C.idcategoria_ingresos = I.idcategoria_ingresos
									INNER JOIN medio_de_pago M ON M.idmedio_de_pago = I.idmedio_de_pago
									WHERE MONTH(fecha) = MONTH(NOW())
									ORDER BY idingresos DESC ");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}


	function agregar($idcategoria_ingresos,$idusuarios,$idmedio_de_pago,$monto,$fecha){
		$stmt = $this->db->prepare("INSERT INTO ingresos (idcategoria_ingresos,idusuarios,idmedio_de_pago,monto,fecha)
								 	VALUES (?,?,?,?,?)");
		$stmt->bind_param('iiiis',$idcategoria_ingresos,$idusuarios,$idmedio_de_pago,$monto,$fecha); 
		$stmt->execute();
		$insert = $stmt->insert_id;
		$stmt->close();  
		return $insert;
	}

	function ingresos_agrupados(){
		$stmt = $this->db->prepare("SELECT 
										ROUND((SUM(monto) * 100) / (SELECT SUM(monto) as 'monto' FROM ingresos WHERE MONTH(fecha) = MONTH(NOW()) )) AS 'porcentaje',
										nom_cat_ingresos
									FROM ingresos G
									INNER JOIN categoria_ingresos C ON C.idcategoria_ingresos = G.idcategoria_ingresos
									INNER JOIN medio_de_pago M ON M.idmedio_de_pago = G.idmedio_de_pago
									WHERE MONTH(fecha) = MONTH(NOW())
									GROUP BY G.idcategoria_ingresos");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

	function actualizar($idcategoria_ingresos,$idusuarios,$idmedio_de_pago,$monto,$idingresos){
		$stmt = $this->db->prepare("UPDATE ingresos 
									SET idcategoria_ingresos = ?, idmedio_de_pago = ?, monto = ?
									WHERE idusuarios = ? AND idingresos = ? ");
		$stmt->bind_param('iiiii',$idcategoria_ingresos,$idmedio_de_pago,$monto,$idusuarios,$idingresos); 
		return $stmt->execute();
	}

	function eliminar($idingresos){
		$stmt = $this->db->prepare("DELETE FROM ingresos WHERE idingresos = ? ");
		$stmt->bind_param('i',$idingresos); 
		return $stmt->execute();
	}

	function ingresos_vs_gastos($desde,$hasta){
	    $stmt = $this->db->prepare("SELECT ? + INTERVAL a + b DAY fechas,
										(SELECT COALESCE(SUM(monto),0) as 'monto' FROM ingresos WHERE date_format(fecha,'%Y-%m-%d') = fechas ) AS 'monto'
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