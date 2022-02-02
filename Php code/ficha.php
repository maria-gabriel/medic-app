<?php 
include ('functions.php');

$Curp=$_POST['Curp'];
$FechaNac=$_POST['FechaNac'];
$TipoSan=$_POST['TipoSan'];
$Genero=$_POST['Genero'];
$EstadoCiv=$_POST['EstadoCiv'];
$Nacionalidad=$_POST['Nacionalidad'];
$Familiar=$_POST['Familiar'];
$Parent=$_POST['Parent'];
$Telefon=$_POST['Telefon'];

ejecutarSQLCommand("INSERT INTO Fichas(Curp, FechaNac, TipoSan, Genero, EstadoCiv, Nacionalidad, Familiar, Parent, Telefon) VALUES ('$Curp','$FechaNac','$TipoSan','$Genero','$EstadoCiv','$Nacionalidad','$Familiar','$Parent','$Telefon')");



 ?>