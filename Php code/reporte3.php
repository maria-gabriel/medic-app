<?php 
include ('functions.php');

$json=array();
mysqli_set_charset($connect, "utf8");

	$mes=date("n");
	$queri ="SELECT COUNT(*) from pacientes";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);

	$queri2 ="SELECT Max(Id_Paciente) from pacientes";
	$resultado2 = mysqli_query($connect,$queri2);
	$row2 = mysqli_fetch_array($resultado2);
	
	for ($i=1; $i<$row2[0]+1;$i++) { 
		
		$queri3 ="SELECT COUNT(*) FROM pacientes,citas WHERE pacientes.Id_Paciente=$i and citas.Patien=$i and Month(Fecha)='$mes'";
		$resultado3 = mysqli_query($connect,$queri3);
		$row3 = mysqli_fetch_array($resultado3);
		 
		 ejecutarSQLCommand("UPDATE Pacientes set NumCitas = '$row3[0]' where Id_Paciente=$i"); 
			
	}
	
 ?>