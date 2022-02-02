<?php 
include ('functions.php');

$Id_Cita=$_GET['Id_Cita'];
$Correo=$_GET['Correo'];
    
	$queri ="select Id_Medico from Medicos where Correo='$Correo'";
	$resultado = mysqli_query($connect,$queri);
	$row = mysqli_fetch_array($resultado);  

	ejecutarSQLCommand("UPDATE cita_medico_paciente set Id_Medico = $row[0] where Id_Cita=$Id_Cita"); 
	ejecutarSQLCommand("UPDATE citas set Doct = $row[0] where Id_Cita=$Id_Cita"); 

 ?>