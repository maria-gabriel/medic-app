<?php 
include ('functions.php');

$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="SELECT COUNT(*) from medicos";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);

	$queri2 ="SELECT Max(Id_Medico) from medicos";
	$resultado2 = mysqli_query($connect,$queri2);
	$row2 = mysqli_fetch_array($resultado2);
	
	for ($i=1; $i<$row2[0]+1;$i++) { 
		
		$queri3 ="SELECT COUNT(*) FROM medicos,citas WHERE medicos.Id_Medico=$i and citas.Doct=$i and citas.estado='Finalizada'";
		$resultado3 = mysqli_query($connect,$queri3);
		$row3 = mysqli_fetch_array($resultado3);
		 
		 ejecutarSQLCommand("UPDATE Medicos set TotalCitas = '$row3[0]' where Id_Medico=$i"); 
			
	}
	
 ?>