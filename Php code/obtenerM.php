<?php 
include ('functions.php');
$Correo=$_GET['Correo'];
$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select * from Medicos where Correo = '$Correo'";
	$resultado = mysqli_query($connect,$queri);


		if($queri){
			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			
			echo json_encode($json,JSON_UNESCAPED_UNICODE);
		}
		
 ?>