<?php 
include ('functions.php');

$json=array();
mysqli_set_charset($connect, "utf8");

	$queri ="select * from Citas order by Fecha DESC";
	$resultado = mysqli_query($connect,$queri);


		if($queri){
			while($reg=mysqli_fetch_array($resultado)){
				$json['datos'][]=$reg;
			}
			
			echo json_encode($json,JSON_UNESCAPED_UNICODE);
		}
		
 ?>