<?php
include ('functions.php');
$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select * from Medicos ORDER BY TotalCitas DESC";
	$resultado = mysqli_query($connect,$queri);

	$queri2 ="SELECT COUNT(*) as Medicos from medicos";
	$resultado2 = mysqli_query($connect,$queri2);

	$queri3 ="SELECT COUNT(*) as Pacientes from pacientes";
	$resultado3 = mysqli_query($connect,$queri3);

	$reg2=mysqli_fetch_array($resultado2);
	$reg3=mysqli_fetch_array($resultado3);

		while($reg=mysqli_fetch_array($resultado) ){
			$reg4 = array_merge($reg, $reg2);
			$reg5 = array_merge($reg4,$reg3);
			$json['datos'][]=$reg5;
		}
			
			echo json_encode($json,JSON_UNESCAPED_UNICODE);
 ?>
