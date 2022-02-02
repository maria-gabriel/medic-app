<?php 
include ('functions.php');

$Correo=$_POST['Correo'];
$Correo2=$_POST['Correo2'];
$Id_Cita=$_POST['Id_Cita'];
$Diagnostico=$_POST['Diagnostico'];
$Presion=$_POST['Presion'];
$Respiracion=$_POST['Respiracion'];
$Pulso=$_POST['Pulso'];
$Temperatura=$_POST['Temperatura'];
$Motivo=$_POST['Motivo'];
$Prescripcion=$_POST['Prescripcion'];
$Fecha=$_POST['Fecha'];
   
   $queri1 ="select Id_Paciente from Pacientes where Correo='$Correo2'";
   $queri2 ="select Id_Medico from Medicos where Correo='$Correo'";
   $resultado1 = mysqli_query($connect,$queri1);
   $resultado2 = mysqli_query($connect,$queri2);
   $row = mysqli_fetch_array($resultado1);
   $row2 = mysqli_fetch_array($resultado2);

	ejecutarSQLCommand("INSERT INTO Observaciones(Diagnostico,Presion,Respiracion,Pulso,Temperatura,Motivo, Prescripcion, Fecha, Patien) VALUES ('$Diagnostico','$Presion','$Respiracion','$Pulso','$Temperatura','$Motivo','$Prescripcion','$Fecha',$row2[0])");    
	
	$queri3 ="select max(Id_Observacion) from Observaciones";
	$resultado3 = mysqli_query($connect,$queri3);
    $row3 = mysqli_fetch_array($resultado3);
	
	ejecutarSQLCommand("INSERT INTO paciente_observacion(Id_Paciente,Id_Medico,Id_Observacion)VALUES($row2[0],$row[0],$row3[0])"); 
 ?>