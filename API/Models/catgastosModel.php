<?php
class catgastosModel extends Model{


	function listar(){
		$stmt = $this->db->prepare("SELECT * FROM categoria_gastos ");
		$stmt->execute();
		$total 	= $stmt->get_result();
	    $stmt->close();
	    return $total;
	}

}
?>