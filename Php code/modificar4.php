<?php 
include ('functions.php');

$Id=$_GET['Id'];
$Valor=$_POST['Valor'];
$Campo=$_GET['Campo'];
    
	ejecutarSQLCommand("UPDATE Citas set $Campo = '$Valor' where Id_Cita='$Id'");    

 ?>