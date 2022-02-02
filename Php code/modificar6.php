<?php 
include ('functions.php');

$Correo=$_GET['Correo'];
$CorreoN=$_POST['Correo'];
$Telefono=$_POST['Telefono'];
$Edad=$_POST['Edad'];
$Contra=$_POST['Contra'];

	$queri ="select Id_Paciente from Pacientes where Correo='$Correo'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);
    
	ejecutarSQLCommand("UPDATE Pacientes set Correo = '$CorreoN' where Id_Paciente='$row[0]'");
	ejecutarSQLCommand("UPDATE Pacientes set Telefono = '$Telefono' where Id_Paciente='$row[0]'");
	ejecutarSQLCommand("UPDATE Pacientes set Edad = '$Edad' where Id_Paciente='$row[0]'");
	ejecutarSQLCommand("UPDATE Pacientes set Contra = '$Contra' where Id_Paciente='$row[0]'");

 ?>