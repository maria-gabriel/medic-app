<?php 
include ('functions.php');

$Correo=$_POST['Correo'];

$queri1 ="select Id_Paciente from Pacientes where Correo='$Correo'";
$queri2 ="select max(Id_Cita) from Citas";
	$resultado1 = mysqli_query($connect,$queri1);
	$resultado2 = mysqli_query($connect,$queri2);
    $row = mysqli_fetch_array($resultado1);
    $row2 = mysqli_fetch_array($resultado2);
    
	ejecutarSQLCommand("INSERT INTO cita_medico_paciente(Id_Paciente, Id_Medico, Id_Cita) VALUES ('$row[0]',0,'$row2[0]')");    
	ejecutarSQLCommand("UPDATE Citas set Patien = '$row[0]' where Id_Cita='$row2[0]'"); 
 ?>