<?php 
include ('functions.php');
$Fecha=$_GET['Fecha'];
$json=array();
$json2=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select Patien from Citas where Fecha='$Fecha'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);
	
	$queri2 ="select * from Citas where Fecha= '$Fecha' and Estado='Pendiente' order by Fecha DESC";
	$result = mysqli_query($connect,$queri2);

	$queri3 ="select Nombre,ApellidoP,Correo,Telefono from Pacientes where Id_Paciente = '$row[0]'";
	$resultado3 = mysqli_query($connect,$queri3);
		
	$reg2=mysqli_fetch_array($resultado3);		
			
			
	while ($row2 = mysqli_fetch_array($result)) {
		$reg3=array_merge($row2,$reg2);
			$json['datos'][]=$reg3;
		}
		echo json_encode($json,JSON_UNESCAPED_UNICODE);

	
 ?>