<?php 
include ('functions.php');

$Correo=$_POST['Correo'];

$queri1 ="select Id_Paciente from Pacientes where Correo='$Correo'";
$queri2 ="select max(Id_Ficha) from Fichas";
	$resultado1 = mysqli_query($connect,$queri1);
	$resultado2 = mysqli_query($connect,$queri2);
    $row = mysqli_fetch_array($resultado1);
    $row2 = mysqli_fetch_array($resultado2);
    
	ejecutarSQLCommand("insert into Paciente_Ficha (Id_Paciente,Id_Ficha) values('$row[0]','$row2[0]')");    

 ?>