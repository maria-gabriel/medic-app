<?php 
include ('functions.php');
$Correo=$_GET['Correo'];
$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select Id_Medico from Medicos where Correo='$Correo'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);
	
	$queri2 ="select * from Citas where Doct= '$row[0]' order by Fecha DESC";
	$result = mysqli_query($connect,$queri2);	
			
			
	while ($row2 = mysqli_fetch_array($result)) {
			$json['datos'][]=$row2;
		}
		echo json_encode($json,JSON_UNESCAPED_UNICODE);

	
 ?>
