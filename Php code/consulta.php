<?php include ('functions.php');

$nombre_contacto=$_GET['nombre'];
$correo=$_GET['correo'];
if($resultset=getSQLResultSet("SELECT Token from contacto_familiar where Nombre_contacto='$nombre_contacto' and Correo='$correo'")){	
		$json= $resultset->fetch_array(MYSQLI_NUM);
		echo json_encode($json);
}
?>