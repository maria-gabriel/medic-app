<?php 
include ('functions.php');
$Correo=$_GET['Correo'];
$json=array();
$json2=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select Id_Paciente from pacientes where Correo='$Correo'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);
	
	$queri2 ="select Id_Cita,Sintomas,Hora,Fecha,Estado from Citas where Patien= '$row[0]' order by Fecha DESC";
	$result = mysqli_query($connect,$queri2);

	$queri3 ="select Nombre,ApellidoP from Pacientes where Correo = '$Correo'";
	$resultado3 = mysqli_query($connect,$queri3);
		
	$reg2=mysqli_fetch_array($resultado3);		
			
			
	while ($row2 = mysqli_fetch_array($result)) {
		$reg3=array_merge($row2,$reg2);
			$json['datos'][]=$reg3;
		}
		echo json_encode($json,JSON_UNESCAPED_UNICODE);

	
 ?>