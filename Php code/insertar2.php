<?php 
include ('functions.php');

$Nombre=$_POST['Nombre'];
$ApellidoP=$_POST['ApellidoP'];
$ApellidoM=$_POST['ApellidoM'];
$Correo=$_POST['Correo'];
$Telefono=$_POST['Telefono'];
$Cedula=$_POST['Cedula'];
$Contra=$_POST['Contra'];
$Tipo=$_POST['Tipo'];
$Token=$_POST['Token'];

ejecutarSQLCommand("INSERT INTO Medicos(Nombre, ApellidoP, ApellidoM, Correo, Telefono, Cedula, Contra, Tipo) VALUES ('$Nombre','$ApellidoP','$ApellidoM','$Correo','$Telefono','$Cedula','$Contra','$Tipo')");



 ?>