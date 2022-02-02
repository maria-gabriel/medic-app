<?php 
include ('functions.php');

$Id=$_GET['Id'];
    
	ejecutarSQLCommand("DELETE FROM Citas WHERE Id_Cita='$Id'");    

 ?>