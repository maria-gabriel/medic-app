<?php 
include ('functions.php');

$Id_Cita=$_GET['Id_Cita'];
$json=array();
mysqli_set_charset($connect, "utf8");
    
	$queri ="select Id_Medico from cita_medico_paciente where Id_Cita='$Id_Cita'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);  

	$queri3 ="Select Nombre,ApellidoP,Telefono,Correo from Medicos where Id_Medico='$row[0]'";
	$result = mysqli_query($connect,$queri3);
				
			
	while ($row2 = mysqli_fetch_array($result)) {
			$json['datos'][]=$row2;
		}
		echo json_encode($json,JSON_UNESCAPED_UNICODE); 

 ?>