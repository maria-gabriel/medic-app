<?php 
include ('functions.php');

$Id=$_GET['Id'];
$Valor=$_POST['Valor'];
$Campo=$_GET['Campo'];
    
	ejecutarSQLCommand("UPDATE Medicos set $Campo = '$Valor' where Id_Medico='$Id'");    

 ?>