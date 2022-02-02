<?php 
include ('functions.php');

//se reciben los datos
$Nombre=$_POST['Nombre'];
$ApellidoP=$_POST['ApellidoP'];
$ApellidoM=$_POST['ApellidoM'];
$Correo=$_POST['Correo'];
$Edad=$_POST['Edad'];
$Telefono=$_POST['Telefono'];
$Contra=$_POST['Contra'];
$Tipo=$_POST['Tipo'];
$Token=$_POST['Token'];

//se ejecuta la consulta de insersión
ejecutarSQLCommand("INSERT INTO Pacientes(Nombre, ApellidoP, ApellidoM, Correo, Edad, Telefono, Contra, Tipo) VALUES ('$Nombre','$ApellidoP','$ApellidoM','$Correo','$Edad','$Telefono','$Contra','$Tipo')");

?>