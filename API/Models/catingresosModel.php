<?php
class catingresosModel extends Model{


	function listar(){
		$stmt = $this->db->prepare("SELECT * FROM categoria_ingresos ");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

}
?>