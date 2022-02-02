<?php 
include ('functions.php');
$Correo=$_GET['Correo'];
$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="Select Id_Ficha from Paciente_Ficha where Id_Paciente=(select Id_Paciente from pacientes where Correo='$Correo')";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);
	
	$queri2 ="select Curp,FechaNac,TipoSan,Genero,EstadoCiv,Nacionalidad,Familiar,Parent,Telefon from Fichas,Paciente_Ficha where Fichas.Id_Ficha= '$row[0]'";

	$resultado2 = mysqli_query($connect,$queri2);


		if($queri2){
			if($reg=mysqli_fetch_array($resultado2)){
				$json['datos'][]=$reg;
			}
			
			echo json_encode($json,JSON_UNESCAPED_UNICODE);
		}
		
 ?>