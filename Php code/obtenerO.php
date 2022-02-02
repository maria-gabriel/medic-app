<?php 
include ('functions.php');
$Correo=$_GET['Correo'];
$json=array();
$json2=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select Id_Paciente from pacientes where Correo='$Correo'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);
	
	$queri2 ="select * from Observaciones where Patien= '$row[0]'";
	$result = mysqli_query($connect,$queri2);	
			
			
	while ($row2 = mysqli_fetch_array($result)) {
			$json['datos'][]=$row2;
		}
		echo json_encode($json,JSON_UNESCAPED_UNICODE);

	
 ?>