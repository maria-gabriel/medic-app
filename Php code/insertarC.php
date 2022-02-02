<?php 
include ('functions.php');

$Sintomas=$_POST['Sintomas'];
$Hora=$_POST['Hora'];
$Fecha=$_POST['Fecha'];
$Latitud=$_POST['Latitud'];
$Longitud=$_POST['Longitud'];
$Referencias=$_POST['Referencias'];
$Estado=$_POST['Estado'];
$queri1 ="select Id_Paciente from Pacientes where Correo='$Correo'";
$resultado1 = mysqli_query($connect,$queri1);
$row = mysqli_fetch_array($resultado1);

//funcion para insertar en la tabla citas
ejecutarSQLCommand("INSERT INTO Citas(Sintomas, Hora, Fecha, Latitud, Longitud, Referencias, Estado, Patien) VALUES ('$Sintomas','$Hora','$Fecha','$Latitud','$Longitud','$Referencias','$Estado','$row[0]')");



 ?>