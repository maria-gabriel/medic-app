<?php 
include ('functions.php');

$Imagen=$_POST['Imagen'];
$Correo=$_POST['Correo'];

	ejecutarSQLCommand("UPDATE Medicos set Token = '$Imagen' where Correo='$Correo'");    

 ?>