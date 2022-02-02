<?php 
include ('functions.php');

$Correo=$_GET['Correo'];
$CorreoN=$_POST['Correo'];
$Telefono=$_POST['Telefono'];
$Contra=$_POST['Contra'];

	$queri ="select Id_Medico from Medicos where Correo='$Correo'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);
    
	ejecutarSQLCommand("UPDATE Medicos set Correo = '$CorreoN' where Id_Medico='$row[0]'");
	ejecutarSQLCommand("UPDATE Medicos set Telefono = '$Telefono' where Id_Medico='$row[0]'");
	ejecutarSQLCommand("UPDATE Medicos set Contra = '$Contra' where Id_Medico='$row[0]'");

 ?>