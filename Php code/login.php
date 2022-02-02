<?php 
include ('functions.php');

$Correo=$_GET['Correo'];
$Contra=$_GET['Contra'];
$json=array();
$json2=array();

	$queri ="select Correo,Contra,Tipo from Pacientes where Correo='$Correo' and Contra='$Contra'";
	$resultado = mysqli_query($connect,$queri);
	$queri2 ="select Correo,Contra,Tipo from Medicos where Correo='$Correo' and Contra='$Contra'";
	$resultado2 = mysqli_query($connect,$queri2);

		if($queri){
			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
				echo json_encode($json);
			}
			
			}
			if($queri2){
				if($reg2=mysqli_fetch_array($resultado2)){
				$json2['datos'][]=$reg2;
				echo json_encode($json2);
				}
				
			
		}
 ?>