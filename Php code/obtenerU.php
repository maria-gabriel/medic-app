<?php 
include ('functions.php');
$Id_Cita=$_GET['Id_Cita'];
$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select Latitud,Longitud from Citas where Id_Cita = '$Id_Cita'";
	$resultado = mysqli_query($connect,$queri);


		if($queri){
			if($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			
			echo json_encode($json,JSON_UNESCAPED_UNICODE);
		}
		
 ?>