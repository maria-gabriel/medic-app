<?php 
include ('functions.php');

$Id=$_GET['Id'];
$Valor=$_POST['Valor'];
$Campo=$_GET['Campo'];
    
	ejecutarSQLCommand("UPDATE Fichas set $Campo = '$Valor' where Id_Ficha='$Id'");    

 ?>